package io.github.imecuadorian.vitalmed.model;

import io.github.imecuadorian.library.*;

public class Consultation {

    private final Generic<String, String> information;

    public Consultation(String speciality, String diagnosis, String treatment, String medication) {
        this.information = new Generic<>(speciality, diagnosis, treatment, medication);
    }

    public String getSpeciality() {
        return information.getT1();
    }

    public String getDiagnosis() {
        return information.getT2();
    }

    public String getTreatment() {
        return information.getS1();
    }

    public String getMedication() {
        return information.getS2();
    }

}
