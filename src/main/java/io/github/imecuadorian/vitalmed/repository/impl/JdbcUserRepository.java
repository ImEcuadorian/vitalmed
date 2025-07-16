package io.github.imecuadorian.vitalmed.repository.impl;

import io.github.imecuadorian.vitalmed.db.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import org.slf4j.*;

import javax.sql.*;
import java.sql.*;
import java.util.*;

public class JdbcUserRepository implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcUserRepository.class);
    private final DataSource dataSource = MySQLConnectionPool.getDataSource();

    @Override
    public void save(User u) {
        var sql = """
                INSERT INTO users
                  (cedula, fullname, email, password_hash, phone, cell, address, role)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.cedula());
            ps.setString(2, u.fullName());
            ps.setString(3, u.email());
            ps.setString(4, u.passwordHash());
            ps.setString(5, u.phone());
            ps.setString(6, u.cell());
            ps.setString(7, u.address());
            ps.setString(8, u.role().name());
            ps.executeUpdate();
            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    u = u.withId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving user: {}", e.getMessage(), e);
        } finally {
            logger.info("User saved: {}", u.fullName());
        }
    }

    @Override
    public Optional<User> findById(String id) {
        var sql = "SELECT * FROM users WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error finding user by ID: {}", e.getMessage(), e);
        } finally {
            logger.info("User search completed for ID: {}", id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByCedula(String cedula) {
        var sql = "SELECT * FROM users WHERE cedula = ?";
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error finding user by cedula: {}", e.getMessage(), e);
        } finally {
            logger.info("User search completed for cedula: {}", cedula);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        var sql = "SELECT * FROM users WHERE email = ?";
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error finding user by email: {}", e.getMessage(), e);
        } finally {
            logger.info("User search completed for email: {}", email);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByCellphone(String cellphone) {
        var sql = "SELECT * FROM users WHERE cell = ?";
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, cellphone);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error finding user by cellphone: {}", e.getMessage(), e);
        } finally {
            logger.info("User search completed for cellphone: {}", cellphone);
        }
        return Optional.empty();
    }

    @Override
    public void updatePassword(String userId, String hash) {
        var sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, hash);
            ps.setInt(2, Integer.parseInt(userId));
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating user password: {}", e.getMessage(), e);
        } finally {
            logger.info("Password updated for user ID: {}", userId);
        }
    }

    @Override
    public List<User> findAll() {
        var sql = "SELECT * FROM users WHERE role = 'PATIENT'";
        var list = new ArrayList<User>();
        try (var conn = dataSource.getConnection();
             var st = conn.createStatement();
             var rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            logger.error("Error finding all users: {}", e.getMessage(), e);
        } finally {
            logger.info("All users search completed.");
        }
        return Collections.emptyList();
    }

    @Override
    public void update(String id, User u) {
        var sql = """
                UPDATE users
                   SET phone=?, cell=?, address=?
                 WHERE id=?
                """;
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(4, u.phone());
            ps.setString(5, u.cell());
            ps.setString(6, u.address());
            ps.setInt(8, Integer.parseInt(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating user: {}", e.getMessage(), e);
        } finally {
            logger.info("User updated: {}", u.fullName());
        }
    }

    @Override
    public void delete(String id) {
        var sql = "DELETE FROM users WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting user: {}", e.getMessage(), e);
        } finally {
            logger.info("User with ID {} deleted.", id);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .cedula(rs.getString("cedula"))
                .fullName(rs.getString("fullname"))
                .email(rs.getString("email"))
                .passwordHash(rs.getString("password_hash"))
                .phone(rs.getString("phone"))
                .cell(rs.getString("cell"))
                .address(rs.getString("address"))
                .role(Role.valueOf(rs.getString("role")))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .updatedAt(rs.getTimestamp("updated_at").toInstant())
                .build();
    }
}
