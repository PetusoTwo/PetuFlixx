package com.example.petuflixx.controllers;

import com.example.petuflixx.api.TMDBService;
import com.example.petuflixx.api.TMDBService.Movie;
import com.example.petuflixx.database.RatingDAO;
import com.example.petuflixx.utils.NavigationUtils;
import com.example.petuflixx.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class MainController {
    private static final Logger logger = Logger.getLogger(MainController.class.getName());
    private final TMDBService tmdbService = new TMDBService();
    private final RatingDAO ratingDAO = new RatingDAO();
    private String currentUser;
    private int currentUserId;

    @FXML private TextField searchField;
    @FXML private FlowPane popularMoviesContainer;
    @FXML private FlowPane recommendedMoviesContainer;
    @FXML private ImageView featuredMovieImage;
    @FXML private Label featuredMovieTitle;
    @FXML private Label featuredMovieRating;
    @FXML private Label featuredMovieYear;
    @FXML private Label featuredMovieDuration;
    @FXML private Label featuredMovieDescription;
    @FXML private Label userNameLabel;
    @FXML private MenuButton userMenu;
    @FXML private StackPane rootPane;

    @FXML
    public void initialize() {
        logger.info("Inicializando MainController");
        setupSearchField();
        setupUserMenu();
        loadPopularMovies();
        loadRecommendedMovies();
        loadFeaturedMovie();
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                searchMovies(newVal);
            } else {
                loadPopularMovies();
            }
        });
    }

    private void setupUserMenu() {
        MenuItem logoutItem = new MenuItem("Cerrar Sesión");
        logoutItem.setOnAction(e -> onLogoutClick());
        userMenu.getItems().add(logoutItem);
    }

    public void setCurrentUser(String userName, int userId) {
        this.currentUser = userName;
        this.currentUserId = userId;
        userNameLabel.setText(userName);
        loadRecommendedMovies(); // Recargar recomendaciones cuando se establece el usuario
    }

    private void onLogoutClick() {
        logger.info("Usuario " + currentUser + " ha cerrado sesión");
        Stage currentStage = (Stage) userNameLabel.getScene().getWindow();
        NavigationUtils.navigateTo("/com/example/petuflixx/hello-view.fxml", currentStage);
    }

    private void loadPopularMovies() {
        try {
            List<Movie> movies = tmdbService.getPopularMovies(1);
            displayMovies(movies, popularMoviesContainer);
        } catch (Exception e) {
            logger.severe("Error al cargar películas populares: " + e.getMessage());
            showError("Error al cargar películas populares");
        }
    }

    private void loadRecommendedMovies() {
        try {
            if (currentUserId > 0) {
                List<Integer> recommendedMovieIds = ratingDAO.getRecommendedMovieIds(currentUserId);
                if (!recommendedMovieIds.isEmpty()) {
                    List<Movie> recommendedMovies = tmdbService.getMoviesByIds(recommendedMovieIds);
                    displayMovies(recommendedMovies, recommendedMoviesContainer);
                } else {
                    // Si no hay recomendaciones, mostrar películas populares
                    List<Movie> popularMovies = tmdbService.getPopularMovies(2);
                    displayMovies(popularMovies, recommendedMoviesContainer);
                }
            } else {
                // Si no hay usuario logueado, mostrar películas populares
                List<Movie> popularMovies = tmdbService.getPopularMovies(2);
                displayMovies(popularMovies, recommendedMoviesContainer);
            }
        } catch (Exception e) {
            logger.severe("Error al cargar películas recomendadas: " + e.getMessage());
            showError("Error al cargar películas recomendadas");
        }
    }

    private void loadFeaturedMovie() {
        try {
            List<Movie> movies = tmdbService.getPopularMovies(1);
            if (!movies.isEmpty()) {
                Movie featured = movies.get(0);
                featuredMovieImage.setImage(new Image(featured.getPosterUrl()));
                featuredMovieTitle.setText(featured.getTitle());
                featuredMovieRating.setText(String.format("%.1f", featured.getVoteAverage()) + " ★");
                featuredMovieYear.setText(String.valueOf(featured.getReleaseYear()));
                featuredMovieDuration.setText("2h 15m"); // Esto debería venir de la API
                featuredMovieDescription.setText(featured.getOverview());
            }
        } catch (Exception e) {
            logger.severe("Error al cargar película destacada: " + e.getMessage());
            showError("Error al cargar película destacada");
        }
    }

    private void searchMovies(String query) {
        try {
            List<Movie> movies = tmdbService.searchMovies(query, 1);
            displayMovies(movies, popularMoviesContainer);
            recommendedMoviesContainer.getChildren().clear();
        } catch (Exception e) {
            logger.severe("Error al buscar películas: " + e.getMessage());
            showError("Error al buscar películas");
        }
    }

    private void displayMovies(List<Movie> movies, FlowPane container) {
        container.getChildren().clear();
        for (Movie movie : movies) {
            VBox movieCard = createMovieCard(movie);
            container.getChildren().add(movieCard);
            
            // Guardar los géneros de la película
            saveMovieGenres(movie.getId(), movie.getGenreIds());
        }
    }

    private VBox createMovieCard(Movie movie) {
        VBox card = new VBox(10);
        card.getStyleClass().add("movie-card");
        card.setPrefWidth(200);

        ImageView poster = new ImageView(new Image(movie.getPosterUrl()));
        poster.setFitWidth(200);
        poster.setFitHeight(300);
        poster.setPreserveRatio(true);

        Label title = new Label(movie.getTitle());
        title.getStyleClass().add("movie-title");
        title.setWrapText(true);

        Label rating = new Label(String.format("%.1f", movie.getVoteAverage()) + " ★");
        rating.getStyleClass().add("movie-rating");

        card.getChildren().addAll(poster, title, rating);
        card.setOnMouseClicked(e -> showMovieDetails(movie));

        return card;
    }

    private void showMovieDetails(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/petuflixx/movie-details-view.fxml"));
            Parent root = loader.load();

            MovieDetailsController controller = loader.getController();
            controller.setMovie(movie);
            controller.setCurrentUserId(currentUserId);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));

            // Aplicar efecto de desenfoque al fondo
            GaussianBlur blur = new GaussianBlur(10);
            rootPane.setEffect(blur);

            // Quitar el efecto cuando se cierra el modal
            stage.setOnHidden(e -> {
                rootPane.setEffect(null);
                loadRecommendedMovies(); // Recargar recomendaciones después de calificar
            });

            stage.showAndWait();
        } catch (IOException e) {
            logger.severe("Error al mostrar detalles de la película: " + e.getMessage());
            showError("Error al mostrar detalles de la película");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveMovieGenres(int movieId, List<Integer> genreIds) {
        String sql = "INSERT OR IGNORE INTO movie_genres (movie_id, genre_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (Integer genreId : genreIds) {
                stmt.setInt(1, movieId);
                stmt.setInt(2, genreId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.severe("Error al guardar géneros de la película: " + e.getMessage());
        }
    }
} 