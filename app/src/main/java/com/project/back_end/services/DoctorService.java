package com.project.back_end.services;
import com.project.back_end.DTO.*;
import com.project.back_end.models.*;
import com.project.back_end.repo.*;
import com.project.back_end.services.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;        // for ArrayList
import java.time.LocalDateTime;     // for LocalDateTime
import java.util.Optional;          // for Optional
import java.util.HashMap;           // for HashMap

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
// --- Spring Core ---
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;

// --- Spring Web / REST ---
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

// --- Spring Transaction / JPA ---
import org.springframework.transaction.annotation.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.*;

@Service
public class DoctorService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    //THOUGHT that we need a contstructor same as doctor one!
    @Autowired
    public DoctorService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        // 1. Define working hours
        int startHour = 9;
        int endHour = 17;

        // 2. Generate list of all hours in the working day
        List<Integer> hours = new ArrayList<>();
        for (int h = startHour; h <= endHour; h++) {
            hours.add(h);
        }

        // 3. Fetch all appointments for this doctor on that date
        LocalDateTime startOfDay = date.atTime(startHour, 0);
        LocalDateTime endOfDay = date.atTime(endHour, 0);
        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctorId, startOfDay, endOfDay);

        // 4. Remove booked hours from the list
        for (Appointment a : appointments) {
            int bookedHour = a.getAppointmentTime().getHour();
            hours.remove(Integer.valueOf(bookedHour)); // remove the hour
        }

        // 5. Convert remaining hours into "HH:mm-HH:mm" strings
        List<String> availableSlots = new ArrayList<>();
        for (int h : hours) {
            String slot = String.format("%02d:00-%02d:00", h, h + 1);
            availableSlots.add(slot);
        }

        return availableSlots;
    }

    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                return -1; 
            }
    
            doctorRepository.save(doctor);
    
            return 1; 
        } catch (Exception e) {
            e.printStackTrace();
            return 0; 
        }
    }
    public int updateDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findById(doctor.getId()).isEmpty()) {
                return -1; 
            }
    
            doctorRepository.save(doctor);
    
            return 1; 
        } catch (Exception e) {
            e.printStackTrace();
            return 0; 
        }
    }

    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    public int deleteDoctor(long id) {
        try {
            Optional<Doctor> doctorOpt = doctorRepository.findById(id);
            if (doctorOpt.isEmpty()) {
                return -1;
            }
            appointmentRepository.deleteAllByDoctorId(id);
    
            doctorRepository.deleteById(id);
    
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Map<String, String> response = new HashMap<>();
    
        try {
            Doctor doctor = doctorRepository.findByEmail(login.getEmail());
            if (doctor == null) {
                response.put("message", "Doctor not found");
                return ResponseEntity.status(404).body(response);
            }
    
            if (!doctor.getPassword().equals(login.getPassword())) {
                response.put("message", "Invalid password");
                return ResponseEntity.status(401).body(response);
            }
            
            String token = tokenService.generateToken(login.getEmail());
            response.put("token", token);
            return ResponseEntity.ok(response);
    
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error with login");
            return ResponseEntity.status(500).body(response);
        }
    }

    public Map<String, Object> findDoctorByName(String name) {
        Map<String, Object> response = new HashMap<>();
    
        try {
            List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
    
            response.put("doctors", doctors);
    
            if (doctors.isEmpty()) {
                response.put("message", "No doctors found");
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Internal error");
        }
    
        return response;
    }
    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        if (amOrPm == null) return doctors; // no filtering
    
        boolean filterAM = amOrPm.equalsIgnoreCase("AM");
        List<Doctor> filtered = new ArrayList<>();
    
        for (Doctor doctor : doctors) {
            for (String slot : doctor.getAvailableTimes()) {
                int hour = Integer.parseInt(slot.substring(0, 2)); // extract hour from "09:00-10:00" WHY are we storing them like this???
                if ((filterAM && hour < 12) || (!filterAM && hour >= 12)) {
                    filtered.add(doctor); // add doctor if any slot matches AM/PM
                    break; 
                }
            }
        }
    
        return filtered;
    }

    public Map<String,Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm){
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        doctors = filterDoctorByTime(doctors, amOrPm);
    
        Map<String,Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return response;
    }
    
    public Map<String,Object> filterDoctorByNameAndTime(String name, String amOrPm){
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCase(name);
        doctors = filterDoctorByTime(doctors, amOrPm);
    
        Map<String,Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return response;
    }
    
    public Map<String,Object> filterDoctorByNameAndSpecility(String name, String specialty){
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    
        Map<String,Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return response;
    }

    public Map<String,Object> filterDoctorByTimeAndSpecility(String specialty, String amOrPm){
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        doctors = filterDoctorByTime(doctors, amOrPm);
    
        Map<String,Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return response;
    }
    

    public Map<String,Object> filterDoctorBySpecility(String specialty){
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
    
        Map<String,Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return response;
    }
    
    public Map<String,Object> filterDoctorsByTime(String amOrPm){
        List<Doctor> doctors = doctorRepository.findAll();
        doctors = filterDoctorByTime(doctors, amOrPm);
    
        Map<String,Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return response;
    }

