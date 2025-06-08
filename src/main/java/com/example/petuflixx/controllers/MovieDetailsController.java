package com.example.petuflixx.controllers;

import com.example.petuflixx.api.TMDBService.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class MovieDetailsController {
    private static final Logger logger = Logger.getLogger(MovieDetailsController.class.getName());
    private Movie movie;

    @FXML private ImageView posterImage;
    @FXML private Label titleLabel;
    @FXML private Label yearLabel;
    @FXML private Label ratingLabel;
    @FXML private Label ratingCountLabel;
    @FXML private Label overviewLabel;
    @FXML private VBox userRatingBox;
    @FXML private Button closeButton;

    public void setMovie(Movie movie) {
        this.movie = movie;
        updateUI();
    }

    private void updateUI() {
        if (movie != null) {
            posterImage.setImage(new Image(movie.getPosterUrl()));
            titleLabel.setText(movie.getTitle());
            yearLabel.setText(String.valueOf(movie.getReleaseYear()));
            ratingLabel.setText(String.format("★ %.1f", movie.getVoteAverage()));
            ratingCountLabel.setText("(Calificación de la comunidad)");
            overviewLabel.setText(movie.getOverview());
            setupRatingSystem();
        }
    }

    private void setupRatingSystem() {
        HBox starsBox = new HBox(10);
        starsBox.setStyle("-fx-alignment: center-left;");

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
                logger.info("Calificación guardada: " + rating + " estrellas para " + movie.getTitle());
                ((Stage) closeButton.getScene().getWindow()).close();
            });

            starsBox.getChildren().add(star);
        }

        userRatingBox.getChildren().add(starsBox);
    }

    @FXML
    private void onCloseButtonClick() {
        ((Stage) closeButton.getScene().getWindow()).close();
    }
} 