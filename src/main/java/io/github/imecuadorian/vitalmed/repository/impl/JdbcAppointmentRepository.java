package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.vitalmed.db.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import org.slf4j.*;

import javax.sql.*;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;

public class JdbcAppointmentRepository implements AppointmentRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcAppointmentRepository.class);
    private final DataSource ds = MySQLConnectionPool.getDataSource();

    @Override
    public void save(Appointment a) {
        String sql = """
                INSERT INTO appointments
                  (patient_id, doctor_id, slot_id, appointment_date, status)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (var conn = ds.getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.patientId());
            ps.setInt(2, a.doctorId());
            ps.setInt(3, a.slot().id());
            ps.setDate(4, Date.valueOf(a.appointmentDate()));
            ps.setString(5, a.status().name());
            ps.executeUpdate();
            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) a = a.withId(rs.getInt(1));
            }
        } catch (SQLException e) {
            LOGGER.error("Error saving appointment: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<Appointment> findById(String id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        try (var conn = ds.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding appointment by ID: {}", e.getMessage(), e);
            return Optional.empty();
        } finally {
            LOGGER.info("Appointment search completed for ID: {}", id);
        }
    }

    @Override
    public List<Appointment> findAll() {
        String sql = "SELECT * FROM appointments";
        var list = new ArrayList<Appointment>();
        try (var conn = ds.getConnection();
             var st = conn.createStatement();
             var rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            LOGGER.error("Error retrieving all appointments: {}", e.getMessage(), e);
        } finally {
            LOGGER.info("Retrieved {} appointments", list.size());
        }
        return list;
    }

    @Override
    public void update(String id, Appointment a) {
        String sql = """
                UPDATE appointments
                   SET patient_id=?, doctor_id=?, slot_id=?, appointment_date=?, status=?
                 WHERE id=?
                """;
        try (var conn = ds.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.patientId());
            ps.setInt(2, a.doctorId());
            ps.setInt(3, a.slot().id());
            ps.setDate(4, Date.valueOf(a.appointmentDate()));
            ps.setString(5, a.status().name());
            ps.setInt(6, Integer.parseInt(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error updating appointment: {}", e.getMessage(), e);
        } finally {
            LOGGER.info("Appointment with ID {} updated successfully", id);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM appointments WHERE id=?";
        try (var conn = ds.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error deleting appointment: {}", e.getMessage(), e);
        } finally {
            LOGGER.info("Appointment with ID {} deleted successfully", id);
        }
    }

    @Override
    public List<Appointment> findByPatient(Integer patientId) {
        String sql = "SELECT * FROM appointments WHERE patient_id = ?";
        var list = new ArrayList<Appointment>();
        try (var conn = ds.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            LOGGER.error("Error finding appointments by patient ID {}: {}", patientId, e.getMessage(), e);
        } finally {
            LOGGER.info("Found {} appointments for patient ID {}", list.size(), patientId);
        }
        return list;
    }

    @Override
    public List<Appointment> findByDoctor(Integer doctorId) {
        String sql = "SELECT * FROM appointments WHERE doctor_id = ?";
        var list = new ArrayList<Appointment>();
        try (var conn = ds.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            LOGGER.error("Error finding appointments by doctor ID {}: {}", doctorId, e.getMessage(), e);
        } finally {
            LOGGER.info("Found {} appointments for doctor ID {}", list.size(), doctorId);
        }
        return list;
    }

    @Override
    public List<Appointment> findByDate(LocalDate date) {
        String sql = "SELECT * FROM appointments WHERE appointment_date = ?";
        var list = new ArrayList<Appointment>();
        try (var conn = ds.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (var rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            LOGGER.error("Error finding appointments by date {}: {}", date, e.getMessage(), e);
        } finally {
            LOGGER.info("Found {} appointments for date {}", list.size(), date);
        }
        return list;
    }

    @Override
    public List<Appointment> findByDoctorAndWeek(Integer doctorId, LocalDate weekStart) {
        String sql = """
                SELECT * FROM appointments
                 WHERE doctor_id = ?
                   AND appointment_date BETWEEN ? AND ?
                """;
        var list = new ArrayList<Appointment>();
        LocalDate weekEnd = weekStart.plusDays(6);
        try (var conn = ds.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, Date.valueOf(weekStart));
            ps.setDate(3, Date.valueOf(weekEnd));
            try (var rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            LOGGER.error("Error finding appointments for doctor ID {} in week starting {}: {}", doctorId, weekStart, e.getMessage(), e);
        } finally {
            LOGGER.info("Found {} appointments for doctor ID {} in week starting {}", list.size(), doctorId, weekStart);
        }
        return list;
    }

    @Override
    public List<Appointment> findByStatusAndDoctor(AppointmentStatus status, Integer doctorId) {
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? AND status = ?";
        var list = new ArrayList<Appointment>();
        try (var conn = ds.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setString(2, status.name());
            try (var rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            LOGGER.error("Error finding appointments by status {} and doctor ID {}: {}", status, doctorId, e.getMessage(), e);
        } finally {
            LOGGER.info("Found {} appointments with status {} for doctor ID {}", list.size(), status, doctorId);
        }
        return list;
    }

    private Appointment mapRow(ResultSet rs) throws SQLException {
        return Appointment.builder()
                .id(rs.getInt("id"))
                .patientId(rs.getInt("patient_id"))
                .doctorId(rs.getInt("doctor_id"))
                .slot(new AppointmentSlot(
                        rs.getInt("slot_id"),
                        null,
                        null,
                        false
                ))
                .appointmentDate(rs.getDate("appointment_date").toLocalDate())
                .status(AppointmentStatus.valueOf(rs.getString("status")))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .build();
    }
}
