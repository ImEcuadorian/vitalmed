package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.time.*;
import java.util.*;

public interface ScheduleService {
    Schedule createSchedule(Schedule schedule);
    Optional<Schedule> getScheduleById(String scheduleId);
    List<Schedule> getAllSchedules();

    List<Schedule> getSchedulesByDoctor(String doctorId);
    List<Schedule> getSchedulesByDay(DayOfWeek day);
    List<Schedule> getSchedulesByRoom(String roomId);
    List<Schedule> getSchedulesByDoctorAndWeek(String doctorId, LocalDate weekStart);
    List<Schedule> getSchedulesByDayAndRoom(DayOfWeek day, String roomId);

    Schedule updateSchedule(String scheduleId, Schedule schedule);
    void deleteSchedule(String scheduleId);
}
