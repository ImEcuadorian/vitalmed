package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public interface ScheduleService {
    CompletableFuture<Schedule> createSchedule(Schedule schedule);
    CompletableFuture<Optional<Schedule>> getScheduleById(String scheduleId);
   CompletableFuture<List<Schedule>> getAllSchedules();
    CompletableFuture<List<Schedule>> getSchedulesByDoctor(String doctorId);
    CompletableFuture<List<Schedule>> getSchedulesByDay(DayOfWeek day);
    CompletableFuture<List<Schedule>> getSchedulesByRoom(String roomId);
    CompletableFuture<List<Schedule>> getSchedulesByDoctorAndWeek(String doctorId, LocalDate weekStart);
    CompletableFuture<List<Schedule>> getSchedulesByDayAndRoom(DayOfWeek day, String roomId);
    CompletableFuture<Schedule> updateSchedule(String scheduleId, Schedule schedule);
    CompletableFuture<Void> deleteSchedule(String scheduleId);
}
