package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.util.*;

public interface DoctorService {

    List<Appointment> getAppointments(String doctorId);
    void updateSchedule(String doctorId, List<Schedule> newSchedule);

}
