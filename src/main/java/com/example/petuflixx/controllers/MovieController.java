package com.example.petuflixx.controllers;

import com.example.petuflixx.api.TMDBService;
import com.example.petuflixx.api.TMDBService.Movie;
import com.example.petuflixx.api.TMDBService.MovieDetails;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class MovieController {
    @FXML private TextField searchField;
    @FXML private FlowPane moviesContainer;
    @FXML private ScrollPane scrollPane;
    @FXML private ComboBox<String> genreFilter;
    @FXML private Slider ratingSlider;
    @FXML private Label ratingLabel;

    private final TMDBService tmdbService;
    private int currentPage = 1;
    private boolean isLoading = false;

    public MovieController() {
        this.tmdbService = new TMDBService();
    }

    @FXML
    public void initialize() {
        // Configurar el slider de calificación
        ratingSlider.setMin(1);
        ratingSlider.setMax(5);
        ratingSlider.setValue(3);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setShowTickLabels(true);
        ratingSlider.setMajorTickUnit(1);
        ratingSlider.setBlockIncrement(1);
        ratingSlider.setSnapToTicks(true);

        // Actualizar etiqueta de calificación
        ratingSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            ratingLabel.setText(String.format("Calificación: %.0f", newVal.doubleValue()));
        });

        // Cargar películas populares iniciales
        loadPopularMovies();

        // Configurar búsqueda
        searchField.setOnAction(e -> searchMovies());

        // Configurar scroll infinito
        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 1.0 && !isLoading) {
                loadMoreMovies();
            }
        });
    }

    private void loadPopularMovies() {
        isLoading = true;
        List<Movie> movies = tmdbService.getPopularMovies(currentPage);
        displayMovies(movies);
        isLoading = false;
    }

    private void searchMovies() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            currentPage = 1;
            moviesContainer.getChildren().clear();
            List<Movie> movies = tmdbService.searchMovies(query, currentPage);
            displayMovies(movies);
        }
    }

    private void loadMoreMovies() {
        if (!isLoading) {
            isLoading = true;
            currentPage++;
            List<Movie> moreMovies = tmdbService.getPopularMovies(currentPage);
            displayMovies(moreMovies);
            isLoading = false;
        }
    }

    private void displayMovies(List<Movie> movies) {
        for (Movie movie : movies) {
            VBox movieCard = createMovieCard(movie);
            moviesContainer.getChildren().add(movieCard);
        }
    }

    private VBox createMovieCard(Movie movie) {
        VBox card = new VBox(10);
        card.getStyleClass().add("movie-card");

        // Imagen del poster
        ImageView posterView = new ImageView();
        posterView.setFitWidth(200);
        posterView.setFitHeight(300);
        posterView.setPreserveRatio(true);
        posterView.setImage(new Image(movie.getPosterUrl()));

        // Título
        Label titleLabel = new Label(movie.getTitle());
        titleLabel.getStyleClass().add("movie-title");

        // Calificación
        Label ratingLabel = new Label(String.format("★ %.1f", movie.getVoteAverage()));
        ratingLabel.getStyleClass().add("movie-rating");

        // Botón de calificar
        Button rateButton = new Button("Calificar");
        rateButton.setOnAction(e -> showRatingDialog(movie));

        card.getChildren().addAll(posterView, titleLabel, ratingLabel, rateButton);
        return card;
    }

    private void showRatingDialog(Movie movie) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Calificar Película");
        dialog.setHeaderText("Califica " + movie.getTitle());

        // Crear contenido del diálogo
        VBox content = new VBox(10);
        Label ratingLabel = new Label("Tu calificación:");
        Slider ratingSlider = new Slider(1, 5, 3);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setShowTickLabels(true);
        ratingSlider.setMajorTickUnit(1);
        ratingSlider.setBlockIncrement(1);
        ratingSlider.setSnapToTicks(true);

        Label valueLabel = new Label("3");
        ratingSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            valueLabel.setText(String.format("%.0f", newVal.doubleValue()));
        });

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Escribe tu comentario (opcional)");
        commentArea.setPrefRowCount(3);

        content.getChildren().addAll(ratingLabel, ratingSlider, valueLabel, commentArea);
        dialog.getDialogPane().setContent(content);

        // Botones
        ButtonType rateButtonType = new ButtonType("Calificar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(rateButtonType, ButtonType.CANCEL);

        // Mostrar diálogo y procesar resultado
        dialog.showAndWait().ifPresent(response -> {
            if (response == rateButtonType) {
                int rating = (int) ratingSlider.getValue();
                String comment = commentArea.getText();
                // Aquí guardarías la calificación en la base de datos
                saveRating(movie.getId(), rating, comment);
            }
        });
    }

    private void saveRating(int movieId, int rating, String comment) {
        // TODO: Implementar guardado en base de datos
        System.out.printf("Guardando calificación: Película %d, Rating %d, Comentario: %s%n", 
            movieId, rating, comment);
    }
} 