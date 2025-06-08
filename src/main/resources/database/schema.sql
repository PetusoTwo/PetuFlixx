-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS petuflixx;
USE petuflixx;

-- Crear la tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarioss (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear la tabla de calificaciones
CREATE TABLE IF NOT EXISTS calificaciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    pelicula_id INT NOT NULL,
    calificacion INT NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
    fecha_calificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarioss(id),
    UNIQUE KEY unique_usuario_pelicula (usuario_id, pelicula_id)
);

-- Crear la tabla de géneros favoritos
CREATE TABLE IF NOT EXISTS generos_favoritos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    genero_id INT NOT NULL,
    peso FLOAT DEFAULT 1.0,
    FOREIGN KEY (usuario_id) REFERENCES usuarioss(id),
    UNIQUE KEY unique_usuario_genero (usuario_id, genero_id)
);

-- Crear la tabla de historial de vistas
CREATE TABLE IF NOT EXISTS historial_vistas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    pelicula_id INT NOT NULL,
    fecha_vista TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarioss(id)
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_calificaciones_usuario ON calificaciones(usuario_id);
CREATE INDEX idx_calificaciones_pelicula ON calificaciones(pelicula_id);
CREATE INDEX idx_generos_favoritos_usuario ON generos_favoritos(usuario_id);
CREATE INDEX idx_historial_usuario ON historial_vistas(usuario_id); 