package io.github.imecuadorian.vitalmed.controller;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.service.AdminService;

import javax.swing.table.DefaultTableModel;
import java.util.*;

public class AdminDashboardController {

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

    public boolean resetPassword(String patientId, String newPassword) {
        return adminService.resetPatientPassword(patientId, newPassword);
    }

}

