package com.example.petuflixx.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/petuflixx?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    
    static {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("Driver MySQL cargado correctamente");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error al cargar el driver MySQL", e);
            throw new RuntimeException("Error al cargar el driver MySQL", e);
        }
    }
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                logger.info("Estableciendo nueva conexión a la base de datos...");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                logger.info("Conexión establecida exitosamente");
            }
            return connection;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al conectar con la base de datos", e);
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                logger.info("Cerrando conexión a la base de datos...");
                connection.close();
                logger.info("Conexión cerrada exitosamente");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al cerrar la conexión", e);
        }
    }
} 