package io.github.imecuadorian.vitalmed.model;

import lombok.*;

@Getter
@AllArgsConstructor
public enum Rol {

    PATIENT("Paciente"),
    DOCTOR("Doctor/a"),
    ADMIN("Administrador");

    private final String name;
}
