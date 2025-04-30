package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.Doctor;
import io.github.imecuadorian.vitalmed.model.Patient;
import io.github.imecuadorian.vitalmed.model.Schedule;
import io.github.imecuadorian.vitalmed.repository.Repository;
import io.github.imecuadorian.vitalmed.service.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminServiceImpl implements AdminService {

    private final Repository<String, Doctor> doctorRepository;
    private final Repository<String, Patient> patientRepository;
    private final Logger logger;

    public AdminServiceImpl(Repository<String, Doctor> doctorRepository,
                            Repository<String, Patient> patientRepository,
                            Logger logger) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.logger = logger;
    }

    @Override
    public boolean registerDoctor(Doctor doctor) {
        if (doctorRepository.findById(doctor.getId()).isPresent()) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(String.format("Doctor with ID %s already exists.", doctor.getId()));
            }
            return false;
        }
        doctorRepository.save(doctor);
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Doctor registered: %s", doctor.getId()));
        }
        return true;
    }

    @Override
    public boolean assignSchedules(String doctorId, List<Schedule> schedules) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(String.format("Doctor not found: %s", doctorId));
            }
            return false;
        }

        Doctor doctor = doctorOpt.get();
        schedules.forEach(doctor::addSchedule);
        doctorRepository.update(doctorId, doctor);

        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Assigned %d schedules to doctor %s", schedules.size(), doctorId));
        }
        return true;
    }

    @Override
    public boolean resetPatientPassword(String patientId, String newPassword) {
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isEmpty()) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(String.format("Patient not found: %s", patientId));
            }
            return false;
        }

        Patient patient = patientOpt.get();
        patient.updatePassword(newPassword);
        patientRepository.update(patientId, patient);

        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Password reset for patient: %s", patientId));
        }
        return true;
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}
