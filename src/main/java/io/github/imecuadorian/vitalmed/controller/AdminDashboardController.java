package io.github.imecuadorian.vitalmed.controller;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.service.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public record AdminDashboardController(AdminService adminService) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public CompletableFuture<List<User>> getPatients() {
        return adminService.listAllPatients();
    }

    public CompletableFuture<List<Doctor>> getDoctors() {
        return adminService.listAllDoctors();
    }
    public CompletableFuture<Doctor> createDoctor(Doctor doctor) {
        return adminService.createDoctor(doctor);
    }

    public CompletableFuture<Doctor> updateDoctor(String doctorId, Doctor doctor) {
        return adminService.updateDoctor(doctorId, doctor);
    }

    public void deleteDoctor(String doctorId) {
        adminService.deleteDoctor(doctorId);
    }

    public CompletableFuture<List<Specialty>> getSpecialties() {
        return adminService.listAllSpecialties();
    }

    public CompletableFuture<List<Room>> getRooms() {
        return adminService.listAllRooms();
    }
}

