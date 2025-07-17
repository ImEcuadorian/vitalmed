package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public interface AppointmentService {
    CompletableFuture<Appointment> bookAppointment(Appointment appointment);
    CompletableFuture<Optional<Appointment>> getAppointmentById(String appointmentId);
    CompletableFuture<List<Appointment>> getAllAppointments();
    CompletableFuture<List<Appointment>> getAppointmentsByPatient(Integer patientId);
    CompletableFuture<List<Appointment>> getAppointmentsByDoctor(Integer doctorId);
    CompletableFuture<List<Appointment>> getAppointmentsByDate(LocalDate date);
    CompletableFuture<List<Appointment>> getAppointmentsByDoctorAndWeek(String doctorId, LocalDate weekStart);
    CompletableFuture<List<Appointment>> getAppointmentsByStatusAndDoctor(AppointmentStatus status, String doctorId);

    CompletableFuture<Appointment> updateAppointment(String appointmentId, Appointment appointment);
    CompletableFuture<Void> cancelAppointment(String appointmentId);
}
