package com.example.petuflixx.database;

import com.example.petuflixx.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    public User createUser(String username, String email, String password, String name) throws SQLException {
        // Primero verificar si el usuario o email ya existen
        if (getUser(username) != null) {
            throw new SQLException("El nombre de usuario ya está en uso");
        }
        if (getUserByEmail(email) != null) {
            throw new SQLException("El email ya está registrado");
        }

        String sql = "INSERT INTO users (username, email, password, name) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, name);
            
            logger.info("Intentando crear usuario: " + username);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Error al crear usuario, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    logger.info("Usuario creado exitosamente con ID: " + id);
                    return new User(id, username, email, password, name);
                } else {
                    throw new SQLException("Error al crear usuario, no se obtuvo el ID.");
                }
            }
        } catch (SQLException e) {
            logger.severe("Error al crear usuario: " + e.getMessage());
            throw e;
        }
    }

    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
                );
            }
            return null;
        }
    }

    public User getUser(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
                );
            }
            return null;
        }
    }
} 