// 1. **Add @Service Annotation**:
//    - This class should be annotated with `@Service` to indicate that it is a service layer class.
//    - The `@Service` annotation marks this class as a Spring-managed bean for business logic.
//    - Instruction: Add `@Service` above the class declaration.

// 2. **Constructor Injection for Dependencies**:
//    - The `DoctorService` class depends on `DoctorRepository`, `AppointmentRepository`, and `TokenService`.
//    - These dependencies should be injected via the constructor for proper dependency management.
//    - Instruction: Ensure constructor injection is used for injecting dependencies into the service.

// 3. **Add @Transactional Annotation for Methods that Modify or Fetch Database Data**:
//    - Methods like `getDoctorAvailability`, `getDoctors`, `findDoctorByName`, `filterDoctorsBy*` should be annotated with `@Transactional`.
//    - The `@Transactional` annotation ensures that database operations are consistent and wrapped in a single transaction.
//    - Instruction: Add the `@Transactional` annotation above the methods that perform database operations or queries.

// 4. **getDoctorAvailability Method**:
//    - Retrieves the available time slots for a specific doctor on a particular date and filters out already booked slots.
//    - The method fetches all appointments for the doctor on the given date and calculates the availability by comparing against booked slots.
//    - Instruction: Ensure that the time slots are properly formatted and the available slots are correctly filtered.

// 5. **saveDoctor Method**:
//    - Used to save a new doctor record in the database after checking if a doctor with the same email already exists.
//    - If a doctor with the same email is found, it returns `-1` to indicate conflict; `1` for success, and `0` for internal errors.
//    - Instruction: Ensure that the method correctly handles conflicts and exceptions when saving a doctor.

// 6. **updateDoctor Method**:
//    - Updates an existing doctor's details in the database. If the doctor doesn't exist, it returns `-1`.
//    - Instruction: Make sure that the doctor exists before attempting to save the updated record and handle any errors properly.

// 7. **getDoctors Method**:
//    - Fetches all doctors from the database. It is marked with `@Transactional` to ensure that the collection is properly loaded.
//    - Instruction: Ensure that the collection is eagerly loaded, especially if dealing with lazy-loaded relationships (e.g., available times). 

// 8. **deleteDoctor Method**:
//    - Deletes a doctor from the system along with all appointments associated with that doctor.
//    - It first checks if the doctor exists. If not, it returns `-1`; otherwise, it deletes the doctor and their appointments.
//    - Instruction: Ensure the doctor and their appointments are deleted properly, with error handling for internal issues.

// 9. **validateDoctor Method**:
//    - Validates a doctor's login by checking if the email and password match an existing doctor record.
//    - It generates a token for the doctor if the login is successful, otherwise returns an error message.
//    - Instruction: Make sure to handle invalid login attempts and password mismatches properly with error responses.

// 10. **findDoctorByName Method**:
//    - Finds doctors based on partial name matching and returns the list of doctors with their available times.
//    - This method is annotated with `@Transactional` to ensure that the database query and data retrieval are properly managed within a transaction.
//    - Instruction: Ensure that available times are eagerly loaded for the doctors.


// 11. **filterDoctorsByNameSpecilityandTime Method**:
//    - Filters doctors based on their name, specialty, and availability during a specific time (AM/PM).
//    - The method fetches doctors matching the name and specialty criteria, then filters them based on their availability during the specified time period.
//    - Instruction: Ensure proper filtering based on both the name and specialty as well as the specified time period.

// 12. **filterDoctorByTime Method**:
//    - Filters a list of doctors based on whether their available times match the specified time period (AM/PM).
//    - This method processes a list of doctors and their available times to return those that fit the time criteria.
//    - Instruction: Ensure that the time filtering logic correctly handles both AM and PM time slots and edge cases.


// 13. **filterDoctorByNameAndTime Method**:
//    - Filters doctors based on their name and the specified time period (AM/PM).
//    - Fetches doctors based on partial name matching and filters the results to include only those available during the specified time period.
//    - Instruction: Ensure that the method correctly filters doctors based on the given name and time of day (AM/PM).

// 14. **filterDoctorByNameAndSpecility Method**:
//    - Filters doctors by name and specialty.
//    - It ensures that the resulting list of doctors matches both the name (case-insensitive) and the specified specialty.
//    - Instruction: Ensure that both name and specialty are considered when filtering doctors.


// 15. **filterDoctorByTimeAndSpecility Method**:
//    - Filters doctors based on their specialty and availability during a specific time period (AM/PM).
//    - Fetches doctors based on the specified specialty and filters them based on their available time slots for AM/PM.
//    - Instruction: Ensure the time filtering is accurately applied based on the given specialty and time period (AM/PM).

// 16. **filterDoctorBySpecility Method**:
//    - Filters doctors based on their specialty.
//    - This method fetches all doctors matching the specified specialty and returns them.
//    - Instruction: Make sure the filtering logic works for case-insensitive specialty matching.

// 17. **filterDoctorsByTime Method**:
//    - Filters all doctors based on their availability during a specific time period (AM/PM).
//    - The method checks all doctors' available times and returns those available during the specified time period.
//    - Instruction: Ensure proper filtering logic to handle AM/PM time periods.

   
}
