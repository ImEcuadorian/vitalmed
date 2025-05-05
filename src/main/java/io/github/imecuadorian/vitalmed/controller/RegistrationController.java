package io.github.imecuadorian.vitalmed.controller;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.service.*;

public class RegistrationController {

    private final PatientService patientService;

    public RegistrationController(PatientService patientService) {
        this.patientService = patientService;
    }
    public boolean register(Patient patient) {
        return patientService.register(patient);
    }
}
