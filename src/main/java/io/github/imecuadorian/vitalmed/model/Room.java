package io.github.imecuadorian.vitalmed.model;

import lombok.*;
import org.jetbrains.annotations.*;

@Builder
@With
public record Room(
        Integer id,
        String code,

        Integer number,
        Specialty specialty

) implements BaseEntity<Integer> {

    @Override
    public @NotNull String toString() {
        return "Sala " + number + " (" + code + ") - " + specialty.name();
    }
}

