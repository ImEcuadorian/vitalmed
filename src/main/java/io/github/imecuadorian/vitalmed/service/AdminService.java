package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.util.List;

public interface AdminService {
    boolean registerDoctor(Doctor doctor);
    boolean assignSchedules(String doctorId, List<Schedule> schedules);
    boolean resetPatientPassword(String patientId, String newPassword);
    List<Patient> getAllPatients();

    List<Doctor> getAllDoctors();
    boolean registerRoom(Room room);
    boolean updateRoom(String roomId, Room updatedRoom);
    boolean deleteRoom(String roomId);
    List<Room> getAllRooms();
}
