package io.github.imecuadorian.vitalmed.factory;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.*;
import io.github.imecuadorian.vitalmed.repository.impl.*;
import io.github.imecuadorian.vitalmed.service.*;
import io.github.imecuadorian.vitalmed.service.impl.*;
import lombok.*;

import java.util.logging.Logger;

public class ServiceFactory {
    private static final Logger logger = Logger.getLogger("VitalmedLogger");
    private static final String DOCTOR_FILE = "doctores.txt";
    private static final String PATIENT_FILE = "pacientes.txt";
    private static final String APPOINTMENT_FILE = "citas.txt";
    private static final Repository<String, Doctor> doctorRepository =
            new DoctorRepository(DOCTOR_FILE, logger);

    private static final Repository<String, Patient> patientRepository =
            new PatientRepository(PATIENT_FILE, logger);

    private static final Repository<String, Appointment> appointmentRepository =
            new AppointmentRepository(APPOINTMENT_FILE, logger);

    @Getter
    private static final DoctorService doctorService =
            new DoctorServiceImpl(doctorRepository, appointmentRepository, logger);

    @Getter
    private static final PatientService patientService =
            new PatientServiceImpl(patientRepository, appointmentRepository, logger);

    @Getter
    private static final AdminService adminService =
            new AdminServiceImpl(doctorRepository, patientRepository, logger);

    @Getter
    private static final AuthService<Doctor> doctorAuth = new GenericAuthService<>(doctorRepository, logger);

    @Getter
    private static final AuthService<Patient> patientAuth = new GenericAuthService<>(patientRepository, logger);

    private ServiceFactory() {
        throw new UnsupportedOperationException("ServiceFactory is a utility class and cannot be instantiated.");
    }
}
