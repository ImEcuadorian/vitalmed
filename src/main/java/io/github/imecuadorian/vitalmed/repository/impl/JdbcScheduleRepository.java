package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.vitalmed.db.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import org.jetbrains.annotations.*;
import org.slf4j.*;

import javax.sql.*;
import java.sql.*;
import java.time.*;
import java.util.*;

public class JdbcScheduleRepository implements ScheduleRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcScheduleRepository.class);
    private final DataSource dataSource = MySQLConnectionPool.getDataSource();

    @Override
    public List<Schedule> findByDoctorId(String doctorId) {
        var sql = """
                SELECT sc.id, sc.doctor_id, sc.room_id, sc.day_of_week, sc.shift_number, sc.start_time, sc.end_time, 
                       r.id AS room_id, r.code AS room_code, r.number AS room_number,
                       s.id AS specialty_id, s.name AS specialty_name,
                          u.id AS user_id, u.cedula, u.fullname, u.email, u.phone, u.cell, u.address, u.role
                FROM schedules sc
                JOIN doctors d ON sc.doctor_id = d.user_id
                JOIN rooms r ON sc.room_id = r.id
                JOIN vitalmed.users u on u.id = sc.doctor_id
                JOIN specialties s ON d.specialty_id = s.id
                WHERE doctor_id = ? AND u.role = 'DOCTOR'
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            return getSchedules(doctorId, ps);
        } catch (Exception e) {
            LOGGER.error("Error fetching schedules for doctor {}: {}", doctorId, e.getMessage(), e);
        }
        return List.of();
    }

    @NotNull
    private List<Schedule> getSchedules(String doctorId, PreparedStatement ps) throws SQLException {
        ps.setString(1, doctorId);
        return getSchedules(ps);
    }

    @NotNull
    private List<Schedule> getSchedules(PreparedStatement ps) throws SQLException {
        try (var rs = ps.executeQuery()) {
            List<Schedule> schedules = new ArrayList<>();
            while (rs.next()) {
                User user = User.builder()
                        .id(rs.getInt("user_id"))
                        .cedula(rs.getString("cedula"))
                        .fullName(rs.getString("fullname"))
                        .email(rs.getString("email"))
                        .phone(rs.getString("phone"))
                        .cell(rs.getString("cell"))
                        .address(rs.getString("address"))
                        .role(Role.valueOf(rs.getString("role")))
                        .build();
                Room room = Room.builder()
                        .id(rs.getInt("room_id"))
                        .code(rs.getString("code"))
                        .number(rs.getInt("number"))
                        .build();
                Specialty specialty = Specialty.builder()
                        .id(rs.getInt("specialty_id"))
                        .name(rs.getString("specialty_name"))
                        .build();
                Schedule schedule = Schedule.builder()
                        .id(rs.getInt("id"))
                        .doctor(new Doctor(user, specialty))
                        .dayOfWeek(DayOfWeek.valueOf(rs.getString("day_of_week")))
                        .shiftNumber(rs.getInt("shift_number"))
                        .startTime(rs.getTime("start_time").toLocalTime())
                        .endTime(rs.getTime("end_time").toLocalTime())
                        .room(room)
                        .build();
                schedules.add(schedule);
            }
            return schedules;
        }
    }

    @Override
    public List<Schedule> findByDay(DayOfWeek day) {
        var sql = """
                SELECT sc.id, sc.doctor_id, sc.room_id, sc.day_of_week, sc.shift_number, sc.start_time, sc.end_time, 
                       r.id AS room_id, r.code AS room_code, r.number AS room_number,
                       s.id AS specialty_id, s.name AS specialty_name,
                          u.id AS user_id, u.cedula, u.fullname, u.email, u.phone, u.cell, u.address, u.role
                FROM schedules sc
                JOIN doctors d ON sc.doctor_id = d.user_id
                JOIN rooms r ON sc.room_id = r.id
                JOIN vitalmed.users u on u.id = sc.doctor_id
                JOIN specialties s ON d.specialty_id = s.id
                WHERE day_of_week = ? AND u.role = 'DOCTOR'
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, day.name());
            return getSchedules(ps);
        } catch (Exception e) {
            LOGGER.error("Error fetching schedules for day {}: {}", day, e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public List<Schedule> findByRoomId(String roomId) {
        var sql = """
                SELECT sc.id, sc.doctor_id, sc.room_id, sc.day_of_week, sc.shift_number, sc.start_time, sc.end_time, 
                       r.id AS room_id, r.code AS room_code, r.number AS room_number,
                       s.id AS specialty_id, s.name AS specialty_name,
                          u.id AS user_id, u.cedula, u.fullname, u.email, u.phone, u.cell, u.address, u.role
                FROM schedules sc
                JOIN doctors d ON sc.doctor_id = d.user_id
                JOIN rooms r ON sc.room_id = r.id
                JOIN vitalmed.users u on u.id = sc.doctor_id
                JOIN specialties s ON d.specialty_id = s.id
                WHERE room_id = ? AND u.role = 'DOCTOR'
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            return getSchedules(roomId, ps);
        } catch (Exception e) {
            LOGGER.error("Error fetching schedules for room {}: {}", roomId, e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public List<Schedule> findByDoctorAndWeek(Integer doctorId, LocalDate weekStart) {
        var sql = """
                SELECT sc.id, sc.doctor_id, sc.room_id, sc.day_of_week, sc.shift_number, sc.start_time, sc.end_time, 
                       r.id AS room_id, r.code AS room_code, r.number AS room_number,
                       s.id AS specialty_id, s.name AS specialty_name,
                          u.id AS user_id, u.cedula, u.fullname, u.email, u.phone, u.cell, u.address, u.role
                FROM schedules sc
                JOIN doctors d ON sc.doctor_id = d.user_id
                JOIN rooms r ON sc.room_id = r.id
                JOIN vitalmed.users u on u.id = sc.doctor_id
                JOIN specialties s ON d.specialty_id = s.id
                WHERE doctor_id = ? AND (sc.start_time BETWEEN ? AND ?)
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            LocalDate weekEnd = weekStart.plusDays(6);
            ps.setTime(2, java.sql.Time.valueOf(weekStart.atStartOfDay().toLocalTime()));
            ps.setTime(3, java.sql.Time.valueOf(weekEnd.atStartOfDay().toLocalTime()));
            return getSchedules(ps);
        } catch (Exception e) {
            LOGGER.error("Error fetching schedules for doctor {} in week starting {}: {}", doctorId, weekStart, e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public List<Schedule> findByDayOfWeekAndRoom(DayOfWeek day, Integer roomId) {
        var sql = """
                SELECT sc.id, sc.doctor_id, sc.room_id, sc.day_of_week, sc.shift_number, sc.start_time, sc.end_time, 
                       r.id AS room_id, r.code AS room_code, r.number AS room_number,
                       s.id AS specialty_id, s.name AS specialty_name,
                          u.id AS user_id, u.cedula, u.fullname, u.email, u.phone, u.cell, u.address, u.role
                FROM schedules sc
                JOIN doctors d ON sc.doctor_id = d.user_id
                JOIN rooms r ON sc.room_id = r.id
                JOIN vitalmed.users u on u.id = sc.doctor_id
                JOIN specialties s ON d.specialty_id = s.id
                WHERE day_of_week = ? AND room_id = ? AND u.role = 'DOCTOR'
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, day.name());
            ps.setInt(2, roomId);
            return getSchedules(ps);
        } catch (Exception e) {
            LOGGER.error("Error fetching schedules for day {} and room {}: {}", day, roomId, e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public void save(Schedule value) {
        var sql = """
                INSERT INTO schedules (doctor_id, room_id, day_of_week, shift_number, start_time, end_time)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, value.doctor().id());
            ps.setInt(2, value.room().id());
            ps.setString(3, value.dayOfWeek().name());
            ps.setInt(4, value.shiftNumber());
            ps.setTime(5, java.sql.Time.valueOf(value.startTime()));
            ps.setTime(6, java.sql.Time.valueOf(value.endTime()));
            ps.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Error saving schedule: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<Schedule> findById(String id) {
        var sql = """
                SELECT sc.id, sc.doctor_id, sc.room_id, sc.day_of_week, sc.shift_number, sc.start_time, sc.end_time, 
                       r.id AS room_id, r.code AS room_code, r.number AS room_number,
                       s.id AS specialty_id, s.name AS specialty_name,
                          u.id AS user_id, u.cedula, u.fullname, u.email, u.phone, u.cell, u.address, u.role
                FROM schedules sc
                JOIN doctors d ON sc.doctor_id = d.user_id
                JOIN rooms r ON sc.room_id = r.id
                JOIN vitalmed.users u on u.id = sc.doctor_id
                JOIN specialties s ON d.specialty_id = s.id
                WHERE sc.id = ? AND u.role = 'DOCTOR'
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = User.builder()
                            .id(rs.getInt("user_id"))
                            .cedula(rs.getString("cedula"))
                            .fullName(rs.getString("fullname"))
                            .email(rs.getString("email"))
                            .phone(rs.getString("phone"))
                            .cell(rs.getString("cell"))
                            .address(rs.getString("address"))
                            .role(Role.valueOf(rs.getString("role")))
                            .build();
                    Room room = Room.builder()
                            .id(rs.getInt("room_id"))
                            .code(rs.getString("room_code"))
                            .number(rs.getInt("room_number"))
                            .build();
                    Specialty specialty = Specialty.builder()
                            .id(rs.getInt("specialty_id"))
                            .name(rs.getString("specialty_name"))
                            .build();
                    Schedule schedule = Schedule.builder()
                            .id(rs.getInt("id"))
                            .doctor(new Doctor(user, specialty))
                            .dayOfWeek(DayOfWeek.valueOf(rs.getString("day_of_week")))
                            .shiftNumber(rs.getInt("shift_number"))
                            .startTime(rs.getTime("start_time").toLocalTime())
                            .endTime(rs.getTime("end_time").toLocalTime())
                            .room(room)
                            .build();
                    return Optional.of(schedule);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error finding schedule by ID {}: {}", id, e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Schedule> findAll() {
        var sql = """
                SELECT sc.id, sc.doctor_id, sc.room_id, sc.day_of_week, sc.shift_number, sc.start_time, sc.end_time, 
                       r.id AS room_id, r.code AS room_code, r.number AS room_number,
                       s.id AS specialty_id, s.name AS specialty_name,
                          u.id AS user_id, u.cedula, u.fullname, u.email, u.phone, u.cell, u.address, u.role
                FROM schedules sc
                JOIN doctors d ON sc.doctor_id = d.user_id
                JOIN rooms r ON sc.room_id = r.id
                JOIN vitalmed.users u on u.id = sc.doctor_id
                JOIN specialties s ON d.specialty_id = s.id
                WHERE u.role = 'DOCTOR'
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            List<Schedule> schedules = new ArrayList<>();
            while (rs.next()) {
                User user = User.builder()
                        .id(rs.getInt("user_id"))
                        .cedula(rs.getString("cedula"))
                        .fullName(rs.getString("fullname"))
                        .email(rs.getString("email"))
                        .phone(rs.getString("phone"))
                        .cell(rs.getString("cell"))
                        .address(rs.getString("address"))
                        .role(Role.valueOf(rs.getString("role")))
                        .build();
                Room room = Room.builder()
                        .id(rs.getInt("room_id"))
                        .code(rs.getString("room_code"))
                        .number(rs.getInt("room_number"))
                        .build();
                Specialty specialty = Specialty.builder()
                        .id(rs.getInt("specialty_id"))
                        .name(rs.getString("specialty_name"))
                        .build();
                Schedule schedule = Schedule.builder()
                        .id(rs.getInt("id"))
                        .doctor(new Doctor(user, specialty))
                        .dayOfWeek(DayOfWeek.valueOf(rs.getString("day_of_week")))
                        .shiftNumber(rs.getInt("shift_number"))
                        .startTime(rs.getTime("start_time").toLocalTime())
                        .endTime(rs.getTime("end_time").toLocalTime())
                        .room(room)
                        .build();
                schedules.add(schedule);
            }
            return schedules;
        } catch (Exception e) {
            LOGGER.error("Error fetching all schedules: {}", e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public void update(String id, Schedule value) {
        var sql = """
                UPDATE schedules
                SET doctor_id = ?, room_id = ?, day_of_week = ?, shift_number = ?, start_time = ?, end_time = ?
                WHERE id = ?
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, value.doctor().id());
            ps.setInt(2, value.room().id());
            ps.setString(3, value.dayOfWeek().name());
            ps.setInt(4, value.shiftNumber());
            ps.setTime(5, java.sql.Time.valueOf(value.startTime()));
            ps.setTime(6, java.sql.Time.valueOf(value.endTime()));
            ps.setInt(7, Integer.parseInt(id));
            ps.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Error updating schedule with ID {}: {}", id, e.getMessage(), e);
        }
        LOGGER.info("Schedule with ID {} updated successfully", id);
    }

    @Override
    public void delete(String id) {
        var sql = "DELETE FROM schedules WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Error deleting schedule with ID {}: {}", id, e.getMessage(), e);
        }
    }
}
