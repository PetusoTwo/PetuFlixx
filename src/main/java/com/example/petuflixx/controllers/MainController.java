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
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Stage;
import javafx.scene.Scene;
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
    @FXML private StackPane rootPane;

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
        
        // Agregar evento de clic para mostrar el modal
        card.setOnMouseClicked(e -> showMovieDetailsModal(movie));
        
        return card;
    }

    private void showMovieDetailsModal(Movie movie) {
        // Crear el contenido del modal
        HBox modalContent = new HBox(20);
        modalContent.setStyle("-fx-background-color: #1f1f1f; -fx-padding: 20; -fx-background-radius: 10;");
        modalContent.setMaxWidth(1000);
        modalContent.setMaxHeight(600);

        // Panel izquierdo con la imagen
        VBox leftPanel = new VBox(10);
        leftPanel.setStyle("-fx-background-color: #2a2a2a; -fx-padding: 15; -fx-background-radius: 5;");
        leftPanel.setPrefWidth(400);

        ImageView posterImage = new ImageView(new Image(movie.getPosterUrl()));
        posterImage.setFitWidth(370);
        posterImage.setFitHeight(500);
        posterImage.setPreserveRatio(true);
        posterImage.setStyle("-fx-background-radius: 5;");

        // Panel derecho con la información
        VBox rightPanel = new VBox(20);
        rightPanel.setStyle("-fx-padding: 20;");
        rightPanel.setPrefWidth(500);

        // Título y año
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        
        Label yearLabel = new Label(String.valueOf(movie.getReleaseYear()));
        yearLabel.setStyle("-fx-text-fill: #999; -fx-font-size: 24px;");
        
        titleBox.getChildren().addAll(titleLabel, yearLabel);

        // Calificación actual
        HBox ratingBox = new HBox(10);
        ratingBox.setAlignment(Pos.CENTER_LEFT);
        
        Label ratingLabel = new Label(String.format("★ %.1f", movie.getVoteAverage()));
        ratingLabel.setStyle("-fx-text-fill: #46d369; -fx-font-size: 20px; -fx-font-weight: bold;");
        
        Label ratingCount = new Label("(Calificación de la comunidad)");
        ratingCount.setStyle("-fx-text-fill: #999; -fx-font-size: 16px;");
        
        ratingBox.getChildren().addAll(ratingLabel, ratingCount);

        // Descripción
        Label overviewLabel = new Label(movie.getOverview());
        overviewLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        overviewLabel.setWrapText(true);

        // Sistema de votación
        VBox userRatingBox = new VBox(15);
        userRatingBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 5;");

        Label ratingTitle = new Label("Tu Calificación");
        ratingTitle.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        HBox starsBox = new HBox(10);
        starsBox.setAlignment(Pos.CENTER_LEFT);

        // Crear estrellas interactivas
        for (int i = 1; i <= 5; i++) {
            Label star = new Label("★");
            final int rating = i;
            star.setStyle("-fx-text-fill: #666; -fx-font-size: 35px; -fx-cursor: hand;");
            star.setOnMouseEntered(e -> {
                for (int j = 0; j < starsBox.getChildren().size(); j++) {
                    Label s = (Label) starsBox.getChildren().get(j);
                    s.setStyle("-fx-text-fill: " + (j < rating ? "#FFD700" : "#666") + "; -fx-font-size: 35px; -fx-cursor: hand;");
                }
            });
            star.setOnMouseExited(e -> {
                for (int j = 0; j < starsBox.getChildren().size(); j++) {
                    Label s = (Label) starsBox.getChildren().get(j);
                    s.setStyle("-fx-text-fill: #666; -fx-font-size: 35px; -fx-cursor: hand;");
                }
            });
            star.setOnMouseClicked(e -> {
                // Aquí guardarías la calificación en la base de datos
                System.out.println("Calificación guardada: " + rating + " estrellas para " + movie.getTitle());
                // Cerrar el modal después de votar
                ((Stage) modalContent.getScene().getWindow()).close();
            });
            starsBox.getChildren().add(star);
        }

        userRatingBox.getChildren().addAll(ratingTitle, starsBox);

        // Botón de cerrar
        Button closeButton = new Button("Cerrar");
        closeButton.setStyle("-fx-background-color: #e50914; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30; -fx-font-size: 16px;");
        closeButton.setOnAction(e -> ((Stage) modalContent.getScene().getWindow()).close());

        // Agregar todo al panel derecho
        rightPanel.getChildren().addAll(titleBox, ratingBox, overviewLabel, userRatingBox, closeButton);

        // Agregar los paneles al contenido del modal
        modalContent.getChildren().addAll(leftPanel, rightPanel);
        leftPanel.getChildren().add(posterImage);

        // Crear la escena y el stage
        Scene scene = new Scene(modalContent);
        scene.getStylesheets().add(getClass().getResource("/com/example/petuflixx/styles.css").toExternalForm());

        Stage modalStage = new Stage(StageStyle.TRANSPARENT);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(rootPane.getScene().getWindow());
        modalStage.setScene(scene);

        // Efecto de desenfoque en el fondo
        GaussianBlur blur = new GaussianBlur(10);
        rootPane.setEffect(blur);

        // Quitar el efecto cuando se cierra el modal
        modalStage.setOnHidden(e -> rootPane.setEffect(null));

        modalStage.show();
    }
} 