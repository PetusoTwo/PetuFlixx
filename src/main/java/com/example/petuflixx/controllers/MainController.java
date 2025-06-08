package com.example.petuflixx.controllers;

import com.example.petuflixx.api.TMDBService;
import com.example.petuflixx.api.TMDBService.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import java.util.List;

public class MainController {
    @FXML private TextField searchField;
    @FXML private FlowPane featuredMoviesContainer;
    @FXML private FlowPane popularMoviesContainer;
    @FXML private FlowPane recommendedMoviesContainer;
    @FXML private ImageView featuredMovieImage;
    @FXML private Label featuredMovieTitle;
    @FXML private Label featuredMovieDescription;
    @FXML private Label featuredMovieRating;
    @FXML private Label featuredMovieYear;
    @FXML private Label featuredMovieDuration;

    private final TMDBService tmdbService;

    public MainController() {
        this.tmdbService = new TMDBService();
    }

    @FXML
    public void initialize() {
        loadFeaturedMovie();
        loadPopularMovies();
        loadRecommendedMovies();
        
        // Configurar búsqueda
        searchField.setOnAction(e -> searchMovies());
    }

    private void loadFeaturedMovie() {
        List<Movie> movies = tmdbService.getPopularMovies(1);
        if (!movies.isEmpty()) {
            Movie featured = movies.get(0);
            featuredMovieImage.setImage(new Image(featured.getBackdropUrl()));
            featuredMovieTitle.setText(featured.getTitle());
            featuredMovieDescription.setText(featured.getOverview());
            featuredMovieRating.setText(String.format("%.1f", featured.getVoteAverage()));
            featuredMovieYear.setText(String.valueOf(featured.getReleaseYear()));
            // La duración no está disponible en la API básica, podríamos obtenerla de los detalles
        }
    }

    private void loadPopularMovies() {
        List<Movie> movies = tmdbService.getPopularMovies(1);
        displayMovies(movies, popularMoviesContainer);
    }

    private void loadRecommendedMovies() {
        List<Movie> movies = tmdbService.getTopRatedMovies(1);
        displayMovies(movies, recommendedMoviesContainer);
    }

    private void searchMovies() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            List<Movie> movies = tmdbService.searchMovies(query, 1);
            displayMovies(movies, popularMoviesContainer);
        }
    }

    private void displayMovies(List<Movie> movies, FlowPane container) {
        container.getChildren().clear();
        for (Movie movie : movies) {
            VBox movieCard = createMovieCard(movie);
            container.getChildren().add(movieCard);
        }
    }

    private VBox createMovieCard(Movie movie) {
        VBox card = new VBox(10);
        card.getStyleClass().add("movie-card");

        ImageView posterView = new ImageView();
        posterView.setFitWidth(240);
        posterView.setFitHeight(360);
        posterView.setPreserveRatio(true);
        posterView.setImage(new Image(movie.getPosterUrl()));

        Label titleLabel = new Label(movie.getTitle());
        titleLabel.getStyleClass().add("movie-title");

        HBox infoBox = new HBox(10);
        Label ratingLabel = new Label(String.format("★ %.1f", movie.getVoteAverage()));
        ratingLabel.getStyleClass().add("movie-rating");
        Label yearLabel = new Label(String.valueOf(movie.getReleaseYear()));
        yearLabel.getStyleClass().add("movie-info");
        infoBox.getChildren().addAll(ratingLabel, yearLabel);

        card.getChildren().addAll(posterView, titleLabel, infoBox);
        return card;
    }
} 