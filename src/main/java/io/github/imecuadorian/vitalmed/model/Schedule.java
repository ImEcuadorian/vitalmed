package io.github.imecuadorian.vitalmed.model;


import lombok.*;

import java.time.*;

@Builder
@With
public record Schedule(
        Integer id,
        Integer doctorId,
        Room room,
        DayOfWeek dayOfWeek,
        int shiftNumber,
        LocalTime startTime,
        LocalTime endTime
) implements BaseEntity<Integer> {
}
