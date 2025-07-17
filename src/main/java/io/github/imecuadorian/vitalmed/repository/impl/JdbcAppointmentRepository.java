package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.vitalmed.db.MySQLConnectionPool;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAppointmentRepository implements AppointmentRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcAppointmentRepository.class);
    private static final String BASE_SELECT = """
        SELECT 
          a.id,
          a.appointment_date,
          a.status,
          a.created_at,

          p.id            AS patient_id,
          p.fullname      AS patient_name,
          p.email         AS patient_email,

          d.id            AS doctor_user_id,
          d.fullname      AS doctor_name,
          d.email         AS doctor_email,
          doc.specialty_id       AS doctor_specialty_id,
          sp.name               AS specialty_name,

          s.id            AS slot_id,
          s.schedule_id   AS schedule_id,
          s.slot_time,
          s.is_available
        FROM appointments a
        JOIN users p      ON a.patient_id = p.id
        JOIN doctors doc  ON a.doctor_id  = doc.user_id
        JOIN users d      ON doc.user_id  = d.id
        JOIN specialties sp ON doc.specialty_id = sp.id
        JOIN appointment_slots s ON a.slot_id = s.id
        """;

    private final DataSource ds = MySQLConnectionPool.getDataSource();
    private final UserRepository      userRepo;
    private final SpecialtyRepository specialtyRepo;

    private final ScheduleRepository scheduleRepo;

    public JdbcAppointmentRepository(UserRepository userRepo,
                                     SpecialtyRepository specialtyRepo, ScheduleRepository scheduleRepo) {
        this.userRepo      = userRepo;
        this.specialtyRepo = specialtyRepo;

        this.scheduleRepo = scheduleRepo;
    }

    @Override
    public void save(Appointment a) {
        String sql = """
            INSERT INTO appointments
              (patient_id, doctor_id, slot_id, appointment_date, status)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.patient().id());
            ps.setInt(2, a.doctor().id());
            ps.setInt(3, a.slot().id());
            ps.setDate(4, Date.valueOf(a.appointmentDate()));
            ps.setString(5, a.status().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    a = a.withId(rs.getInt(1));
                    LOGGER.info("Appointment saved with ID {}", a.id());
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error saving appointment: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<Appointment> findById(String id) {
        String sql = BASE_SELECT + " WHERE a.id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding appointment by ID {}: {}", id, e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Appointment> findAll() {
        String sql = BASE_SELECT + " ORDER BY a.appointment_date, a.id";
        return queryList(sql, ps -> { /* no parameters */ });
    }

    @Override
    public void update(String id, Appointment a) {
        String sql = """
            UPDATE appointments
               SET patient_id=?, doctor_id=?, slot_id=?, appointment_date=?, status=?
             WHERE id=?
            """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.patient().id());
            ps.setInt(2, a.doctor().id());
            ps.setInt(3, a.slot().id());
            ps.setDate(4, Date.valueOf(a.appointmentDate()));
            ps.setString(5, a.status().name());
            ps.setInt(6, Integer.parseInt(id));
            ps.executeUpdate();
            LOGGER.info("Appointment {} updated", id);
        } catch (SQLException e) {
            LOGGER.error("Error updating appointment {}: {}", id, e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
            LOGGER.info("Appointment {} deleted", id);
        } catch (SQLException e) {
            LOGGER.error("Error deleting appointment {}: {}", id, e.getMessage(), e);
        }
    }

    @Override
    public List<Appointment> findByPatient(Integer patientId) {
        String sql = BASE_SELECT + " WHERE a.patient_id = ? ORDER BY a.appointment_date";
        return queryList(sql, ps -> ps.setInt(1, patientId));
    }

    @Override
    public List<Appointment> findByDoctor(Integer doctorId) {
        String sql = BASE_SELECT + " WHERE a.doctor_id = ? ORDER BY a.appointment_date";
        return queryList(sql, ps -> ps.setInt(1, doctorId));
    }

    @Override
    public List<Appointment> findByDate(LocalDate date) {
        String sql = BASE_SELECT + " WHERE a.appointment_date = ? ORDER BY a.id";
        return queryList(sql, ps -> ps.setDate(1, Date.valueOf(date)));
    }

    @Override
    public List<Appointment> findByDoctorAndWeek(Integer doctorId, LocalDate weekStart) {
        String sql = BASE_SELECT + " WHERE a.doctor_id = ? AND a.appointment_date BETWEEN ? AND ? ORDER BY a.appointment_date";
        LocalDate weekEnd = weekStart.plusDays(6);
        return queryList(sql, ps -> {
            ps.setInt(1, doctorId);
            ps.setDate(2, Date.valueOf(weekStart));
            ps.setDate(3, Date.valueOf(weekEnd));
        });
    }

    @Override
    public List<Appointment> findByStatusAndDoctor(AppointmentStatus status, Integer doctorId) {
        String sql = BASE_SELECT + " WHERE a.doctor_id = ? AND a.status = ? ORDER BY a.appointment_date";
        return queryList(sql, ps -> {
            ps.setInt(1, doctorId);
            ps.setString(2, status.name());
        });
    }

    // Helper to execute a query that returns multiple Appointment rows
    private List<Appointment> queryList(String sql, ThrowingConsumer<PreparedStatement> paramSetter) {
        List<Appointment> list = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            paramSetter.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Query error: {}", e.getMessage(), e);
        }
        return list;
    }

    // Map a single row (with all JOINed columns) into an Appointment
    private Appointment mapRow(ResultSet rs) throws SQLException {
        // paciente
        User patient = User.builder()
                .id(rs.getInt("patient_id"))
                .fullName(rs.getString("patient_name"))
                .email(rs.getString("patient_email"))
                .build();

        // doctor usuario
        User doctorUser = User.builder()
                .id(rs.getInt("doctor_user_id"))
                .fullName(rs.getString("doctor_name"))
                .email(rs.getString("doctor_email"))
                .build();

        // doctor especialidad
        Specialty specialty = new Specialty(
                rs.getInt("doctor_specialty_id"),
                rs.getString("specialty_name")
        );
        Doctor doctor = new Doctor(doctorUser, specialty);

        Optional<Schedule> scheduleOpt = scheduleRepo.findById(String.valueOf(rs.getInt("schedule_id")));
        AppointmentSlot slot = new AppointmentSlot(
                rs.getInt("slot_id"),
                scheduleOpt.get(),
                rs.getTime("slot_time").toLocalTime(),
                rs.getBoolean("is_available")
        );

        return Appointment.builder()
                .id(rs.getInt("id"))
                .patient(patient)
                .doctor(doctor)
                .slot(slot)
                .appointmentDate(rs.getDate("appointment_date").toLocalDate())
                .status(AppointmentStatus.valueOf(rs.getString("status")))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .build();
    }

    /** Functional interface to allow lambdas that throw SQLException */
    @FunctionalInterface
    private interface ThrowingConsumer<T> {
        void accept(T t) throws SQLException;
    }
}
