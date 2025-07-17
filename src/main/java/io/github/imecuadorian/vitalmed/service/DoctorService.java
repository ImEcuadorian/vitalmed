package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public interface DoctorService {

    // --- Schedule & slots ---
    /**
     * Get this doctor’s schedule for a particular week.
     */
    CompletableFuture<List<Schedule>> getWeeklySchedule(String doctorId, LocalDate weekStart);

    /**
     * List the free slots you still have on a given date.
     */
    CompletableFuture<List<AppointmentSlot>> getAvailableSlots(String doctorId, LocalDate date);

    // --- Appointments ---
    /**
     * Fetch all upcoming appointments for this doctor.
     */
    CompletableFuture<List<Appointment>> getAppointmentsForWeek(String doctorId, LocalDate weekStart);

    /**
     * Mark an appointment as attended or cancelled.
     */
    CompletableFuture<Appointment> updateAppointmentStatus(String appointmentId, AppointmentStatus newStatus);

    // --- Consultations & history ---
    /**
     * Record a new Consultation (diagnosis, treatment, medication)
     * linked to an existing appointment.
     */
    CompletableFuture<List<ClinicalHistory>> getPatientHistory(String patientId);

    /**
     * Optionally insert or update a clinical history record.
     */
    CompletableFuture<ClinicalHistory> upsertClinicalHistory(ClinicalHistory history);
}