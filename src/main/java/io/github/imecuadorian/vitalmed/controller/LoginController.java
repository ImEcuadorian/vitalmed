package io.github.imecuadorian.vitalmed.controller;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.service.*;
import io.github.imecuadorian.vitalmed.util.*;

import javax.swing.*;

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

    public void login(String email, String password, JFrame currentView) {
        if (email.equals(Constants.ADMIN_EMAIL) && password.equals(Constants.ADMIN_PASSWORD)) {
            JOptionPane.showMessageDialog(currentView, "Bienvenido Administrador");
            currentView.dispose();
            return;
        }

        doctorAuthService.login(email, password).ifPresentOrElse(doctor -> {
            JOptionPane.showMessageDialog(currentView, "Bienvenido Dr. " + doctor.getFullName());
            currentView.dispose();
        }, () -> patientAuthService.login(email, password).ifPresentOrElse(patient -> {
            JOptionPane.showMessageDialog(currentView, "Bienvenido " + patient.getFullName());
            currentView.dispose();
        }, () -> JOptionPane.showMessageDialog(currentView, "Credenciales inválidas")));
    }
}

