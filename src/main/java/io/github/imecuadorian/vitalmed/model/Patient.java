package io.github.imecuadorian.vitalmed.model;

import io.github.imecuadorian.library.*;

import java.util.*;

public class Patient extends User {

    private final Generic<List<History>, ?> information;

    public Patient(String id, String fullName, String email, String password, String phone, String mobile, String address) {
        super(id, fullName, email, password, phone, mobile, address, Rol.PATIENT);
        this.information = new Generic<>(new ArrayList<>(), null);
    }

    public List<History> getConsultations() {
        return information.getT1();
    }
    public void addHistory(History history) {
        information.getT1().add(history);
    }
}
