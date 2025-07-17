package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.vitalmed.db.MySQLConnectionPool;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAppointmentSlotRepository implements AppointmentSlotRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcAppointmentSlotRepository.class);
    private final DataSource ds = MySQLConnectionPool.getDataSource();

    private final ScheduleRepository scheduleRepository;

    public JdbcAppointmentSlotRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<AppointmentSlot> findAvailableByDoctorAndDate(Integer doctorId, LocalDate date) {
        String sql = """
            SELECT s.id,
                   s.schedule_id,
                   s.slot_time,
                   s.is_available
              FROM appointment_slots s
              JOIN schedules sc ON s.schedule_id = sc.id
             WHERE sc.doctor_id   = ?
               AND sc.day_of_week  = ?
               AND s.is_available  = 1
               AND s.id NOT IN (
                   SELECT slot_id
                     FROM appointments
                    WHERE appointment_date = ?
               )
             ORDER BY s.slot_time
            """;
        List<AppointmentSlot> slots = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setString(2, date.getDayOfWeek().name());
            ps.setDate(3, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    slots.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error buscando slots disponibles para doctor {} en {}: {}",
                    doctorId, date, e.getMessage(), e);
        } finally {
            LOGGER.info("Encontrados {} slots disponibles para doctor {} en {}",
                    slots.size(), doctorId, date);
        }
        return slots;
    }

    @Override
    public void reserveSlot(Integer slotId) {
        String sql = "UPDATE appointment_slots SET is_available = 0 WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error reservando slot {}: {}", slotId, e.getMessage(), e);
        } finally {
            LOGGER.info("Slot {} marcado como reservado", slotId);
        }
    }

    @Override
    public void save(AppointmentSlot slot) {
        String sql = """
            INSERT INTO appointment_slots (schedule_id, slot_time, is_available)
            VALUES (?, ?, ?)
            """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, slot.schedule().id());
            ps.setTime(2, Time.valueOf(slot.slotTime()));
            ps.setBoolean(3, slot.isAvailable());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    // Si tu modelo tiene un método withId:
                    // slot = slot.withId(rs.getInt(1));
                    LOGGER.info("Inserted slot con ID {}", rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error guardando slot: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<AppointmentSlot> findById(String id) {
        String sql = "SELECT id, schedule_id, slot_time, is_available FROM appointment_slots WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error buscando slot por ID {}: {}", id, e.getMessage(), e);
        } finally {
            LOGGER.info("Búsqueda completada para slot ID {}", id);
        }
        return Optional.empty();
    }

    @Override
    public List<AppointmentSlot> findAll() {
        String sql = "SELECT id, schedule_id, slot_time, is_available FROM appointment_slots";
        List<AppointmentSlot> slots = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                slots.add(mapRow(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error recuperando todos los slots: {}", e.getMessage(), e);
        } finally {
            LOGGER.info("Total de slots recuperados: {}", slots.size());
        }
        return slots;
    }

    @Override
    public void update(String id, AppointmentSlot slot) {
        String sql = """
            UPDATE appointment_slots
               SET schedule_id = ?, slot_time = ?, is_available = ?
             WHERE id = ?
            """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slot.schedule().id());
            ps.setTime(2, Time.valueOf(slot.slotTime()));
            ps.setBoolean(3, slot.isAvailable());
            ps.setInt(4, Integer.parseInt(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error actualizando slot {}: {}", id, e.getMessage(), e);
        } finally {
            LOGGER.info("Slot {} actualizado", id);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM appointment_slots WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error eliminando slot {}: {}", id, e.getMessage(), e);
        } finally {
            LOGGER.info("Slot {} eliminado", id);
        }
    }

    private AppointmentSlot mapRow(ResultSet rs) throws SQLException {

        Optional<Schedule> scheduleOpt = scheduleRepository.findById(String.valueOf(rs.getInt("schedule_id")));
        if (scheduleOpt.isEmpty()) {
            LOGGER.warn("Schedule with ID {} not found for slot ID {}", rs.getInt("schedule_id"), rs.getInt("id"));
            return null;
        }
        return new AppointmentSlot(
                rs.getInt("id"),
                scheduleOpt.get(),
                rs.getTime("slot_time").toLocalTime(),
                rs.getBoolean("is_available")
        );
    }
}
