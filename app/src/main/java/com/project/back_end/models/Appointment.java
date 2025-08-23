package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Doctor doctor;

    @ManyToOne
    @NotNull
    private Patient patient;

    @Future
    @NotNull
    private LocalDateTime appointmentTime;

    @NotNull(message = "Please provide a status of 0 for Scheduled or 1 for Completed")
    private int status = 0; // default to scheduled

    // ------------------
    // Transient convenience methods
    // ------------------

    /** Returns the doctor ID without persisting it separately */
    @Transient
    public Long getDoctorId() {
        return doctor != null ? doctor.getId() : null;
    }

    /** Returns the patient ID without persisting it separately */
    @Transient
    public Long getPatientId() {
        return patient != null ? patient.getId() : null;
    }

    /** Calculates the end time of the appointment (1 hour later) */
    @Transient
    public LocalDateTime getEndTime() {
        return appointmentTime != null ? appointmentTime.plusHours(1) : null;
    }

    /** Returns just the date part of the appointment */
    @Transient
    public LocalDate getAppointmentDate() {
        return appointmentTime != null ? appointmentTime.toLocalDate() : null;
    }

    /** Returns just the time part of the appointment */
    @Transient
    public LocalTime getAppointmentTimeOnly() {
        return appointmentTime != null ? appointmentTime.toLocalTime() : null;
    }

    // ------------------
    // Constructors
    // ------------------

    public Appointment() {}

    public Appointment(Doctor doctor, Patient patient, LocalDateTime appointmentTime) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.status = 0;
    }

    // ------------------
    // Getters & Setters
    // ------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /** Convenience method to mark appointment as completed */
    public void completeAppointment() {
        this.status = 1;
    }
}