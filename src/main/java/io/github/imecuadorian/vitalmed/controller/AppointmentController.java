package io.github.imecuadorian.vitalmed.controller;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.service.*;

import java.util.*;
import java.util.concurrent.*;

public record AppointmentController(AppointmentService appointmentService) {

    public CompletableFuture<Appointment> createAppointment(Appointment appointment) {
        return appointmentService.bookAppointment(appointment);
    }

    public CompletableFuture<Appointment> updateAppointment(String appointmentId, Appointment appointment) {
        return appointmentService.updateAppointment(appointmentId, appointment);
    }

}
