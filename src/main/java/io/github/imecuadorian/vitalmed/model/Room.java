package io.github.imecuadorian.vitalmed.model;

import io.github.imecuadorian.library.*;

public class Room {
    private final Generic<String, String> information;
    private final Generic<Integer, ?> number;

    public Room(String id, String specialty, int number) {
        this.information = new Generic<>(id, specialty);
        this.number = new Generic<>(number);
    }

    public String getId() {
        return information.getT1();
    }

    public String getSpecialty() {
        return information.getS1();
    }

    public int getNumber() {
        return number.getT1();
    }
}
