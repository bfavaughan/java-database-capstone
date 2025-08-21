package com.project.back_end.controllers;
import com.project.back_end.models.*;
import com.project.back_end.repo.*;
import com.project.back_end.services.*;
import com.project.back_end.DTO.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final MainService service;

    @Autowired
    public PatientController(PatientService patientService, MainService service) {
        this.patientService = patientService;
        this.service = service;
    }

    // 3. Get patient details
    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatient(@PathVariable String token) {
        return patientService.getPatientDetails(token);
    }

    // 4. Create new patient
    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        return patientService.createPatient(patient);
    }

    // 5. Patient login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    // 6. Get patient appointments
    @GetMapping("/appointments/{patientId}/{token}/{user}")
    public ResponseEntity<Map<String, Object>> getPatientAppointment(
            @PathVariable Long patientId,
            @PathVariable String token,
            @PathVariable String user) {

        boolean valid = service.validateToken(token, user);
        if (!valid) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        return patientService.getPatientAppointment(patientId, token);
    }

    // 7. Filter patient appointments
    @GetMapping("/appointments/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointment(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        boolean valid = service.validateToken(token, "patient");
        if (!valid) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        return patientService.filterPatient(condition, name, token);
    }

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller for patient-related operations.
//    - Use `@RequestMapping("/patient")` to prefix all endpoints with `/patient`, grouping all patient functionalities under a common route.


// 2. Autowire Dependencies:
//    - Inject `PatientService` to handle patient-specific logic such as creation, retrieval, and appointments.
//    - Inject the shared `Service` class for tasks like token validation and login authentication.


// 3. Define the `getPatient` Method:
//    - Handles HTTP GET requests to retrieve patient details using a token.
//    - Validates the token for the `"patient"` role using the shared service.
//    - If the token is valid, returns patient information; otherwise, returns an appropriate error message.


// 4. Define the `createPatient` Method:
//    - Handles HTTP POST requests for patient registration.
//    - Accepts a validated `Patient` object in the request body.
//    - First checks if the patient already exists using the shared service.
//    - If validation passes, attempts to create the patient and returns success or error messages based on the outcome.


// 5. Define the `login` Method:
//    - Handles HTTP POST requests for patient login.
//    - Accepts a `Login` DTO containing email/username and password.
//    - Delegates authentication to the `validatePatientLogin` method in the shared service.
//    - Returns a response with a token or an error message depending on login success.


// 6. Define the `getPatientAppointment` Method:
//    - Handles HTTP GET requests to fetch appointment details for a specific patient.
//    - Requires the patient ID, token, and user role as path variables.
//    - Validates the token using the shared service.
//    - If valid, retrieves the patient's appointment data from `PatientService`; otherwise, returns a validation error.


// 7. Define the `filterPatientAppointment` Method:
//    - Handles HTTP GET requests to filter a patient's appointments based on specific conditions.
//    - Accepts filtering parameters: `condition`, `name`, and a token.
//    - Token must be valid for a `"patient"` role.
//    - If valid, delegates filtering logic to the shared service and returns the filtered result.



}


