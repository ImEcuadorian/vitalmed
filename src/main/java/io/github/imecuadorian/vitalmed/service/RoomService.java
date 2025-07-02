package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.util.*;

public interface RoomService {
    Room createRoom(Room room);
    Optional<Room> getRoomById(String roomId);
    Optional<Room> getRoomByCode(String code);
    List<Room> getAllRooms();
    List<Room> getRoomsBySpeciality(String speciality);
    List<Room> getRoomsByNumber(int number);
    Room updateRoom(String roomId, Room room);
    void deleteRoom(String roomId);
}