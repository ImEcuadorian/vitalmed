package io.github.imecuadorian.vitalmed.model;

import lombok.*;

@Builder
@With
public record Specialty(
        Integer id,
        String name
) implements BaseEntity<Integer> {
}
