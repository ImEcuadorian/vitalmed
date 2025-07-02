package io.github.imecuadorian.vitalmed.model;

import lombok.*;

import java.time.*;

@Builder @With
public record Patient(User user) implements BaseEntity<Integer> {
    @Override
    public Integer id() {
        return user.id();
    }

    public String cedula()      { return user.cedula(); }
    public String fullName()    { return user.fullName(); }
    public String email()       { return user.email(); }
    public String phone()       { return user.phone(); }
    public String cell()        { return user.cell(); }
    public String address()     { return user.address(); }
    public String passwordHash(){ return user.passwordHash(); }
    public Role role()          { return Role.PATIENT; }
    public Instant createdAt()  { return user.createdAt(); }
    public Instant updatedAt()  { return user.updatedAt(); }
}
