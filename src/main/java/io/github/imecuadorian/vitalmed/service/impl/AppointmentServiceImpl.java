package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.Appointment;
import io.github.imecuadorian.vitalmed.repository.Repository;
import io.github.imecuadorian.vitalmed.service.AppointmentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentServiceImpl implements AppointmentService {

    private final Repository<String, Appointment> appointmentRepo;
    private final Logger logger;

    public AppointmentServiceImpl(Repository<String, Appointment> appointmentRepo, Logger logger) {
        this.appointmentRepo = appointmentRepo;
        this.logger = logger;
    }

    @Override
    public boolean createAppointment(Appointment appointment) {
        if (!isAvailable(appointment.getDoctorId(), appointment.getRoom().getId() , appointment.getDateTime())) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Conflict: Doctor already has an appointment at " + appointment.getDateTime());
            }
            return false;
        }
        appointmentRepo.save(appointment);
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Appointment created for patient: " + appointment.getPatientId());
        }
        return true;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointmentRepo.findAll().stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .toList();
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointmentRepo.findAll().stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .toList();
    }

    @Override
    public boolean isAvailable(String doctorId, String roomId, LocalDateTime dateTime) {
        return appointmentRepo.findAll().stream()
                .noneMatch(a ->
                        a.getDoctorId().equals(doctorId)
                        && a.getRoom().equals(roomId)
                        && a.getDateTime().equals(dateTime)
                );
    }
}
