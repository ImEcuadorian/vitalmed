package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.vitalmed.db.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import org.slf4j.*;

import javax.sql.*;
import java.util.*;

public class JdbcHistoryRepository implements HistoryRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcHistoryRepository.class);
    private final DataSource dataSource = MySQLConnectionPool.getDataSource();

    @Override
    public List<ClinicalHistory> findByPatientId(String patientId) {

        var sql = """
                SELECT * FROM clinical_history
                WHERE patient_id = ?
                """;

        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, patientId);
            try (var rs = ps.executeQuery()) {
                List<ClinicalHistory> histories = new ArrayList<>();
                while (rs.next()) {
                    ClinicalHistory history = ClinicalHistory.builder()
                            .id(rs.getInt("id"))
                            .patientId(rs.getInt("patient_id"))
                            .recordNumber(rs.getString("record_number"))
                            .age(rs.getInt("age"))
                            .sex(Gender.valueOf(rs.getString("sex")))
                            .allergies(rs.getString("allergies"))
                            .diseases(rs.getString("diseases"))
                            .medications(rs.getString("medications"))
                            .surgeries(rs.getString("surgeries"))
                            .createdAt(rs.getTimestamp("created_at").toInstant())
                            .updatedAt(rs.getTimestamp("updated_at").toInstant())
                            .build();
                    histories.add(history);
                }
                return histories;
            }
        } catch (Exception e) {
            LOGGER.error("Error fetching clinical histories for patient {}: {}", patientId, e.getMessage(), e);
        }

        return List.of();
    }

    @Override
    public void upsert(ClinicalHistory history) {
        var sql = """
                INSERT INTO clinical_history
                (patient_id, record_number, age, sex, allergies, diseases, medications, surgeries, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                record_number = VALUES(record_number),
                age = VALUES(age)
                """;

        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
                ps.setInt(1, history.patientId());
                ps.setString(2, history.recordNumber());
                ps.setInt(3, history.age());
                ps.setString(4, history.sex().toString());
                ps.setString(5, history.allergies());
                ps.setString(6, history.diseases());
                ps.setString(7, history.medications());
                ps.setString(8, history.surgeries());
                ps.setTimestamp(9, java.sql.Timestamp.from(history.createdAt()));
                ps.setTimestamp(10, java.sql.Timestamp.from(history.updatedAt()));
                ps.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Error upserting clinical history for patient {}: {}", history.patientId(), e.getMessage(), e);
        }

    }

    @Override
    public void save(ClinicalHistory value) {
        var sql = """
                INSERT INTO clinical_history
                (patient_id, record_number, age, sex, allergies, diseases, medications, surgeries, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
                ps.setInt(1, value.patientId());
                ps.setString(2, value.recordNumber());
                ps.setInt(3, value.age());
                ps.setString(4, value.sex().toString());
                ps.setString(5, value.allergies());
                ps.setString(6, value.diseases());
                ps.setString(7, value.medications());
                ps.setString(8, value.surgeries());
                ps.setTimestamp(9, java.sql.Timestamp.from(value.createdAt()));
                ps.setTimestamp(10, java.sql.Timestamp.from(value.updatedAt()));
                ps.executeUpdate();
                LOGGER.info("Clinical history for patient {} saved successfully.", value.patientId());
        } catch (Exception e) {
            LOGGER.error("Error saving clinical history for patient {}: {}", value.patientId(), e.getMessage(), e);
        }
    }

    @Override
    public Optional<ClinicalHistory> findById(String id) {
        return findAll().stream()
                .filter(history -> history.id().toString().equals(id))
                .findFirst();
    }

    @Override
    public List<ClinicalHistory> findAll() {
        var sql = """
                SELECT * FROM clinical_history
                """;
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql);
                var rs = ps.executeQuery()) {
                List<ClinicalHistory> histories = new ArrayList<>();
                while (rs.next()) {
                    ClinicalHistory history = ClinicalHistory.builder()
                            .id(rs.getInt("id"))
                            .patientId(rs.getInt("patient_id"))
                            .recordNumber(rs.getString("record_number"))
                            .age(rs.getInt("age"))
                            .sex(Gender.valueOf(rs.getString("sex")))
                            .allergies(rs.getString("allergies"))
                            .diseases(rs.getString("diseases"))
                            .medications(rs.getString("medications"))
                            .surgeries(rs.getString("surgeries"))
                            .createdAt(rs.getTimestamp("created_at").toInstant())
                            .updatedAt(rs.getTimestamp("updated_at").toInstant())
                            .build();
                    histories.add(history);
                }
                return histories;
            } catch (Exception e) {
            LOGGER.error("Error fetching clinical histories: {}", e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public void update(String patientId, ClinicalHistory value) {
        var sql = """
                UPDATE clinical_history
                SET age
                = ?, sex = ?, allergies = ?, diseases = ?, medications = ?, surgeries = ?, updated_at = ?
                WHERE patient_id = ?
                """;
        try (var conn = dataSource.getConnection();
                var ps = conn.prepareStatement(sql)) {
                ps.setInt(1, value.age());
                ps.setString(2, value.sex().toString());
                ps.setString(3, value.allergies());
                ps.setString(4, value.diseases());
                ps.setString(5, value.medications());
                ps.setString(6, value.surgeries());
                ps.setTimestamp(7, java.sql.Timestamp.from(value.updatedAt()));
                ps.setInt(8, value.patientId());
                ps.executeUpdate();
                LOGGER.info("Clinical history for patient {} updated successfully.", value.patientId());
        } catch (Exception e) {
            LOGGER.error("Error updating clinical history for patient {}: {}", value.patientId(), e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        var sql = """
                DELETE FROM clinical_history
                WHERE id = ?
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Clinical history with ID {} deleted successfully.", id);
            } else {
                LOGGER.warn("No clinical history found with ID {}.", id);
            }
        } catch (Exception e) {
            LOGGER.error("Error deleting clinical history with ID {}: {}", id, e.getMessage(), e);
        }
    }
}
