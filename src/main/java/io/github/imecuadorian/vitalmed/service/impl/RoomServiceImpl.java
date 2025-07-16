package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.service.*;
import io.github.imecuadorian.vitalmed.thread.*;

import java.util.*;
import java.util.concurrent.*;

public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public CompletableFuture<Room> createRoom(Room room) {
        return CompletableFuture.supplyAsync(() -> {
            if (room == null) {
                throw new IllegalArgumentException("Room cannot be null");
            }
            roomRepository.save(room);
            return room;
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<Optional<Room>> getRoomById(String roomId) {
        return CompletableFuture.supplyAsync(() -> {
            if (roomId == null || roomId.isBlank()) {
                throw new IllegalArgumentException("Room ID cannot be null or empty");
            }
            return roomRepository.findById(roomId);
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<Optional<Room>> getRoomByCode(String code) {
        return CompletableFuture.supplyAsync(() -> {
            if (code == null || code.isBlank()) {
                throw new IllegalArgumentException("Room code cannot be null or empty");
            }
            return roomRepository.findById(code);
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<List<Room>> getAllRooms() {
        return CompletableFuture.supplyAsync(() -> {
            List<Room> rooms = roomRepository.findAll();
            if (rooms == null || rooms.isEmpty()) {
                return Collections.emptyList();
            }
            return rooms;
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<List<Room>> getRoomsBySpeciality(String speciality) {
        return CompletableFuture.supplyAsync(() -> {
            if (speciality == null || speciality.isBlank()) {
                throw new IllegalArgumentException("Speciality cannot be null or empty");
            }
            List<Room> rooms = roomRepository.findBySpeciality(speciality);
            if (rooms == null || rooms.isEmpty()) {
                return Collections.emptyList();
            }
            return rooms;
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<Optional<Room>> getRoomsByNumber(int number) {
        return CompletableFuture.supplyAsync(() -> {
            if (number <= 0) {
                throw new IllegalArgumentException("Room number must be greater than zero");
            }
            return roomRepository.findByNumber(number);
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<Room> updateRoom(String roomId, Room room) {
        return CompletableFuture.supplyAsync(() -> {
            if (roomId == null || roomId.isBlank()) {
                throw new IllegalArgumentException("Room ID cannot be null or empty");
            }
            if (room == null) {
                throw new IllegalArgumentException("Room cannot be null");
            }
            roomRepository.update(roomId, room);
            return room;
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<Void> deleteRoom(String roomId) {
        return CompletableFuture.runAsync(() -> {
            if (roomId == null || roomId.isBlank()) {
                throw new IllegalArgumentException("Room ID cannot be null or empty");
            }
            roomRepository.delete(roomId);
        }, AppExecutors.db());
    }
}
