package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.Appointment;
import io.github.imecuadorian.vitalmed.model.Patient;
import io.github.imecuadorian.vitalmed.repository.Repository;
import io.github.imecuadorian.vitalmed.service.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientServiceImpl implements PatientService {

    private final Repository<String, Patient> patientRepository;
    private final Repository<String, Appointment> appointmentRepository;
    private final Logger logger;

    public PatientServiceImpl(Repository<String, Patient> patientRepository,
                              Repository<String, Appointment> appointmentRepository,
                              Logger logger) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.logger = logger;
    }

    @Override
    public boolean register(Patient patient) {
        boolean exists = patientRepository.findById(patient.getId()).isPresent();
        if (exists) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(String.format("Patient with ID %s already exists", patient.getId()));
            }
            return false;
        }
        boolean emailExists = patientRepository.findByEmail(patient.getEmail()).isPresent();
        if (emailExists) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(String.format("Email %s already registered", patient.getEmail()));
            }
            return false;
        }
        boolean cellphoneExists = patientRepository.findByCellphone(patient.getMobile()).isPresent();
        if (cellphoneExists) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(String.format("Cellphone %s already registered", patient.getMobile()));
            }
            return false;
        }
        patientRepository.save(patient);
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Patient registered: %s", patient.getId()));
        }
        return true;
    }

    @Override
    public boolean scheduleAppointment(Appointment appointment) {
        boolean occupied = appointmentRepository.findAll().stream()
                .anyMatch(existing ->
                        existing.getDoctorId().equals(appointment.getDoctorId()) &&
                        existing.getDateTime().equals(appointment.getDateTime())
                );

        if (occupied) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(String.format("Schedule conflict for doctor %s at %s",
                        appointment.getDoctorId(), appointment.getDateTime()));
            }
            return false;
        }

        appointmentRepository.save(appointment);
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Appointment scheduled for patient %s with doctor %s at %s",
                    appointment.getPatientId(), appointment.getDoctorId(), appointment.getDateTime()));
        }
        return true;
    }

    @Override
    public List<Appointment> getAppointments(String patientId) {
        return appointmentRepository.findAll().stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .toList();
    }
}
