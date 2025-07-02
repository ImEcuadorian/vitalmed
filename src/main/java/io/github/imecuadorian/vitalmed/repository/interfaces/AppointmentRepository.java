package io.github.imecuadorian.vitalmed.repository.interfaces;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.*;

import java.time.*;
import java.util.*;

public interface AppointmentRepository extends CRUDRepository<String, Appointment> {
    List<Appointment> findByPatient(Integer patientId);
    List<Appointment> findByDoctor(Integer doctorId);
    List<Appointment> findByDate(LocalDate date);
    List<Appointment> findByDoctorAndWeek(Integer doctorId, LocalDate weekStart);
    List<Appointment> findByStatusAndDoctor(AppointmentStatus status, Integer doctorId);
}
