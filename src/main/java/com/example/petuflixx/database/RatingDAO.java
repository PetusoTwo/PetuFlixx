package com.example.petuflixx.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingDAO {
    
    public void saveRating(int userId, int movieId, int rating) {
        String sql = "INSERT INTO calificaciones (usuario_id, pelicula_id, calificacion) " +
                    "VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE calificacion = ?";
                    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, movieId);
            pstmt.setInt(3, rating);
            pstmt.setInt(4, rating);
            
            pstmt.executeUpdate();
            
            // Actualizar géneros favoritos basado en la calificación
            updateFavoriteGenres(userId, movieId, rating);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la calificación: " + e.getMessage());
        }
    }
    
    public int getUserRating(int userId, int movieId) {
        String sql = "SELECT calificacion FROM calificaciones WHERE usuario_id = ? AND pelicula_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, movieId);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("calificacion");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0; // Retorna 0 si no hay calificación
    }
    
    private void updateFavoriteGenres(int userId, int movieId, int rating) {
        // Obtener los géneros de la película desde la API de TMDB
        // y actualizar la tabla de géneros_favoritos
        // Este método se implementará cuando agreguemos la lógica de recomendaciones
    }
    
    public List<Integer> getRecommendedMovies(int userId) {
        String sql = "SELECT DISTINCT c2.pelicula_id " +
                    "FROM calificaciones c1 " +
                    "JOIN calificaciones c2 ON c1.pelicula_id != c2.pelicula_id " +
                    "WHERE c1.usuario_id = ? AND c1.calificacion >= 4 " +
                    "ORDER BY c2.calificacion DESC " +
                    "LIMIT 20";
                    
        List<Integer> recommendedMovies = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                recommendedMovies.add(rs.getInt("pelicula_id"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return recommendedMovies;
    }
} 