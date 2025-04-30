package io.github.imecuadorian.vitalmed.model;

import io.github.imecuadorian.library.*;

import java.time.*;

public class Appointment {

    private final Generic<String, String> information;
    private final Generic<String, LocalDateTime> data;

    public Appointment(String id, String patientId, String doctorId, String speciality, String room, LocalDateTime dateTime) {
        this.information = new Generic<>(id, patientId, doctorId, speciality);
        this.data = new Generic<>(room, dateTime);
    }

    public String getId() {
        return information.getT1();
    }

    public String getPatientId() {
        return information.getT2();
    }

    public String getDoctorId() {
        return information.getS1();
    }

    public String getSpeciality() {
        return information.getS2();
    }

    public String getRoom() {
        return data.getT1();
    }

    public LocalDateTime getDateTime() {
        return data.getS1();
    }

}
