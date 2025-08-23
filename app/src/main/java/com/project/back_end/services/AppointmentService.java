package com.project.back_end.services;


import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class AppointmentService {


    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    //THOUGHT that we need a contstructor
    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

     public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1; // success
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // failure
        }
    }

    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();

        Appointment existing = appointmentRepository.findById(appointment.getId()).orElse(null);
        if (existing == null) {
            response.put("message", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        }

        //LOOK for doctors in doctor repositry
        Doctor doctor = doctorRepository.findById(appointment.getDoctorId()).orElse(null);
        if (doctor == null) {
            response.put("message", "Invalid doctor ID");
            return ResponseEntity.badRequest().body(response);
        }

        //Is appointment date already passed??
        if (appointment.getAppointmentTime().isBefore(LocalDateTime.now())) {
            response.put("message", "Appointment time cannot be in the past");
            return ResponseEntity.badRequest().body(response);
        }
        
        //Grab existing appointments using the func we just wrote
        List<Appointment> existingAppointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                appointment.getDoctorId(),
                appointment.getAppointmentTime().minusMinutes(10),//10 mins before
                appointment.getAppointmentTime().plusMinutes(60) //see if it's more than 60 minutes
        );
        //if we have any coming back then timeslot taken
        if (!existingAppointments.isEmpty()) {
            response.put("message", "Appointment already booked for this time slot");
            return ResponseEntity.badRequest().body(response);
        }
        // Check if appointment time is in the past
        if (appointment.getAppointmentTime().isBefore(LocalDateTime.now())) {
            response.put("message", "Appointment time cannot be in the past");
            return ResponseEntity.badRequest().body(response);
        }
        appointmentRepository.save(appointment);
        response.put("message", "Appointment updated successfully");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();

        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment == null) {
            response.put("message", "Appointment not found");
        }


        appointmentRepository.delete(appointment);
        response.put("message", "Appointment cancelled successfully");
        return ResponseEntity.ok(response);
    }
    @Transactional
    public Map<String, Object> getAppointments(LocalDate date, String pname) {
        Map<String, Object> response = new HashMap<>();
        if (pname == null && date == null) {
            response.put("message", "Either patient name or date must be provided");
            return response;
        }
    
        LocalDateTime start = (date != null) ? date.atStartOfDay() : null;
        LocalDateTime end = (date != null) ? start.plusDays(1) : null;
    
        List<Appointment> appointments;
    
        if (pname != null && date != null) {
            appointments = appointmentRepository
                    .findByPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                             pname, start, end);
        } else if (date != null) {
            appointments = appointmentRepository
                    .findByAppointmentTimeBetween(start, end);
        } else {
            // fallback in case only pname is provided without date
            response.put("message", "Date must be provided if searching by patient name");
            return response;
        }
    
        response.put("appointments", appointments);
        return response;
    }

    // 8. Change Status Method
    @Transactional
    public boolean changeAppointmentStatus(Long appointmentId, int newStatus) {
        // Find the appointment by ID
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isEmpty()) {
            return false; // Appointment not found
        }

        Appointment appointment = optionalAppointment.get();
        // Update the status
        appointment.setStatus(newStatus);

        // Save changes (optional if using JPA, transactional commit will persist)
        appointmentRepository.save(appointment);

        return true; // Status updated successfully
    }


// 1. **Add @Service Annotation**:
//    - To indicate that this class is a service layer class for handling business logic.
//    - The `@Service` annotation should be added before the class declaration to mark it as a Spring service component.
//    - Instruction: Add `@Service` above the class definition.

// 2. **Constructor Injection for Dependencies**:
//    - The `AppointmentService` class requires several dependencies like `AppointmentRepository`, `Service`, `TokenService`, `PatientRepository`, and `DoctorRepository`.
//    - These dependencies should be injected through the constructor.
//    - Instruction: Ensure constructor injection is used for proper dependency management in Spring.

// 3. **Add @Transactional Annotation for Methods that Modify Database**:
//    - The methods that modify or update the database should be annotated with `@Transactional` to ensure atomicity and consistency of the operations.
//    - Instruction: Add the `@Transactional` annotation above methods that interact with the database, especially those modifying data.

// 4. **Book Appointment Method**:
//    - Responsible for saving the new appointment to the database.
//    - If the save operation fails, it returns `0`; otherwise, it returns `1`.
//    - Instruction: Ensure that the method handles any exceptions and returns an appropriate result code.

// 5. **Update Appointment Method**:
//    - This method is used to update an existing appointment based on its ID.
//    - It validates whether the patient ID matches, checks if the appointment is available for updating, and ensures that the doctor is available at the specified time.
//    - If the update is successful, it saves the appointment; otherwise, it returns an appropriate error message.
//    - Instruction: Ensure proper validation and error handling is included for appointment updates.

// 6. **Cancel Appointment Method**:
//    - This method cancels an appointment by deleting it from the database.
//    - It ensures the patient who owns the appointment is trying to cancel it and handles possible errors.
//    - Instruction: Make sure that the method checks for the patient ID match before deleting the appointment.

// 7. **Get Appointments Method**:
//    - This method retrieves a list of appointments for a specific doctor on a particular day, optionally filtered by the patient's name.
//    - It uses `@Transactional` to ensure that database operations are consistent and handled in a single transaction.
//    - Instruction: Ensure the correct use of transaction boundaries, especially when querying the database for appointments.

// 8. **Change Status Method**:
//    - This method updates the status of an appointment by changing its value in the database.
//    - It should be annotated with `@Transactional` to ensure the operation is executed in a single transaction.
//    - Instruction: Add `@Transactional` before this method to ensure atomicity when updating appointment status.


}
