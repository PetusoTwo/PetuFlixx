package com.example.petuflixx.controllers;

import com.example.petuflixx.database.UserDAO;
import com.example.petuflixx.models.User;
import com.example.petuflixx.utils.NavigationUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Logger;

public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    private final UserDAO userDAO = new UserDAO();

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML
    public void initialize() {
        logger.info("LoginController inicializado");
    }

    @FXML
    private void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Por favor, complete todos los campos");
            return;
        }

        try {
            User user = userDAO.getUser(username);
            if (user != null && user.getPassword().equals(password)) {
                logger.info("Usuario " + username + " ha iniciado sesión");
                navigateToMain(user.getName(), user.getId());
            } else {
                showError("Usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            logger.severe("Error al iniciar sesión: " + e.getMessage());
            showError("Error al iniciar sesión");
        }
    }

    @FXML
    private void onRegisterButtonClick() {
        logger.info("Navegando a la pantalla de registro");
        NavigationUtils.navigateTo("/com/example/petuflixx/register-view.fxml", (Stage) registerButton.getScene().getWindow());
    }

    private void navigateToMain(String userName, int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/petuflixx/main-view.fxml"));
            Parent root = loader.load();
            MainController mainController = loader.getController();
            mainController.setCurrentUser(userName, userId);

            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            currentStage.setScene(scene);
            currentStage.setMaximized(true);
        } catch (IOException e) {
            logger.severe("Error al cargar la vista principal: " + e.getMessage());
            showError("Error al cargar la vista principal");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 