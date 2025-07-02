package io.github.imecuadorian.vitalmed.model;

import lombok.*;

import java.time.*;
import java.util.*;

@Builder
@With
public record Consultation(
        String appointmentId,
        String specialty,
        String diagnosis,
        String treatment,
        String medication,
        Instant createdAt
) {
    public Consultation {
        // you can validate non-null fields here
        Objects.requireNonNull(appointmentId);
        Objects.requireNonNull(diagnosis);
    }
}
