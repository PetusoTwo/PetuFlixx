package com.example.petuflixx.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDatabase {
    public static void main(String[] args) {
        try {
            System.out.println("=== Iniciando prueba de base de datos ===");
            
            // 1. Probar conexión
            System.out.println("\n1. Probando conexión...");
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("¡Conexión exitosa!");
            
            // 2. Verificar si la tabla existe
            System.out.println("\n2. Verificando si la tabla usuarioss existe...");
            String checkTableSQL = "SHOW TABLES LIKE 'usuarioss'";
            try (PreparedStatement pstmt = conn.prepareStatement(checkTableSQL)) {
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println("La tabla usuarioss existe");
                } else {
                    System.out.println("La tabla usuarioss NO existe");
                    return;
                }
            }
            
            // 3. Intentar insertar un usuario de prueba
            System.out.println("\n3. Intentando insertar usuario de prueba...");
            String insertSQL = "INSERT INTO usuarioss (nombre, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, "Usuario Prueba");
                pstmt.setString(2, "prueba@test.com");
                pstmt.setString(3, "password123");
                
                int affectedRows = pstmt.executeUpdate();
                System.out.println("Filas afectadas: " + affectedRows);
                
                if (affectedRows > 0) {
                    System.out.println("¡Usuario de prueba insertado exitosamente!");
                }
            }
            
            // 4. Verificar si el usuario se insertó
            System.out.println("\n4. Verificando si el usuario se insertó...");
            String selectSQL = "SELECT * FROM usuarioss WHERE email = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                pstmt.setString(1, "prueba@test.com");
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    System.out.println("Usuario encontrado:");
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Nombre: " + rs.getString("nombre"));
                    System.out.println("Email: " + rs.getString("email"));
                } else {
                    System.out.println("No se encontró el usuario");
                }
            }
            
            // Cerrar la conexión
            DatabaseConnection.closeConnection();
            System.out.println("\nConexión cerrada correctamente");
            
        } catch (SQLException e) {
            System.out.println("\n=== ERROR ===");
            System.out.println("Mensaje de error: " + e.getMessage());
            System.out.println("Código de error: " + e.getErrorCode());
            System.out.println("Estado SQL: " + e.getSQLState());
            e.printStackTrace();
        }
    }
} 