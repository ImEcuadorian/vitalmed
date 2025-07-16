package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.util.List;
import java.util.concurrent.*;

public interface AdminService {

    CompletableFuture<Doctor> createDoctor(Doctor doctor);

    CompletableFuture<Doctor> updateDoctor(String doctorId, Doctor doctor);

    void deleteDoctor(String doctorId);

    CompletableFuture<List<Doctor>> listAllDoctors();

    User createPatient(User patient);

    User updatePatient(String patientId, User patient);

    void deletePatient(String patientId);

    CompletableFuture<List<User>> listAllPatients();

    Room createRoom(Room room);

    Room updateRoom(String roomId, Room room);

    void deleteRoom(String roomId);

    CompletableFuture<List<Room>> listAllRooms();

    void assignWeeklySchedule(String doctorId, List<Schedule> weeklySchedule);

    CompletableFuture<List<Specialty>> listAllSpecialties();
}
