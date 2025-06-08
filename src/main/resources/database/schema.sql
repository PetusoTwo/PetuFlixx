-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS petuflixx;
USE petuflixx;

-- Crear la tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear la tabla de géneros
CREATE TABLE IF NOT EXISTS genres (
    id INTEGER PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Insertar géneros comunes
INSERT IGNORE INTO genres (id, name) VALUES
(28, 'Acción'),
(12, 'Aventura'),
(16, 'Animación'),
(35, 'Comedia'),
(80, 'Crimen'),
(99, 'Documental'),
(18, 'Drama'),
(10751, 'Familia'),
(14, 'Fantasía'),
(36, 'Historia'),
(27, 'Terror'),
(10402, 'Música'),
(9648, 'Misterio'),
(10749, 'Romance'),
(878, 'Ciencia ficción'),
(10770, 'Película de TV'),
(53, 'Suspense'),
(10752, 'Bélica'),
(37, 'Western');

-- Crear la tabla de películas
CREATE TABLE IF NOT EXISTS movies (
    id INTEGER PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    overview TEXT,
    poster_path VARCHAR(255),
    vote_average REAL,
    vote_count INTEGER,
    release_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear la tabla de géneros de películas
CREATE TABLE IF NOT EXISTS movie_genres (
    movie_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    PRIMARY KEY (movie_id, genre_id),
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

-- Crear la tabla de calificaciones
CREATE TABLE IF NOT EXISTS ratings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    movie_id INTEGER NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    UNIQUE(user_id, movie_id)
);

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_movies_title ON movies(title);
CREATE INDEX IF NOT EXISTS idx_movies_vote_average ON movies(vote_average);
CREATE INDEX IF NOT EXISTS idx_movie_genres_movie_id ON movie_genres(movie_id);
CREATE INDEX IF NOT EXISTS idx_movie_genres_genre_id ON movie_genres(genre_id);
CREATE INDEX IF NOT EXISTS idx_ratings_user_id ON ratings(user_id);
CREATE INDEX IF NOT EXISTS idx_ratings_movie_id ON ratings(movie_id);

-- Crear la tabla de géneros favoritos
CREATE TABLE IF NOT EXISTS generos_favoritos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    genero_id INT NOT NULL,
    peso FLOAT DEFAULT 1.0,
    FOREIGN KEY (usuario_id) REFERENCES users(id),
    UNIQUE KEY unique_usuario_genero (usuario_id, genero_id)
);

-- Crear la tabla de historial de vistas
CREATE TABLE IF NOT EXISTS historial_vistas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    pelicula_id INT NOT NULL,
    fecha_vista TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES users(id)
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_calificaciones_usuario ON ratings(user_id);
CREATE INDEX idx_calificaciones_pelicula ON ratings(movie_id);
CREATE INDEX idx_generos_favoritos_usuario ON generos_favoritos(usuario_id);
CREATE INDEX idx_historial_usuario ON historial_vistas(usuario_id); 