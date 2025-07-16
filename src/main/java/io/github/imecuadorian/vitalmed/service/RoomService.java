package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.util.*;
import java.util.concurrent.*;

public interface RoomService {
    CompletableFuture<Room> createRoom(Room room);
    CompletableFuture<Optional<Room>> getRoomById(String roomId);
    CompletableFuture<Optional<Room>> getRoomByCode(String code);
    CompletableFuture<List<Room>> getAllRooms();
    CompletableFuture<List<Room>> getRoomsBySpeciality(String speciality);
    CompletableFuture<Optional<Room>> getRoomsByNumber(int number);
    CompletableFuture<Room> updateRoom(String roomId, Room room);
    CompletableFuture<Void> deleteRoom(String roomId);
}