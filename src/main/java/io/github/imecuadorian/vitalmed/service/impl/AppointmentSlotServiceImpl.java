package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.service.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    private final AppointmentSlotRepository appointmentSlotRepository;

    public AppointmentSlotServiceImpl(AppointmentSlotRepository appointmentSlotRepository) {
        this.appointmentSlotRepository = appointmentSlotRepository;
    }
    @Override
    public CompletableFuture<AppointmentSlot> createSlot(AppointmentSlot slot) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                appointmentSlotRepository.save(slot);
                return slot;
            } catch (Exception e) {
                throw new RuntimeException("Failed to create appointment slot", e);
            }
        });
    }

    @Override
    public CompletableFuture<Optional<AppointmentSlot>> getSlotById(String slotId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return appointmentSlotRepository.findById(slotId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve appointment slot", e);
            }
        });
    }

    @Override
    public CompletableFuture<List<AppointmentSlot>> getAllSlots() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return appointmentSlotRepository.findAll();
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve all appointment slots", e);
            }
        });
    }

    @Override
    public CompletableFuture<List<AppointmentSlot>> getAvailableSlots(String doctorId, LocalDate date) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return appointmentSlotRepository.findAvailableByDoctorAndDate(Integer.parseInt(doctorId), date);
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve available slots", e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> reserveSlot(String slotId) {
        return CompletableFuture.runAsync(() -> {
            try {
                appointmentSlotRepository.reserveSlot(Integer.parseInt(slotId));
            } catch (Exception e) {
                throw new RuntimeException("Failed to reserve appointment slot", e);
            }
        });
    }

    @Override
    public CompletableFuture<AppointmentSlot> updateSlot(String slotId, AppointmentSlot slot) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<AppointmentSlot> existingSlotOpt = appointmentSlotRepository.findById(slotId);
                if (existingSlotOpt.isPresent()) {
                    AppointmentSlot existingSlot = existingSlotOpt.get();
                    existingSlot.withSlotTime(slot.slotTime());
                    existingSlot.withAvailable(slot.isAvailable());
                    appointmentSlotRepository.save(existingSlot);
                    return existingSlot;
                } else {
                    throw new RuntimeException("Appointment slot not found");
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to update appointment slot", e);
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteSlot(String slotId) {
        return CompletableFuture.runAsync(() -> {
            try {
                appointmentSlotRepository.delete(slotId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete appointment slot", e);
            }
        });
    }
}
