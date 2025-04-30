package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.Doctor;
import io.github.imecuadorian.vitalmed.model.Patient;
import io.github.imecuadorian.vitalmed.model.Schedule;

import java.util.List;

public interface AdminService {
    boolean registerDoctor(Doctor doctor);
    boolean assignSchedules(String doctorId, List<Schedule> schedules);
    boolean resetPatientPassword(String patientId, String newPassword);
    List<Patient> getAllPatients();
}
