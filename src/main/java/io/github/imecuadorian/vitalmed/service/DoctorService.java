package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.util.*;

public interface DoctorService {

    Optional<Doctor> login(String email, String password);
    List<Appointment> getAppointments(String doctorId);
    void updateSchedule(String doctorId, List<Schedule> newSchedule);

}
