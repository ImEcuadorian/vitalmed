package io.github.imecuadorian.vitalmed.model;

import io.github.imecuadorian.library.*;

public class History {

    private final Generic<String, String> information;
    private final Generic<Integer, String> data;
    public History(String id, int age, String gender, String allergies, String diseases, String medications, String operations) {
        this.information = new Generic<>(id, gender, allergies, diseases);
        this.data = new Generic<>(age, null, medications, operations);
    }

    public String getId() {
        return information.getT1();
    }
    public int getAge() {
        return data.getT1();
    }
    public String getGender() {
        return information.getT2();
    }
    public String getAllergies() {
        return information.getS1();
    }
    public String getDiseases() {
        return information.getS2();
    }
    public String getMedications() {
        return data.getS1();
    }
    public String getOperations() {
        return data.getS2();
    }
}
