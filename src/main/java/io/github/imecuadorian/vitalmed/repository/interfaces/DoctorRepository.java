package io.github.imecuadorian.vitalmed.repository.interfaces;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.*;

import java.time.*;
import java.util.*;

public interface DoctorRepository extends CRUDRepository<String, Doctor> {

    List<Schedule> findWeeklyScheduleByDoctorId(String doctorId, LocalDate weekStart);

    List<AppointmentSlot> findAvailableSlotsByDoctorAndDate(String doctorId, LocalDate date);

    // ---- Citas ----
    List<Appointment> findAppointmentsForWeek(String doctorId, LocalDate weekStart);

    Appointment updateAppointmentStatus(String appointmentId, AppointmentStatus newStatus);

    List<ClinicalHistory> getPatientHistory(String patientId);

    ClinicalHistory upsertClinicalHistory(ClinicalHistory history);
}
