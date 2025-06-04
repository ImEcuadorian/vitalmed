package io.github.imecuadorian.vitalmed.model;

public enum Rol {

    PATIENT("Paciente"),
    DOCTOR("Doctor/a"),
    ADMIN("Administrador");

    private final String name;

    Rol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
