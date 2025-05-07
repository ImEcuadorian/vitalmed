package io.github.imecuadorian.vitalmed.model;

import io.github.imecuadorian.library.*;

import java.time.*;

public class Schedule {
    private final Generic<LocalTime, LocalTime> time;
    private final Generic<DayOfWeek, Room> information;

    public Schedule(DayOfWeek day, LocalTime startTime, LocalTime endTime, Room room) {
        this.time = new Generic<>(startTime, endTime);
        this.information = new Generic<>(day, room);
    }

    public LocalTime getStartTime() {
        return time.getT1();
    }

    public LocalTime getEndTime() {
        return time.getT2();
    }

    public DayOfWeek getDay() {
        return information.getT1();
    }

    public Room getRoom() {
        return information.getS1();
    }
}
