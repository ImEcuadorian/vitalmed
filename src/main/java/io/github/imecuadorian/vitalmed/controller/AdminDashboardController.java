package io.github.imecuadorian.vitalmed.controller;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.service.*;

import java.io.*;
import java.util.*;

public class AdminDashboardController implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final AdminService adminService;

    public AdminDashboardController(AdminService adminService) {
        this.adminService = adminService;
    }

    public boolean addDoctor(Doctor doctor) {
        if (doctor.getId().isBlank() || doctor.getFullName().isBlank() || doctor.getEmail().isBlank()
            || doctor.getPassword().isBlank() || doctor.getSpeciality().isBlank()) {
            return false;
        }
        return adminService.registerDoctor(doctor);
    }

    public List<Patient> getPatients() {
        return adminService.getAllPatients();
    }

    public List<Doctor> getDoctors() {
        return adminService.getAllDoctors();
    }

    public boolean resetPassword(String patientId, String newPassword) {
        return adminService.resetPatientPassword(patientId, newPassword);
    }

    public boolean assignSchedules(String doctorId, List<Schedule> schedules) {
        return adminService.assignSchedules(doctorId, schedules);
    }

    public List<Room> getRooms() {
        return adminService.getAllRooms();
    }

}

