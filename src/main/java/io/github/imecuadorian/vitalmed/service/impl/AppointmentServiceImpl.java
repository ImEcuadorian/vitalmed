package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.service.*;
import io.github.imecuadorian.vitalmed.thread.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public CompletableFuture<Appointment> bookAppointment(Appointment appointment) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                appointmentRepository.save(appointment);
                return appointment;
            } catch (Exception e) {
                throw new RuntimeException("Failed to book appointment", e);
            }
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<Optional<Appointment>> getAppointmentById(String appointmentId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return appointmentRepository.findById(appointmentId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve appointment", e);
            }
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<List<Appointment>> getAllAppointments() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return appointmentRepository.findAll();
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve all appointments", e);
            }
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<List<Appointment>> getAppointmentsByPatient(Integer patientId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return appointmentRepository.findByPatient(patientId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve appointments for patient", e);
            }
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<List<Appointment>> getAppointmentsByDoctor(Integer doctorId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return appointmentRepository.findByDoctor(doctorId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve appointments for doctor", e);
            }
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<List<Appointment>> getAppointmentsByDate(LocalDate date) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return appointmentRepository.findByDate(date);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve appointments for date", e);
            }
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<List<Appointment>> getAppointmentsByDoctorAndWeek(String doctorId, LocalDate weekStart) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return appointmentRepository.findByDoctorAndWeek(Integer.parseInt(doctorId), weekStart);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve appointments for doctor and week", e);
            }
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<List<Appointment>> getAppointmentsByStatusAndDoctor(AppointmentStatus status, String doctorId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return appointmentRepository.findByStatusAndDoctor(status, Integer.parseInt(doctorId));
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve appointments by status and doctor", e);
            }
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<Appointment> updateAppointment(String appointmentId, Appointment appointment) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<Appointment> existingAppointmentOpt = appointmentRepository.findById(appointmentId);
                if (existingAppointmentOpt.isPresent()) {
                    Appointment existingAppointment = existingAppointmentOpt.get();
                    Appointment updatedAppointment = existingAppointment.withPatient(appointment.patient())
                            .withDoctor(appointment.doctor())
                            .withSlot(appointment.slot())
                            .withAppointmentDate(appointment.appointmentDate())
                            .withStatus(appointment.status())
                            .withCreatedAt(existingAppointment.createdAt());
                    appointmentRepository.save(updatedAppointment);
                    return updatedAppointment;
                } else {
                    throw new RuntimeException("Appointment not found");
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to update appointment", e);
            }
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<Void> cancelAppointment(String appointmentId) {
        return CompletableFuture.runAsync(() -> {
            try {
                Optional<Appointment> existingAppointmentOpt = appointmentRepository.findById(appointmentId);
                if (existingAppointmentOpt.isPresent()) {
                    Appointment existingAppointment = existingAppointmentOpt.get();
                    Appointment cancelledAppointment = existingAppointment.withStatus(AppointmentStatus.CANCELED);
                    appointmentRepository.save(cancelledAppointment);
                } else {
                    throw new RuntimeException("Appointment not found");
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to cancel appointment", e);
            }
        }, AppExecutors.db());
    }
}
