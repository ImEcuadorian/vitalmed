package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.service.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public CompletableFuture<List<Schedule>> getWeeklySchedule(String doctorId, LocalDate weekStart) {
        return CompletableFuture.supplyAsync(() ->
                doctorRepository.findWeeklyScheduleByDoctorId(doctorId, weekStart)
        );
    }

    @Override
    public CompletableFuture<List<AppointmentSlot>> getAvailableSlots(String doctorId, LocalDate date) {
        return CompletableFuture.supplyAsync(() ->
                doctorRepository.findAvailableSlotsByDoctorAndDate(doctorId, date)
        );
    }

    @Override
    public CompletableFuture<List<Appointment>> getAppointmentsForWeek(String doctorId, LocalDate weekStart) {
        return CompletableFuture.supplyAsync(() ->
                doctorRepository.findAppointmentsForWeek(doctorId, weekStart)
        );
    }

    @Override
    public CompletableFuture<Appointment> updateAppointmentStatus(String appointmentId, AppointmentStatus newStatus) {
        return CompletableFuture.supplyAsync(() ->
                doctorRepository.updateAppointmentStatus(appointmentId, newStatus)
        );
    }

    @Override
    public CompletableFuture<List<ClinicalHistory>> getPatientHistory(String patientId) {
        return CompletableFuture.supplyAsync(() ->
                doctorRepository.getPatientHistory(patientId)
        );
    }

    @Override
    public CompletableFuture<ClinicalHistory> upsertClinicalHistory(ClinicalHistory history) {
        return CompletableFuture.supplyAsync(() ->
                doctorRepository.upsertClinicalHistory(history)
        );
    }
}
