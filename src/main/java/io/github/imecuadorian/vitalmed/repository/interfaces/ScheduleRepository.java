package io.github.imecuadorian.vitalmed.repository.interfaces;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.*;

import java.time.*;
import java.util.*;

public interface ScheduleRepository extends CRUDRepository<String, Schedule> {

    List<Schedule> findByDoctorId(String doctorId);
    List<Schedule> findByDay(DayOfWeek day);
    List<Schedule> findByRoomId(String roomId);
    List<Schedule> findByDoctorAndWeek(Integer doctorId, LocalDate weekStart);
    List<Schedule> findByDayOfWeekAndRoom(DayOfWeek day, Integer roomId);
}
