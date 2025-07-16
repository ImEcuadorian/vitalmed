package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.time.*;
import java.util.*;

public interface AppointmentService {
    Appointment bookAppointment(Appointment appointment);
    Optional<Appointment> getAppointmentById(String appointmentId);
    List<Appointment> getAllAppointments();
    List<Appointment> getAppointmentsByPatient(String patientId);
    List<Appointment> getAppointmentsByDoctor(String doctorId);
    List<Appointment> getAppointmentsByDate(LocalDate date);
    List<Appointment> getAppointmentsByDoctorAndWeek(String doctorId, LocalDate weekStart);
    List<Appointment> getAppointmentsByStatusAndDoctor(AppointmentStatus status, String doctorId);

    Appointment updateAppointment(String appointmentId, Appointment appointment);
    void cancelAppointment(String appointmentId);
}
