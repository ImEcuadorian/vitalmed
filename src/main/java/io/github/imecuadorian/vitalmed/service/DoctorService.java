package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.time.*;
import java.util.*;

public interface DoctorService {

    // --- Schedule & slots ---
    /**
     * Get this doctor’s schedule for a particular week.
     */
    List<Schedule> getWeeklySchedule(String doctorId, LocalDate weekStart);

    /**
     * List the free slots you still have on a given date.
     */
    List<AppointmentSlot> getAvailableSlots(String doctorId, LocalDate date);

    // --- Appointments ---
    /**
     * Fetch all upcoming appointments for this doctor.
     */
    List<Appointment> getAppointmentsForWeek(String doctorId, LocalDate weekStart);

    /**
     * Mark an appointment as attended or cancelled.
     */
    Appointment updateAppointmentStatus(String appointmentId, AppointmentStatus newStatus);

    // --- Consultations & history ---
    /**
     * Record a new Consultation (diagnosis, treatment, medication)
     * linked to an existing appointment.
     */
    Consultation recordConsultation(String appointmentId, Consultation consultation);

    /**
     * Fetch full clinical history for a given patient.
     */
    List<ClinicalHistory> getPatientHistory(String patientId);

    /**
     * Optionally insert or update a clinical history record.
     */
    ClinicalHistory upsertClinicalHistory(ClinicalHistory history);
}