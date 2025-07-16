package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.vitalmed.db.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import org.slf4j.*;

import javax.sql.*;
import java.sql.*;
import java.util.*;

public class JdbcRoomRepository implements RoomRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcRoomRepository.class);
    private final DataSource dataSource = MySQLConnectionPool.getDataSource();

    @Override
    public List<Room> findBySpeciality(String speciality) {
        var sql = """
                SELECT r.id, r.code, r.number, s.id, s.name AS speciality
                FROM rooms r
                INNER JOIN specialties s ON r.specialty_id = s.id
                WHERE s.name = ?
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, speciality);
            try (var rs = ps.executeQuery()) {
                List<Room> rooms = new ArrayList<>();
                while (rs.next()) {
                    Room room = new Room(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getInt("number"),
                            new Specialty(rs.getInt("id"), rs.getString("speciality"))
                    );
                    rooms.add(room);
                }
                return rooms;
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding rooms by speciality: {}", e.getMessage(), e);
        }

        return Collections.emptyList();
    }

    @Override
    public Optional<Room> findByNumber(int number) {
        var sql = """
                SELECT r.id, r.code, r.number, s.id, s.name AS speciality
                FROM rooms r
                INNER JOIN specialties s ON r.specialty_id = s.id
                WHERE r.number = ?
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, number);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getInt("number"),
                            new Specialty(rs.getInt("id"), rs.getString("speciality"))
                    );
                    return Optional.of(room);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding rooms by number: {}", e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Room value) {
        var sql = """
                INSERT INTO rooms (code, number, specialty_id)
                VALUES (?, ?, ?)
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, value.code());
            ps.setInt(2, value.number());
            ps.setInt(3, value.specialty().id());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (var generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        value = value.withId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error saving room: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<Room> findById(String id) {
        var sql = """
                SELECT r.id, r.code, r.number, s.id, s.name AS speciality
                FROM rooms r
                INNER JOIN specialties s ON r.specialty_id = s.id
                WHERE r.id = ?
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getInt("number"),
                            new Specialty(rs.getInt("id"), rs.getString("speciality"))
                    );
                    return Optional.of(room);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding room by ID: {}", e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Room> findAll() {
        var sql = """
                SELECT r.id, r.code, r.number, s.id, s.name AS speciality
                FROM rooms r
                INNER JOIN specialties s ON r.specialty_id = s.id
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            List<Room> rooms = new ArrayList<>();
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getInt("number"),
                        new Specialty(rs.getInt("id"), rs.getString("speciality"))
                );
                rooms.add(room);
            }
            return rooms;
        } catch (SQLException e) {
            LOGGER.error("Error finding all rooms: {}", e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public void update(String id, Room value) {
        var sql = """
                UPDATE rooms
                SET code = ?, number = ?, specialty_id = ?
                WHERE id = ?
                """;
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
                ps.setString(1, value.code());
                ps.setInt(2, value.number());
                ps.setInt(3, value.specialty().id());
                ps.setString(4, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                LOGGER.error("Error updating room: {}", e.getMessage(), e);
            }
    }

    @Override
    public void delete(String id) {
        var sql = """
                DELETE FROM rooms
                WHERE id = ?
                """;
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                LOGGER.error("Error deleting room: {}", e.getMessage(), e);
            }

    }
}



