package io.github.imecuadorian.vitalmed.model;

import lombok.*;
import org.jetbrains.annotations.*;

@Builder
@With
public record Specialty(
        Integer id,
        String name
) implements BaseEntity<Integer> {
    @Override
    public @NotNull String toString() {
        return name;
    }
}
