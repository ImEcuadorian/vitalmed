package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.vitalmed.db.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import org.slf4j.*;

import javax.sql.*;
import java.sql.*;
import java.time.*;
import java.util.*;

public class JdbcDoctorRepository implements DoctorRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcDoctorRepository.class);
    private final DataSource dataSource = MySQLConnectionPool.getDataSource();

    private final ScheduleRepository       scheduleRepo;
    private final AppointmentSlotRepository slotRepo;
    private final AppointmentRepository     apptRepo;
    private final HistoryRepository historyRepo;

    public JdbcDoctorRepository(
            ScheduleRepository scheduleRepo,
            AppointmentSlotRepository slotRepo,
            AppointmentRepository apptRepo,
            HistoryRepository historyRepo
    ) {
        this.scheduleRepo  = scheduleRepo;
        this.slotRepo      = slotRepo;
        this.apptRepo      = apptRepo;
        this.historyRepo   = historyRepo;
    }

    @Override
    public void save(Doctor value) {
        var sql = """
                INSERT INTO users
                  (cedula, fullname, email, password_hash, phone, cell, address, role)
                VALUES (?, ?, ?, ?, ?, ?, ?, 'DOCTOR')
                """;
        var sqlSpecialty = """
                INSERT INTO doctors (user_id, specialty_id)
                VALUES (?, ?)
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);
            ps.setString(1, value.cedula());
            ps.setString(2, value.fullName());
            ps.setString(3, value.email());
            ps.setString(4, value.passwordHash());
            ps.setString(5, value.phone());
            ps.setString(6, value.cell());
            ps.setString(7, value.address());
            ps.executeUpdate();
            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    try (var psSpecialty = conn.prepareStatement(sqlSpecialty)) {
                        psSpecialty.setInt(1, userId);
                        psSpecialty.setInt(2, value.specialty().id());
                        psSpecialty.executeUpdate();
                        conn.commit();
                        LOGGER.info("Doctor saved successfully: {}", value.user().fullName());
                    } catch (SQLException e) {
                        conn.rollback();
                        LOGGER.error("Error saving doctor specialty: {}", e.getMessage(), e);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error saving doctor: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<Doctor> findById(String id) {
        return findAll().stream()
                .filter(doctor -> doctor.user().id().toString().equals(id))
                .findFirst();
    }

    @Override
    public List<Doctor> findAll() {
        var sql = """
                SELECT u.id, u.cedula, u.fullname, u.email, u.password_hash,
                       u.phone, u.cell, u.address, u.role, s.id AS specialty_id, s.name AS specialty_name
                FROM users AS u
                JOIN doctors AS d ON u.id = d.user_id
                JOIN specialties s on s.id = d.specialty_id
                WHERE u.role = 'DOCTOR'
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            List<Doctor> doctors = new ArrayList<>();
            while (rs.next()) {
                User user = User.builder()
                        .id(rs.getInt("id"))
                        .cedula(rs.getString("cedula"))
                        .fullName(rs.getString("fullname"))
                        .email(rs.getString("email"))
                        .passwordHash(rs.getString("password_hash"))
                        .phone(rs.getString("phone"))
                        .cell(rs.getString("cell"))
                        .address(rs.getString("address"))
                        .role(Role.valueOf(rs.getString("role")))
                        .build();
                Doctor doctor = Doctor.builder()
                        .user(user)
                        .specialty(new Specialty(
                                rs.getInt("specialty_id"),
                                rs.getString("specialty_name")
                        ))
                        .build();
                doctors.add(doctor);
            }
            return doctors;
        } catch (SQLException e) {
            LOGGER.error("Error fetching doctors: {}", e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public void update(String id, Doctor value) {
        var sql = """
                UPDATE users
                SET cedula = ?, fullname = ?, email = ?, password_hash = ?, phone = ?, cell = ?, address = ?
                WHERE id = ?
                """;
        var sqlSpecialty = """
                UPDATE doctors
                SET specialty_id = ?
                WHERE user_id = ?
                """;
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql);
                var psSpecialty = conn.prepareStatement(sqlSpecialty)) {
                conn.setAutoCommit(false);
                ps.setString(1, value.user().cedula());
                ps.setString(2, value.user().fullName());
                ps.setString(3, value.user().email());
                ps.setString(4, value.user().passwordHash());
                ps.setString(5, value.user().phone());
                ps.setString(6, value.user().cell());
                ps.setString(7, value.user().address());
                ps.setInt(8, Integer.parseInt(id));
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    psSpecialty.setInt(1, value.specialty().id());
                    psSpecialty.setInt(2, Integer.parseInt(id));
                    psSpecialty.executeUpdate();
                    conn.commit();
                    LOGGER.info("Doctor with ID {} updated successfully.", id);
                } else {
                    LOGGER.warn("No doctor found with ID {} to update.", id);
                }
            } catch (SQLException e) {
                LOGGER.error("Error updating doctor: {}", e.getMessage(), e);
            }
    }

    @Override
    public void delete(String id) {
        var sql = "DELETE FROM users WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Doctor with ID {} deleted successfully.", id);
            } else {
                LOGGER.warn("No doctor found with ID {} to delete.", id);
            }
        } catch (SQLException e) {
            LOGGER.error("Error deleting doctor: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<Schedule> findWeeklyScheduleByDoctorId(String doctorId, LocalDate weekStart) {
        return scheduleRepo.findByDoctorAndWeek(Integer.parseInt(doctorId), weekStart);
    }

    @Override
    public List<AppointmentSlot> findAvailableSlotsByDoctorAndDate(String doctorId, LocalDate date) {
        return slotRepo.findAvailableByDoctorAndDate(Integer.parseInt(doctorId), date);
    }

    @Override
    public List<Appointment> findAppointmentsForWeek(String doctorId, LocalDate weekStart) {
        return apptRepo.findByDoctorAndWeek(Integer.parseInt(doctorId), weekStart);
    }

    @Override
    public Appointment updateAppointmentStatus(String appointmentId, AppointmentStatus newStatus) {
        // 1) Leer la cita
        Appointment a = apptRepo.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appointmentId));
        // 2) Cambiar el estado
        Appointment updated = a.withStatus(newStatus);
        // 3) Persistir
        apptRepo.update(appointmentId, updated);
        return updated;
    }

    @Override
    public List<ClinicalHistory> getPatientHistory(String patientId) {
        return historyRepo.findByPatientId(patientId);
    }

    @Override
    public ClinicalHistory upsertClinicalHistory(ClinicalHistory history) {
        if (history.id() != null) {
            historyRepo.update(history.id().toString(), history);
            return history;
        } else {
            historyRepo.save(history);
            return history;
        }
    }
}
