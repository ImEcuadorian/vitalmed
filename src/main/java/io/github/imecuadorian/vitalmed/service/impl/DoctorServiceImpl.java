package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.Appointment;
import io.github.imecuadorian.vitalmed.model.Doctor;
import io.github.imecuadorian.vitalmed.model.Schedule;
import io.github.imecuadorian.vitalmed.repository.Repository;
import io.github.imecuadorian.vitalmed.service.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoctorServiceImpl implements DoctorService {

    private final Repository<String, Doctor> doctorRepository;
    private final Repository<String, Appointment> appointmentRepository;
    private final Logger logger;

    public DoctorServiceImpl(Repository<String, Doctor> doctorRepository,
                             Repository<String, Appointment> appointmentRepository,
                             Logger logger) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.logger = logger;
    }

    @Override
    public Optional<Doctor> login(String email, String password) {
        Optional<Doctor> doctorOpt = doctorRepository.findAll().stream()
                .filter(d -> d.validateLogin(email, password))
                .findFirst();
        if (logger.isLoggable(Level.INFO)) {
            logger.info(doctorOpt.isPresent()
                    ? String.format("Login successful for doctor: %s", email)
                    : String.format("Login failed for doctor: %s", email));
        }
        return doctorOpt;
    }

    @Override
    public List<Appointment> getAppointments(String doctorId) {
        List<Appointment> list = appointmentRepository.findAll().stream()
                .filter(app -> app.getDoctorId().equals(doctorId))
                .toList();
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Doctor %s has %d appointments.", doctorId, list.size()));
        }
        return list;
    }

    @Override
    public void updateSchedule(String doctorId, List<Schedule> newSchedule) {
        doctorRepository.findById(doctorId).ifPresentOrElse(doctor -> {
            newSchedule.forEach(doctor::addSchedule);
            doctorRepository.update(doctorId, doctor);
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("Updated schedule for doctor %s", doctorId));
            }
        }, () -> {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(String.format("Doctor not found with ID: %s", doctorId));
            }
        });
    }
}
