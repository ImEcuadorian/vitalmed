package io.github.imecuadorian.vitalmed.model;

import lombok.*;

import java.time.*;

@Builder @With
public record User(
        Integer id,
        String cedula,
        String fullName,
        String email,
        String phone,
        String cell,
        String address,
        String passwordHash,
        Role role,
        Instant createdAt,
        Instant updatedAt
) implements BaseEntity<Integer> {}
