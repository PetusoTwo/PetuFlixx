package com.example.petuflixx.database;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            System.out.println("Intentando conectar a la base de datos...");
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("¡Conexión exitosa!");
            
            // Cerrar la conexión
            DatabaseConnection.closeConnection();
            System.out.println("Conexión cerrada correctamente.");
            
        } catch (Exception e) {
            System.out.println("Error al conectar a la base de datos:");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
} 