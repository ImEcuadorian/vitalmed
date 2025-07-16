package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.vitalmed.db.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import org.slf4j.*;

import javax.sql.*;
import java.sql.*;
import java.util.*;

public class JdbcSpecialtyRepository implements SpecialtyRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcSpecialtyRepository.class);
    private final DataSource dataSource = MySQLConnectionPool.getDataSource();

    @Override
    public Optional<Specialty> findByName(String name) {
        return findAll().stream()
                .filter(specialty -> specialty.name().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public void save(Specialty value) {
        var sql = "INSERT INTO specialties (name) VALUES (?)";
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, value.name());
            ps.executeUpdate();
            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    LOGGER.info("Specialty saved with ID: {}", generatedId);
                } else {
                    LOGGER.warn("No ID was generated for the specialty.");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error saving specialty: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<Specialty> findById(Integer id) {
        return findAll().stream()
                .filter(specialty -> specialty.id().equals(id))
                .findFirst();
    }

    @Override
    public List<Specialty> findAll() {
        var sql = "SELECT * FROM specialties ORDER BY name";
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            List<Specialty> specialties = new ArrayList<>();
            while (rs.next()) {
                Specialty specialty = new Specialty(rs.getInt("id"), rs.getString("name"));
                specialties.add(specialty);
            }
            return specialties;
        } catch (SQLException e) {
            LOGGER.error("Error fetching specialties: {}", e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public void update(Integer id, Specialty value) {
        var sql = "UPDATE specialties SET name = ? WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, value.name());
            ps.setInt(2, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Specialty with ID {} updated successfully.", id);
            } else {
                LOGGER.warn("No specialty found with ID {} to update.", id);
            }
        } catch (SQLException e) {
            LOGGER.error("Error updating specialty: {}", e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        var sql = "DELETE FROM specialties WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Specialty with ID {} deleted successfully.", id);
            } else {
                LOGGER.warn("No specialty found with ID {} to delete.", id);
            }
        } catch (SQLException e) {
            LOGGER.error("Error deleting specialty: {}", e.getMessage(), e);
        }
    }
}
