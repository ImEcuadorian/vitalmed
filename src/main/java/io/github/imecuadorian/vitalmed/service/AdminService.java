package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.util.List;

public interface AdminService {

    Doctor createDoctor(Doctor doctor);

    Doctor updateDoctor(String doctorId, Doctor doctor);

    void deleteDoctor(String doctorId);

    List<Doctor> listAllDoctors();

    Patient createPatient(Patient patient);

    Patient updatePatient(String patientId, Patient patient);

    void deletePatient(String patientId);

    List<Patient> listAllPatients();

    List<User> listUsersByRole(Role role);

    Specialty createSpecialty(Specialty specialty);

    Specialty updateSpecialty(Integer id, Specialty specialty);

    void deleteSpecialty(Integer id);

    List<Specialty> listAllSpecialties();

    Room createRoom(Room room);

    Room updateRoom(String roomId, Room room);

    void deleteRoom(String roomId);

    List<Room> listAllRooms();

    void assignWeeklySchedule(String doctorId, List<Schedule> weeklySchedule);
}
