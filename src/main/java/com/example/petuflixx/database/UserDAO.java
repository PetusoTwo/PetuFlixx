package com.example.petuflixx.database;

import com.example.petuflixx.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());
    private final Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO usuarioss (nombre, email, password) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            logger.info("Preparando registro de usuario: " + user.getEmail());
            
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            
            int rowsAffected = stmt.executeUpdate();
            logger.info("Filas afectadas: " + rowsAffected);
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar usuario en la base de datos", e);
            logger.severe("Mensaje de error SQL: " + e.getMessage());
            logger.severe("Código de error SQL: " + e.getErrorCode());
            logger.severe("Estado SQL: " + e.getSQLState());
            return false;
        }
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM usuarioss WHERE email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar usuario por email", e);
        }
        return null;
    }
} 