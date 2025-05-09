package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.*;

import java.time.*;
import java.util.logging.*;

public class AppointmentRepository extends FileRepository<String, Appointment> {

    public AppointmentRepository(String path, Logger logger) {
        super(path, logger);
    }

    @Override
    protected String serialize(Appointment entity) {
        return String.join(";",
                entity.getId(),
                entity.getPatientId(),
                entity.getDoctorId(),
                entity.getSpeciality(),
                entity.getRoom().toString(),
                entity.getDateTime().toString()
        );
    }

    @Override
    protected Appointment deserialize(String line) {
        String[] parts = line.split(";");
        return new Appointment(
                parts[0], parts[1], parts[2], parts[3],
                new Room("1","2",3), LocalDateTime.parse(parts[5])
        );
    }

    @Override
    protected String getId(Appointment entity) {
        return entity.getId();
    }
}
