package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.MainService;
import com.project.back_end.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final MainService service;
    private final AppointmentService appointmentService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService,
                                  MainService service,
                                  AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.service = service;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(
            @Valid @RequestBody Prescription prescription,
            @PathVariable String token) {
        ResponseEntity<Map<String, String>> valid = service.validateToken(token, "doctor");
        if (!valid.getStatusCode().is2xxSuccessful()) {

            return ResponseEntity.status(403)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        // Update appointment status before saving
        appointmentService.changeAppointmentStatus(prescription.getAppointmentId(), 1);

        return prescriptionService.savePrescription(prescription);
    }

    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> valid = service.validateToken(token, "doctor");
        if (!valid.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        Map<String, Object> prescription = prescriptionService.getPrescription(appointmentId).getBody();
        if (prescription == null || prescription.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No prescription found for this appointment"));
        }

        return ResponseEntity.ok(prescription);
    }

}
