package io.github.imecuadorian.vitalmed.model;


import lombok.*;

import java.time.*;

@Builder
@With
public record ClinicalHistory(
        Integer id,
        Integer patientId,
        String recordNumber,
        int age,
        Gender sex,
        String allergies,
        String diseases,
        String medications,
        String surgeries,
        Instant createdAt,
        Instant updatedAt
) implements BaseEntity<Integer> {}
