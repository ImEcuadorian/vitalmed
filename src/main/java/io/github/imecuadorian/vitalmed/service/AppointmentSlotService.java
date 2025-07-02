package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.time.*;
import java.util.*;

public interface AppointmentSlotService {
    AppointmentSlot createSlot(AppointmentSlot slot);
    Optional<AppointmentSlot> getSlotById(String slotId);
    List<AppointmentSlot> getAllSlots();
    List<AppointmentSlot> getAvailableSlots(String doctorId, LocalDate date);
    void reserveSlot(String slotId);
    AppointmentSlot updateSlot(String slotId, AppointmentSlot slot);
    void deleteSlot(String slotId);
}
