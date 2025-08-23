package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MainService service;

    // 3. Get appointments by date and patient name
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(
            @PathVariable("date") String dateStr,
            @PathVariable("patientName") String patientName,
            @PathVariable("token") String token) {

        Map<String, Object> response = new HashMap<>();
        try {
            ResponseEntity<Map<String, String>> valid = service.validateToken(token, "doctor");
            if (valid.getStatusCode().is2xxSuccessful()) {
                LocalDate date = LocalDate.parse(dateStr); // convert string to LocalDate
                response.put("success",appointmentService.getAppointments(date,patientName));
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            response.put("message", "Error fetching appointments");
            return ResponseEntity.status(500).body(response);
        }
    }

    // 4. Book a new appointment
    @PostMapping("/book/{token}")
    public ResponseEntity<Map<String, Object>> bookAppointment(
            @RequestBody Appointment appointment,
            @PathVariable("token") String token) {

        Map<String, Object> response = new HashMap<>();
        try {
            ResponseEntity<Map<String, String>> valid = service.validateToken(token, "patient");
            if (valid.hasBody()) {
                response.put("success",appointmentService.bookAppointment(appointment));
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            response.put("message", "Error booking appointment");
            return ResponseEntity.status(500).body(response);
        }
    }

    // 5. Update an existing appointment
    @PutMapping("/update/{token}")
    public ResponseEntity<Map<String, Object>> updateAppointment(
            @RequestBody Appointment appointment,
            @PathVariable("token") String token) {

        Map<String, Object> response = new HashMap<>();
        try {
            boolean valid = service.validateToken(token, "patient").hasBody();
            if (valid) {
                response.put("success",appointmentService.updateAppointment(appointment));
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            response.put("message", "Error updating appointment");
            return ResponseEntity.status(500).body(response);
        }
    }

    // 6. Cancel an appointment
    @DeleteMapping("/cancel/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> cancelAppointment(
            @PathVariable("appointmentId") Long appointmentId,
            @PathVariable("token") String token) {

        Map<String, Object> response = new HashMap<>();
        try {
            boolean valid = service.validateToken(token, "patient").hasBody();
            if (valid) {
                response.put("success",appointmentService.cancelAppointment(appointmentId,token));
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            response.put("message", "Error canceling appointment");
            return ResponseEntity.status(500).body(response);
        }
    }


// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("/appointments")` to set a base path for all appointment-related endpoints.
//    - This centralizes all routes that deal with booking, updating, retrieving, and canceling appointments.


// 2. Autowire Dependencies:
//    - Inject `AppointmentService` for handling the business logic specific to appointments.
//    - Inject the general `Service` class, which provides shared functionality like token validation and appointment checks.


// 3. Define the `getAppointments` Method:
//    - Handles HTTP GET requests to fetch appointments based on date and patient name.
//    - Takes the appointment date, patient name, and token as path variables.
//    - First validates the token for role `"doctor"` using the `Service`.
//    - If the token is valid, returns appointments for the given patient on the specified date.
//    - If the token is invalid or expired, responds with the appropriate message and status code.


// 4. Define the `bookAppointment` Method:
//    - Handles HTTP POST requests to create a new appointment.
//    - Accepts a validated `Appointment` object in the request body and a token as a path variable.
//    - Validates the token for the `"patient"` role.
//    - Uses service logic to validate the appointment data (e.g., check for doctor availability and time conflicts).
//    - Returns success if booked, or appropriate error messages if the doctor ID is invalid or the slot is already taken.


// 5. Define the `updateAppointment` Method:
//    - Handles HTTP PUT requests to modify an existing appointment.
//    - Accepts a validated `Appointment` object and a token as input.
//    - Validates the token for `"patient"` role.
//    - Delegates the update logic to the `AppointmentService`.
//    - Returns an appropriate success or failure response based on the update result.


// 6. Define the `cancelAppointment` Method:
//    - Handles HTTP DELETE requests to cancel a specific appointment.
//    - Accepts the appointment ID and a token as path variables.
//    - Validates the token for `"patient"` role to ensure the user is authorized to cancel the appointment.
//    - Calls `AppointmentService` to handle the cancellation process and returns the result.


}
