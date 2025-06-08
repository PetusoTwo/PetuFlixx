package com.example.petuflixx.database;

import com.example.petuflixx.models.Rating;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RatingDAO {
    private static final Logger logger = Logger.getLogger(RatingDAO.class.getName());

    public void saveRating(int userId, int movieId, int rating) throws SQLException {
        String sql = "INSERT INTO ratings (user_id, movie_id, rating) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE rating = VALUES(rating)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, movieId);
            stmt.setInt(3, rating);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Calificación guardada exitosamente para usuario " + userId + " y película " + movieId);
            } else {
                logger.warning("No se pudo guardar la calificación");
            }
        } catch (SQLException e) {
            logger.severe("Error al guardar la calificación: " + e.getMessage());
            throw e;
        }
    }

    public List<Rating> getUserRatings(int userId) {
        String sql = "SELECT * FROM ratings WHERE user_id = ? ORDER BY created_at DESC";
        List<Rating> ratings = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Rating rating = new Rating(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("movie_id"),
                    rs.getInt("rating"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                ratings.add(rating);
            }
            
        } catch (SQLException e) {
            logger.severe("Error al obtener las calificaciones del usuario: " + e.getMessage());
            throw new RuntimeException("Error al obtener las calificaciones del usuario", e);
        }
        
        return ratings;
    }

    public List<Integer> getRecommendedMovieIds(int userId) throws SQLException {
        String sql = """
            WITH UserRatings AS (
                -- Obtener las calificaciones del usuario
                SELECT movie_id, rating
                FROM ratings
                WHERE user_id = ?
            ),
            UserGenres AS (
                -- Obtener los géneros de las películas que el usuario ha calificado positivamente
                SELECT DISTINCT mg.genre_id
                FROM movie_genres mg
                JOIN UserRatings ur ON mg.movie_id = ur.movie_id
                WHERE ur.rating >= 4
            ),
            SimilarUsers AS (
                -- Encontrar usuarios con gustos similares
                SELECT r2.user_id, 
                       COUNT(DISTINCT mg.genre_id) as common_genres,
                       AVG(ABS(r1.rating - r2.rating)) as rating_diff
                FROM ratings r1
                JOIN ratings r2 ON r1.movie_id = r2.movie_id AND r2.user_id != ?
                JOIN movie_genres mg ON r1.movie_id = mg.movie_id
                JOIN UserGenres ug ON mg.genre_id = ug.genre_id
                WHERE r1.user_id = ?
                GROUP BY r2.user_id
                HAVING common_genres > 0
                ORDER BY common_genres DESC, rating_diff ASC
                LIMIT 10
            ),
            RecommendedMovies AS (
                -- Obtener películas recomendadas basadas en usuarios similares
                SELECT r.movie_id,
                       COUNT(DISTINCT r.user_id) as user_count,
                       AVG(r.rating) as avg_rating,
                       COUNT(DISTINCT mg.genre_id) as matching_genres
                FROM ratings r
                JOIN SimilarUsers su ON r.user_id = su.user_id
                JOIN movie_genres mg ON r.movie_id = mg.movie_id
                JOIN UserGenres ug ON mg.genre_id = ug.genre_id
                WHERE r.movie_id NOT IN (
                    SELECT movie_id FROM ratings WHERE user_id = ?
                )
                GROUP BY r.movie_id
                HAVING avg_rating >= 3.5
                ORDER BY matching_genres DESC, avg_rating DESC, user_count DESC
                LIMIT 20
            )
            SELECT movie_id FROM RecommendedMovies
            """;

        List<Integer> movieIds = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            stmt.setInt(4, userId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                movieIds.add(rs.getInt("movie_id"));
            }
            
            logger.info("Se encontraron " + movieIds.size() + " películas recomendadas para el usuario " + userId);
        }
        return movieIds;
    }
} 