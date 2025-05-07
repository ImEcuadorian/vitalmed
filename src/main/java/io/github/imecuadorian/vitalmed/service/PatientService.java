package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.Appointment;
import io.github.imecuadorian.vitalmed.model.Patient;

import java.util.List;

public interface PatientService {
    boolean register(Patient patient);
    boolean scheduleAppointment(Appointment appointment);
    List<Appointment> getAppointments(String patientId);
}
