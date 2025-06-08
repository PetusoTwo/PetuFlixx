-- Crear la nueva tabla users si no existe
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Migrar datos de usuarioss a users
INSERT INTO users (username, email, password, name)
SELECT 
    email as username,  -- Usamos el email como username inicialmente
    email,
    password,
    nombre as name
FROM usuarioss
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE users.email = usuarioss.email
);

-- Crear índices para mejorar el rendimiento de búsqueda
CREATE INDEX IF NOT EXISTS idx_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_email ON users(email);

-- Opcional: Eliminar la tabla antigua después de verificar la migración
-- DROP TABLE IF EXISTS usuarioss; 