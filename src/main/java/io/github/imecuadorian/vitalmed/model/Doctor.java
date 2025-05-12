package io.github.imecuadorian.vitalmed.model;

import io.github.imecuadorian.library.*;

import java.util.*;

public class Doctor extends User {

    private final Generic<String, List<Schedule>> information;

    public Doctor(String id, String fullName, String email,
                  String password, String phone, String mobile,
                  String address, String speciality) {
        super(id, fullName, email, password, phone, mobile, address, Rol.DOCTOR);
        this.information = new Generic<>(speciality, new ArrayList<>());
    }

    public String getSpeciality() {
        return information.getT1();
    }
    public List<Schedule> getSchedules() {
        return information.getS1();
    }

    public void addSchedule(Schedule schedule) {
        information.getS1().add(schedule);
    }

    @Override
    public String toString() {
        return """
                Doctor/a: %s - Especialidad: %s
                """.formatted(getFullName(), getSpeciality());
    }
}
