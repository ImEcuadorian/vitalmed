package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public interface AppointmentSlotService {
    CompletableFuture<AppointmentSlot> createSlot(AppointmentSlot slot);
    CompletableFuture<Optional<AppointmentSlot>> getSlotById(String slotId);
    CompletableFuture<List<AppointmentSlot>> getAllSlots();
    CompletableFuture<List<AppointmentSlot>> getAvailableSlots(String doctorId, LocalDate date);
    CompletableFuture<Void> reserveSlot(String slotId);
    CompletableFuture<AppointmentSlot> updateSlot(String slotId, AppointmentSlot slot);
    CompletableFuture<Void> deleteSlot(String slotId);
}
