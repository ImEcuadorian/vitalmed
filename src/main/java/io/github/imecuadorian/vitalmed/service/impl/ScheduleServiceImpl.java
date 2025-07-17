package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.service.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class ScheduleServiceImpl implements ScheduleService {


    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    @Override
    public CompletableFuture<Schedule> createSchedule(Schedule schedule) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                scheduleRepository.save(schedule);
                return schedule;
            } catch (Exception e) {
                throw new RuntimeException("Failed to create schedule", e);
            }
        });
    }

    @Override
    public CompletableFuture<Optional<Schedule>> getScheduleById(String scheduleId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return scheduleRepository.findById(scheduleId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve schedule", e);
            }
        });
    }

    @Override
    public CompletableFuture<List<Schedule>> getAllSchedules() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return scheduleRepository.findAll();
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve all schedules", e);
            }
        });
    }

    @Override
    public CompletableFuture<List<Schedule>> getSchedulesByDoctor(String doctorId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return scheduleRepository.findByDoctorId(doctorId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve schedules for doctor", e);
            }
        });
    }

    @Override
    public CompletableFuture<List<Schedule>> getSchedulesByDay(DayOfWeek day) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return scheduleRepository.findByDay(day);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve schedules for day", e);
            }
        });
    }

    @Override
    public CompletableFuture<List<Schedule>> getSchedulesByRoom(String roomId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return scheduleRepository.findByRoomId(roomId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve schedules for room", e);
            }
        });
    }

    @Override
    public CompletableFuture<List<Schedule>> getSchedulesByDoctorAndWeek(String doctorId, LocalDate weekStart) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return scheduleRepository.findByDoctorAndWeek(Integer.valueOf(doctorId), weekStart);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve schedules for doctor and week", e);
            }
        });
    }

    @Override
    public CompletableFuture<List<Schedule>> getSchedulesByDayAndRoom(DayOfWeek day, String roomId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return scheduleRepository.findByDayOfWeekAndRoom(day, Integer.valueOf(roomId));
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve schedules for day and room", e);
            }
        });
    }

    @Override
    public CompletableFuture<Schedule> updateSchedule(String scheduleId, Schedule schedule) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<Schedule> existingSchedule = scheduleRepository.findById(scheduleId);
                if (existingSchedule.isPresent()) {
                    Schedule updatedSchedule = existingSchedule.get();
                    updatedSchedule.withDoctor(schedule.doctor());
                    updatedSchedule.withRoom(schedule.room());
                    updatedSchedule.withDayOfWeek(schedule.dayOfWeek());
                    updatedSchedule.withStartTime(schedule.startTime());
                    updatedSchedule.withEndTime(schedule.endTime());
                    scheduleRepository.save(updatedSchedule);
                    return updatedSchedule;
                } else {
                    throw new RuntimeException("Schedule not found");
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to update schedule", e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteSchedule(String scheduleId) {
        return CompletableFuture.runAsync(() -> {
            try {
                scheduleRepository.delete(scheduleId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete schedule", e);
            }
        });
    }
}
