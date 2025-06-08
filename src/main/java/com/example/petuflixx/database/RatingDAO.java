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

    public List<Integer> getRecommendedMovieIds(int userId) {
        List<Integer> recommendedMovies = new ArrayList<>();
        String sql = """
            WITH UserRatings AS (
                SELECT movie_id, rating
                FROM ratings
                WHERE user_id = ?
            ),
            UserGenres AS (
                SELECT DISTINCT mg.genre_id
                FROM movie_genres mg
                JOIN UserRatings ur ON mg.movie_id = ur.movie_id
                WHERE ur.rating >= 4
            ),
            SimilarUsers AS (
                SELECT r.user_id, COUNT(*) as common_ratings
                FROM ratings r
                JOIN UserRatings ur ON r.movie_id = ur.movie_id
                WHERE r.user_id != ?
                AND ABS(r.rating - ur.rating) <= 1
                GROUP BY r.user_id
                HAVING common_ratings >= 2
                ORDER BY common_ratings DESC
                LIMIT 10
            )
            SELECT DISTINCT m.id
            FROM movies m
            JOIN movie_genres mg ON m.id = mg.movie_id
            LEFT JOIN UserGenres ug ON mg.genre_id = ug.genre_id
            LEFT JOIN ratings r ON m.id = r.movie_id AND r.user_id = ?
            WHERE r.id IS NULL
            AND m.vote_average >= 3.5
            GROUP BY m.id
            ORDER BY 
                COUNT(CASE WHEN ug.genre_id IS NOT NULL THEN 1 END) DESC,
                m.vote_average DESC,
                RAND()
            LIMIT 10
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                recommendedMovies.add(rs.getInt("id"));
            }
            
            // Si no hay suficientes recomendaciones, añadir películas populares
            if (recommendedMovies.size() < 10) {
                String fallbackSql = """
                    SELECT m.id
                    FROM movies m
                    LEFT JOIN ratings r ON m.id = r.movie_id AND r.user_id = ?
                    WHERE r.id IS NULL
                    AND m.vote_average >= 3.5
                    ORDER BY m.vote_count DESC, RAND()
                    LIMIT ?
                    """;
                
                try (PreparedStatement fallbackStmt = conn.prepareStatement(fallbackSql)) {
                    fallbackStmt.setInt(1, userId);
                    fallbackStmt.setInt(2, 10 - recommendedMovies.size());
                    
                    ResultSet fallbackRs = fallbackStmt.executeQuery();
                    while (fallbackRs.next()) {
                        int movieId = fallbackRs.getInt("id");
                        if (!recommendedMovies.contains(movieId)) {
                            recommendedMovies.add(movieId);
                        }
                    }
                }
            }
            
            logger.info("Se encontraron " + recommendedMovies.size() + " películas recomendadas para el usuario " + userId);
            
        } catch (SQLException e) {
            logger.severe("Error al obtener películas recomendadas: " + e.getMessage());
        }
        
        return recommendedMovies;
    }
} 