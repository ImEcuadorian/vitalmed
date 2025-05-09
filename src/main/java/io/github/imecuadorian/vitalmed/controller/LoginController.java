package io.github.imecuadorian.vitalmed.controller;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.service.*;
import io.github.imecuadorian.vitalmed.util.*;


public class LoginController {
    private final AdminService adminService;
    private final AuthService<Doctor> doctorAuthService;
    private final AuthService<Patient> patientAuthService;

    public LoginController(AdminService adminService,
                           AuthService<Doctor> doctorAuthService,
                           AuthService<Patient> patientAuthService) {
        this.adminService = adminService;
        this.doctorAuthService = doctorAuthService;
        this.patientAuthService = patientAuthService;
    }

    public User login(String email, String password) {
        if (email.equals(Constants.ADMIN_EMAIL) && password.equals(Constants.ADMIN_PASSWORD)) {
            return new Admin();
        }

        if (doctorAuthService.login(email, password).isPresent()) {
            return doctorAuthService.login(email, password).get();
        }

        if (patientAuthService.login(email, password).isPresent()) {
            return patientAuthService.login(email, password).get();
        }
        return null;
    }

}

