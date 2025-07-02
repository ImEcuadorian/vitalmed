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

    public List<Patient> getPatients() {
        return adminService.listAllPatients();
    }

    public List<Doctor> getDoctors() {
        return adminService.listAllDoctors();
    }

}

