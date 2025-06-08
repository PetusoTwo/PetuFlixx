-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS petuflix_db;
USE petuflix_db;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso DATETIME,
    es_admin BOOLEAN DEFAULT FALSE
);

-- Tabla de películas
CREATE TABLE peliculas (
    id_pelicula INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    anio INT,
    duracion INT, -- en minutos
    poster_url VARCHAR(255),
    director VARCHAR(100),
    pais_origen VARCHAR(100),
    idioma_original VARCHAR(50),
    calificacion_promedio DECIMAL(3,2) DEFAULT 0.00,
    total_votos INT DEFAULT 0,
    fecha_agregado DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de géneros
CREATE TABLE generos (
    id_genero INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla de relación películas-géneros
CREATE TABLE peliculas_generos (
    id_pelicula INT,
    id_genero INT,
    PRIMARY KEY (id_pelicula, id_genero),
    FOREIGN KEY (id_pelicula) REFERENCES peliculas(id_pelicula) ON DELETE CASCADE,
    FOREIGN KEY (id_genero) REFERENCES generos(id_genero) ON DELETE CASCADE
);

-- Tabla de actores
CREATE TABLE actores (
    id_actor INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    biografia TEXT,
    foto_url VARCHAR(255)
);

-- Tabla de relación películas-actores
CREATE TABLE peliculas_actores (
    id_pelicula INT,
    id_actor INT,
    personaje VARCHAR(100),
    es_principal BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id_pelicula, id_actor),
    FOREIGN KEY (id_pelicula) REFERENCES peliculas(id_pelicula) ON DELETE CASCADE,
    FOREIGN KEY (id_actor) REFERENCES actores(id_actor) ON DELETE CASCADE
);

-- Tabla de valoraciones de usuarios
CREATE TABLE valoraciones (
    id_usuario INT,
    id_pelicula INT,
    puntuacion INT CHECK (puntuacion BETWEEN 1 AND 5),
    comentario TEXT,
    fecha_valoracion DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario, id_pelicula),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_pelicula) REFERENCES peliculas(id_pelicula) ON DELETE CASCADE
);

-- Tabla de listas de favoritos
CREATE TABLE favoritos (
    id_usuario INT,
    id_pelicula INT,
    fecha_agregado DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario, id_pelicula),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_pelicula) REFERENCES peliculas(id_pelicula) ON DELETE CASCADE
);

-- Tabla de recomendaciones
CREATE TABLE recomendaciones (
    id_usuario INT,
    id_pelicula INT,
    puntuacion_recomendacion DECIMAL(5,2),
    fecha_recomendacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario, id_pelicula),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_pelicula) REFERENCES peliculas(id_pelicula) ON DELETE CASCADE
);

-- Tabla de preferencias de usuario
CREATE TABLE preferencias_usuario (
    id_usuario INT,
    id_genero INT,
    peso DECIMAL(3,2) DEFAULT 1.00,
    PRIMARY KEY (id_usuario, id_genero),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_genero) REFERENCES generos(id_genero) ON DELETE CASCADE
);

-- Tabla de listas de reproducción
CREATE TABLE listas_reproduccion (
    id_lista INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    es_publica BOOLEAN DEFAULT FALSE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- Tabla de relación listas-películas
CREATE TABLE listas_peliculas (
    id_lista INT,
    id_pelicula INT,
    fecha_agregado DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_lista, id_pelicula),
    FOREIGN KEY (id_lista) REFERENCES listas_reproduccion(id_lista) ON DELETE CASCADE,
    FOREIGN KEY (id_pelicula) REFERENCES peliculas(id_pelicula) ON DELETE CASCADE
);

-- Tabla de historial de visualización
CREATE TABLE historial_visualizacion (
    id_usuario INT,
    id_pelicula INT,
    fecha_visualizacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    progreso INT DEFAULT 0, -- porcentaje visto
    PRIMARY KEY (id_usuario, id_pelicula),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_pelicula) REFERENCES peliculas(id_pelicula) ON DELETE CASCADE
);

-- Índices para optimización
CREATE INDEX idx_peliculas_titulo ON peliculas(titulo);
CREATE INDEX idx_peliculas_anio ON peliculas(anio);
CREATE INDEX idx_valoraciones_puntuacion ON valoraciones(puntuacion);
CREATE INDEX idx_peliculas_calificacion ON peliculas(calificacion_promedio);
CREATE INDEX idx_historial_fecha ON historial_visualizacion(fecha_visualizacion); 