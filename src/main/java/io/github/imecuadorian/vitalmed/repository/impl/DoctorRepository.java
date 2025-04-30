package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.vitalmed.model.Doctor;
import io.github.imecuadorian.vitalmed.repository.*;

import java.util.logging.Logger;

public class DoctorRepository extends FileRepository<String, Doctor> {

    public DoctorRepository(String path, Logger logger) {
        super(path, logger);
    }

    @Override
    protected String serialize(Doctor doctor) {
        return String.join(";",
                doctor.getId(),
                doctor.getFullName(),
                doctor.getEmail(),
                doctor.getPassword(),
                doctor.getPhone(),
                doctor.getMobile(),
                doctor.getAddress(),
                doctor.getSpeciality()
        );
    }

    @Override
    protected Doctor deserialize(String line) {
        String[] parts = line.split(";");
        return new Doctor(
                parts[0], parts[1], parts[2], parts[3],
                parts[4], parts[5], parts[6], parts[7]
        );
    }

    @Override
    protected String getId(Doctor doctor) {
        return doctor.getId();
    }
}
