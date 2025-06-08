package com.example.petuflixx.database;

import com.example.petuflixx.models.Rating;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RatingDAO {
    private static final Logger logger = Logger.getLogger(RatingDAO.class.getName());

    public void saveRating(Rating rating) {
        String sql = "INSERT INTO ratings (user_id, movie_id, rating) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE rating = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rating.getUserId());
            pstmt.setInt(2, rating.getMovieId());
            pstmt.setInt(3, rating.getRating());
            pstmt.setInt(4, rating.getRating());
            
            int affectedRows = pstmt.executeUpdate();
            logger.info("Calificación guardada. Filas afectadas: " + affectedRows);
            
        } catch (SQLException e) {
            logger.severe("Error al guardar la calificación: " + e.getMessage());
            throw new RuntimeException("Error al guardar la calificación", e);
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
            WITH UserGenres AS (
                SELECT DISTINCT m.genre_id
                FROM ratings r
                JOIN movie_genres m ON r.movie_id = m.movie_id
                WHERE r.user_id = ? AND r.rating >= 4
            ),
            SimilarUsers AS (
                SELECT r2.user_id, COUNT(DISTINCT m.genre_id) as common_genres
                FROM ratings r2
                JOIN movie_genres m ON r2.movie_id = m.movie_id
                JOIN UserGenres ug ON m.genre_id = ug.genre_id
                WHERE r2.user_id != ?
                GROUP BY r2.user_id
                HAVING common_genres > 0
            ),
            RecommendedMovies AS (
                SELECT r.movie_id, AVG(r.rating) as avg_rating
                FROM ratings r
                JOIN SimilarUsers su ON r.user_id = su.user_id
                WHERE r.movie_id NOT IN (
                    SELECT movie_id FROM ratings WHERE user_id = ?
                )
                GROUP BY r.movie_id
                HAVING avg_rating >= 3.5
                ORDER BY avg_rating DESC
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
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                movieIds.add(rs.getInt("movie_id"));
            }
        }
        return movieIds;
    }
} 