# PetuFlixx

¡Bienvenido a PetuFlixx, tu propia aplicación de escritorio de gestión y recomendación de películas! Desarrollada con JavaFX y conectada a una base de datos MySQL/MariaDB, esta aplicación te permite registrarte, iniciar sesión, explorar un catálogo de películas (gracias a la API de TMDB), calificar tus favoritas y recibir recomendaciones personalizadas basadas en tus gustos.

## Características

-   **Autenticación de Usuarios**: Registro y inicio de sesión seguro.
-   **Exploración de Películas**: Navega por películas populares, mejor valoradas y recomendadas.
-   **Búsqueda de Películas**: Encuentra cualquier película usando la barra de búsqueda.
-   **Calificación de Películas**: Califica las películas con un sistema de estrellas.
-   **Sistema de Recomendación**: Recibe recomendaciones de películas personalizadas basadas en tus calificaciones y géneros preferidos, y en usuarios con gustos similares.
-   **Interfaz de Usuario Moderna**: Un diseño intuitivo y atractivo inspirado en plataformas de streaming populares.

## Tecnologías Utilizadas

-   **Frontend**: JavaFX
-   **Backend**: Java (JDBC)
-   **Base de Datos**: MySQL / MariaDB
-   **API Externa**: The Movie Database (TMDB) API
-   **Herramienta de Construcción**: Maven

## Configuración del Proyecto

Sigue estos pasos para configurar y ejecutar PetuFlixx en tu máquina local.

### Prerrequisitos

-   Java Development Kit (JDK) 11 o superior instalado.
-   Maven instalado.
-   Servidor de base de datos MySQL o MariaDB en ejecución (por ejemplo, con XAMPP o WAMP).
-   Una clave API de TMDB (puedes obtener una en [The Movie Database API](https://www.themoviedb.org/documentation/api)).

### 1. Configuración de la Base de Datos

1.  **Crea la base de datos**:
    Abre tu herramienta de gestión de bases de datos (por ejemplo, phpMyAdmin para XAMPP/WAMP) y crea una nueva base de datos llamada `petuflixx`.

2.  **Ejecuta el script SQL**:
    Usa el siguiente script SQL para crear las tablas necesarias en tu base de datos `petuflixx`. Puedes copiar y pegar esto directamente en la sección SQL de phpMyAdmin y ejecutarlo.

    ```sql
      database.sql
    ```

3.  **Actualiza la conexión a la base de datos**:
    Asegúrate de que la clase `src/main/java/com/example/petuflixx/database/DatabaseConnection.java` tenga los detalles de conexión correctos para tu base de datos (nombre de usuario, contraseña, URL).

    ```java
    // Ejemplo de DatabaseConnection.java
    public class DatabaseConnection {
        private static final String DB_URL = "jdbc:mysql://localhost:3306/petuflixx?useSSL=false&allowPublicKeyRetrieval=true";
        private static final String DB_USER = "tu_usuario_mysql"; // Cambia esto
        private static final String DB_PASSWORD = "tu_password_mysql"; // Cambia esto
        // ... el resto de tu código
    }
    ```

### 2. Configuración de la API de TMDB

1.  Abre el archivo `src/main/java/com/example/petuflixx/api/TMDBService.java`.
2.  Reemplaza `YOUR_API_KEY` con tu clave API de TMDB.

    ```java
    // Ejemplo de TMDBService.java
    public class TMDBService {
        private static final String API_KEY = "tu_clave_api_tmdb"; // Cambia esto
        // ... el resto de tu código
    }
    ```

### 3. Construir y Ejecutar la Aplicación

1.  Abre tu terminal en la raíz del proyecto (donde se encuentra `pom.xml`).
2.  **Limpia y construye el proyecto con Maven**:
    ```bash
    mvn clean install
    ```
3.  **Ejecuta la aplicación**:
    Puedes ejecutarla desde tu IDE (IntelliJ IDEA, Eclipse, etc.) o desde la línea de comandos si tienes configurado el plugin `javafx-maven-plugin`:

    ```bash
    mvn javafx:run
    ```

## Capturas de Pantalla

### Pantalla de Inicio de Sesión

![Captura de pantalla 2025-06-08 150521](https://github.com/user-attachments/assets/3aee49ec-733f-4bb9-a039-c21fc98a2027)


### Pantalla de Registro

![Captura de pantalla 2025-06-08 150532](https://github.com/user-attachments/assets/2c4d8fc1-1861-4675-b107-8f41acca42bd)

### Vista Principal de Películas

![Captura de pantalla 2025-06-08 123828](https://github.com/user-attachments/assets/ec18c316-4ea3-4608-a150-583a3f82898b)


### Detalles de la Película y Calificación

![Captura de pantalla 2025-06-08 151331](https://github.com/user-attachments/assets/fdcc014c-d980-459d-9395-6bb3e78e9dde)
