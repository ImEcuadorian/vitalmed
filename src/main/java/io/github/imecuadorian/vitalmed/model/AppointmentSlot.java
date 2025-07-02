package io.github.imecuadorian.vitalmed.model;

import lombok.*;

import java.time.*;

@Builder
@With
public record AppointmentSlot(
        Integer id,
        Schedule schedule,
        LocalTime slotTime,
        boolean isAvailable
) implements BaseEntity<Integer> {}