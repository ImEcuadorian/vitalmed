package io.github.imecuadorian.vitalmed.model;

import lombok.*;

import java.time.*;

@Builder @With
public record Appointment(
        Integer id,
        User patient,
        Doctor doctor,
        AppointmentSlot slot,
        LocalDate appointmentDate,
        AppointmentStatus status,
        Instant createdAt
) implements BaseEntity<Integer> {}