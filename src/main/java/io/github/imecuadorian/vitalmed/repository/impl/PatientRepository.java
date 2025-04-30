package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.*;

import java.util.logging.*;

public class PatientRepository extends FileRepository<String, Patient> {

    public PatientRepository(String path, Logger logger) {
        super(path, logger);
    }

    @Override
    protected String serialize(Patient patient) {
        return String.join(";",
                patient.getId(),
                patient.getFullName(),
                patient.getEmail(),
                patient.getPassword(),
                patient.getPhone(),
                patient.getMobile(),
                patient.getAddress()
        );
    }

    @Override
    protected Patient deserialize(String line) {
        String[] parts = line.split(";");
        return new Patient(
                parts[0], parts[1], parts[2], parts[3],
                parts[4], parts[5], parts[6]
        );
    }

    @Override
    protected String getId(Patient patient) {
        return patient.getId();
    }
}
