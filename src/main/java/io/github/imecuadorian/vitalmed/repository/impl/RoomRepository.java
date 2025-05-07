package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.library.FileType;
import io.github.imecuadorian.library.Files;
import io.github.imecuadorian.vitalmed.model.Room;
import io.github.imecuadorian.vitalmed.repository.FileRepository;

import java.util.logging.Logger;

public class RoomRepository extends FileRepository<String, Room> {

    public RoomRepository(String fileName, Logger logger) {
        super(fileName, logger);
    }

    @Override
    protected String serialize(Room room) {
        return String.format("%s;%s;%d",
                room.getId(),
                room.getSpecialty(),
                room.getNumber());
    }

    @Override
    protected Room deserialize(String line) {
        String[] parts = line.split(";");
        String id = parts[0];
        String specialty = parts[1];
        int number = Integer.parseInt(parts[2]);
        return new Room(id, specialty, number);
    }

    @Override
    protected String getId(Room room) {
        return room.getId();
    }
}
