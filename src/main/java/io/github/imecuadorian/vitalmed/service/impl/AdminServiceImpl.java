package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.service.*;
import io.github.imecuadorian.vitalmed.thread.*;
import io.github.imecuadorian.vitalmed.util.*;
import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

public class AdminServiceImpl implements AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;
    private final RoomRepository roomRepository;

    public AdminServiceImpl(UserRepository userRepository, DoctorRepository doctorRepository, SpecialtyRepository specialtyRepository, RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.specialtyRepository = specialtyRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public CompletableFuture<Doctor> createDoctor(Doctor doctor) {
        return CompletableFuture.supplyAsync(() -> {
            if (userRepository.findByEmail(doctor.email()).isPresent()) {
                LOGGER.error("Email already registered: {}", doctor.email());
                return null;
            }
            if (userRepository.findByCedula(doctor.cedula()).isPresent()) {
                LOGGER.error("Cedula already registered: {}", doctor.cedula());
                return null;
            }
            String hashed = PasswordUtil.hash(doctor.passwordHash());

            User original = doctor.user();
            User userWithHash = original.withPasswordHash(hashed);

            Doctor doctorWithHashedPassword = doctor.withUser(userWithHash);
            try {
                doctorRepository.save(doctorWithHashedPassword);
                LOGGER.info("Doctor created successfully: {}", doctor.user().fullName());
                return doctor;
            } catch (Exception e) {
                LOGGER.error("Error creating doctor: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to create doctor", e);
            }
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<Doctor> updateDoctor(String doctorId, Doctor doctor) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Doctor> existingDoctorOpt = doctorRepository.findById(doctorId);
            if (existingDoctorOpt.isEmpty()) {
                LOGGER.error("Doctor with ID {} not found.", doctorId);
                return null;
            }
            Doctor existingDoctor = existingDoctorOpt.get();
            if (userRepository.findByEmail(doctor.email()).isPresent() &&
                !existingDoctor.user().email().equals(doctor.email())) {
                LOGGER.error("Email already registered: {}", doctor.email());
                return null;
            }
            if (userRepository.findByCedula(doctor.cedula()).isPresent() &&
                !existingDoctor.user().cedula().equals(doctor.cedula())) {
                LOGGER.error("Cedula already registered: {}", doctor.cedula());
                return null;
            }
            String hashed = PasswordUtil.hash(doctor.passwordHash());
            User original = doctor.user();
            User userWithHash = original.withPasswordHash(hashed);
            Doctor doctorWithHashedPassword = doctor.withUser(userWithHash);
            try {
                doctorRepository.update(doctorId, doctorWithHashedPassword);
                LOGGER.info("Doctor updated successfully: {}", doctor.user().fullName());
                return doctorWithHashedPassword;
            } catch (Exception e) {
                LOGGER.error("Error updating doctor: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to update doctor", e);
            }
        }, AppExecutors.db());
    }

    @Override
    public void deleteDoctor(String doctorId) {
        CompletableFuture.runAsync(() -> {
            Optional<Doctor> existingDoctorOpt = doctorRepository.findById(doctorId);
            if (existingDoctorOpt.isEmpty()) {
                LOGGER.error("Doctor with ID {} not found.", doctorId);
                return;
            }
            Doctor existingDoctor = existingDoctorOpt.get();
            try {
                doctorRepository.delete(doctorId);
                LOGGER.info("Doctor deleted successfully: {}", existingDoctor.user().fullName());
            } catch (Exception e) {
                LOGGER.error("Error deleting doctor: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to delete doctor", e);
            }
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<List<Doctor>> listAllDoctors() {
        return CompletableFuture.supplyAsync(() -> {
            List<Doctor> doctors = doctorRepository.findAll();
            if (doctors.isEmpty()) {
                LOGGER.info("No doctors found.");
            } else {
                LOGGER.info("Found {} doctors.", doctors.size());
            }
            return doctors;
        }, AppExecutors.db());
    }

    @Override
    public User createPatient(User patient) {
        return null;
    }

    @Override
    public User updatePatient(String patientId, User patient) {
        return null;
    }

    @Override
    public void deletePatient(String patientId) {

    }

    @Override
    public CompletableFuture<List<User>> listAllPatients() {
        return CompletableFuture.supplyAsync(() -> {
            List<User> patients = userRepository.findAll();
            if (patients.isEmpty()) {
                LOGGER.info("No patients found.");
            } else {
                LOGGER.info("Found {} patients.", patients.size());
            }
            return patients;
        }, AppExecutors.db());
    }

    @Override
    public Room createRoom(Room room) {
        return null;
    }

    @Override
    public Room updateRoom(String roomId, Room room) {
        return null;
    }

    @Override
    public void deleteRoom(String roomId) {

    }

    @Override
    public CompletableFuture<List<Room>> listAllRooms() {
        return CompletableFuture.supplyAsync(() -> {
            List<Room> rooms = roomRepository.findAll();
            if (rooms.isEmpty()) {
                LOGGER.info("No rooms found.");
            } else {
                LOGGER.info("Found {} rooms.", rooms.size());
            }
            return rooms;
        }, AppExecutors.db());
    }

    @Override
    public void assignWeeklySchedule(String doctorId, List<Schedule> weeklySchedule) {

    }

    @Override
    public CompletableFuture<List<Specialty>> listAllSpecialties() {
        return CompletableFuture.supplyAsync(() -> {
            List<Specialty> specialties = specialtyRepository.findAll();
            if (specialties.isEmpty()) {
                LOGGER.info("No specialties found.");
            } else {
                LOGGER.info("Found {} specialties.", specialties.size());
            }
            return specialties;
        }, AppExecutors.db());
    }
}
