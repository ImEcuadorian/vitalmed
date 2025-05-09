package io.github.imecuadorian.vitalmed.controller;

import io.github.imecuadorian.vitalmed.model.Doctor;
import io.github.imecuadorian.vitalmed.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboardController {

    private final AdminService adminService;

    public AdminDashboardController(AdminService adminService) {
        this.adminService = adminService;
    }

    public void addDoctor(Doctor doctor, DefaultTableModel model) {
        if (doctor.getId().isBlank() || doctor.getFullName().isBlank() || doctor.getEmail().isBlank()
            || doctor.getPassword().isBlank() || doctor.getSpeciality().isBlank()) {
            JOptionPane.showMessageDialog(null, "Complete todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean registered = adminService.registerDoctor(doctor);
        if (registered) {
            model.addRow(new Object[]{doctor.getId(), doctor.getFullName(), doctor.getSpeciality()});
            JOptionPane.showMessageDialog(null, "Doctor registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo registrar el doctor. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

