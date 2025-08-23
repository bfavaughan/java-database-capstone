package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final MainService service;

    @Autowired
    public DoctorController(DoctorService doctorService, MainService service) {
        this.doctorService = doctorService;
        this.service = service;
    }


    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable LocalDate date,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();
        try {
            ResponseEntity<Map<String, String>> valid = service.validateToken(token, user);
            if (!valid.getStatusCode().is2xxSuccessful()) {
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(403).body(response);
            }
            response.put("success",doctorService.getDoctorAvailability(doctorId, date));
            return ResponseEntity.ok(response) ;
        } catch (Exception e) {
            response.put("message", "Error retrieving availability");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Doctor> doctors = doctorService.getDoctors();
            response.put("doctors", doctors);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error fetching doctors");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, Object>> saveDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {
        Map<String, Object> response = new HashMap<>();
        int saveDoc = doctorService.saveDoctor(doctor);
        response.put("success","done");
        return ResponseEntity.ok(response);
    }

    // 6. Doctor login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    // 7. Update doctor (admin only)
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {
        Map<String, String> response = new HashMap<>();
        ResponseEntity<Map<String, String>> valid = service.validateToken(token, "doctor");
        if (!valid.getStatusCode().is2xxSuccessful()) {
            response.put("message", "Invalid or expired token");
            return ResponseEntity.status(403).body(response);
        }
        response.put("success", String.valueOf(doctorService.updateDoctor(doctor)));
        return ResponseEntity.ok(response) ;
    }

    // 8. Delete doctor (admin only)
    @DeleteMapping("/{doctorId}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable Long doctorId,
            @PathVariable String token) {
        Map<String, String> response = new HashMap<>();
        ResponseEntity<Map<String, String>> valid = service.validateToken(token, "doctor");
        if (!valid.getStatusCode().is2xxSuccessful()) {
            response.put("message", "Invalid or expired token");
            return ResponseEntity.status(403).body(response);
        }
        response.put("success", String.valueOf(doctorService.deleteDoctor(doctorId)));
        return ResponseEntity.ok(response) ;
    }

    // 9. Filter doctors
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {
        return service.filterDoctor(name, time, speciality);
    }

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST controller that serves JSON responses.
//    - Use `@RequestMapping("${api.path}doctor")` to prefix all endpoints with a configurable API path followed by "doctor".
//    - This class manages doctor-related functionalities such as registration, login, updates, and availability.


// 2. Autowire Dependencies:
//    - Inject `DoctorService` for handling the core logic related to doctors (e.g., CRUD operations, authentication).
//    - Inject the shared `Service` class for general-purpose features like token validation and filtering.


// 3. Define the `getDoctorAvailability` Method:
//    - Handles HTTP GET requests to check a specific doctor’s availability on a given date.
//    - Requires `user` type, `doctorId`, `date`, and `token` as path variables.
//    - First validates the token against the user type.
//    - If the token is invalid, returns an error response; otherwise, returns the availability status for the doctor.


// 4. Define the `getDoctor` Method:
//    - Handles HTTP GET requests to retrieve a list of all doctors.
//    - Returns the list within a response map under the key `"doctors"` with HTTP 200 OK status.


// 5. Define the `saveDoctor` Method:
//    - Handles HTTP POST requests to register a new doctor.
//    - Accepts a validated `Doctor` object in the request body and a token for authorization.
//    - Validates the token for the `"admin"` role before proceeding.
//    - If the doctor already exists, returns a conflict response; otherwise, adds the doctor and returns a success message.


// 6. Define the `doctorLogin` Method:
//    - Handles HTTP POST requests for doctor login.
//    - Accepts a validated `Login` DTO containing credentials.
//    - Delegates authentication to the `DoctorService` and returns login status and token information.


// 7. Define the `updateDoctor` Method:
//    - Handles HTTP PUT requests to update an existing doctor's information.
//    - Accepts a validated `Doctor` object and a token for authorization.
//    - Token must belong to an `"admin"`.
//    - If the doctor exists, updates the record and returns success; otherwise, returns not found or error messages.


// 8. Define the `deleteDoctor` Method:
//    - Handles HTTP DELETE requests to remove a doctor by ID.
//    - Requires both doctor ID and an admin token as path variables.
//    - If the doctor exists, deletes the record and returns a success message; otherwise, responds with a not found or error message.


// 9. Define the `filter` Method:
//    - Handles HTTP GET requests to filter doctors based on name, time, and specialty.
//    - Accepts `name`, `time`, and `speciality` as path variables.
//    - Calls the shared `Service` to perform filtering logic and returns matching doctors in the response.


}
