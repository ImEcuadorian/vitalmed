package io.github.imecuadorian.vitalmed.repository.interfaces;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.*;

import java.time.*;
import java.util.*;

public interface AppointmentSlotRepository extends CRUDRepository<String, AppointmentSlot> {

    List<AppointmentSlot> findAvailableByDoctorAndDate(Integer doctorId, LocalDate date);

    void reserveSlot(Integer slotId);
}
