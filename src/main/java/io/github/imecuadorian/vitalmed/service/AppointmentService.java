package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    boolean createAppointment(Appointment appointment);
    List<Appointment> getAppointmentsByDoctor(String doctorId);
    List<Appointment> getAppointmentsByPatient(String patientId);
    boolean isAvailable(String doctorId, String roomId, LocalDateTime dateTime);
}
