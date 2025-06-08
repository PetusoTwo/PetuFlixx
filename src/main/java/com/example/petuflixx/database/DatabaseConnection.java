package com.example.petuflixx.database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.io.InputStream;

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
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error al cargar el driver MySQL", e);
            throw new RuntimeException("Error al cargar el driver MySQL", e);
        }
    }
    
    private static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Ejecutar script de esquema completo
            executeScript(conn, "/database/schema.sql");
            logger.info("Base de datos inicializada correctamente");
        } catch (SQLException e) {
            logger.severe("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
    
    private static void executeScript(Connection conn, String scriptPath) throws SQLException {
        try (InputStream is = DatabaseConnection.class.getClassLoader().getResourceAsStream(scriptPath)) {
            if (is == null) {
                throw new SQLException("No se pudo encontrar el archivo: " + scriptPath);
            }
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder script = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    // Ignorar comentarios y líneas vacías
                    if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                        continue;
                    }
                    script.append(line).append("\n");
                }
                
                String[] statements = script.toString().split(";");
                for (String statement : statements) {
                    if (!statement.trim().isEmpty()) {
                        try (Statement stmt = conn.createStatement()) {
                            stmt.execute(statement);
                        }
                    }
                }
                logger.info("Script SQL ejecutado exitosamente: " + scriptPath);
            }
        } catch (IOException e) {
            logger.severe("Error al leer el script " + scriptPath + ": " + e.getMessage());
            throw new SQLException("Error al leer el script", e);
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