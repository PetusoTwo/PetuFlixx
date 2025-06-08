package com.example.petuflixx.controllers;

import com.example.petuflixx.database.UserDAO;
import com.example.petuflixx.models.User;
import com.example.petuflixx.utils.NavigationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.logging.Logger;

public class RegisterController {
    private static final Logger logger = Logger.getLogger(RegisterController.class.getName());
    private final UserDAO userDAO = new UserDAO();

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private TextField confirmPasswordField;
    @FXML private TextField nameField;
    @FXML private Button registerButton;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        logger.info("RegisterController inicializado");
    }

    @FXML
    private void onRegisterButtonClick() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String name = nameField.getText().trim();

        // Validaciones
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
            showError("Por favor, complete todos los campos");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Por favor, ingrese un email válido");
            return;
        }

        if (password.length() < 6) {
            showError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Las contraseñas no coinciden");
            return;
        }

        try {
            // Crear nuevo usuario
            User newUser = userDAO.createUser(username, email, password, name);
            logger.info("Usuario registrado exitosamente: " + username);
            
            // Mostrar mensaje de éxito
            showSuccess("Usuario registrado exitosamente");
            
            // Navegar a la pantalla de login
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            NavigationUtils.navigateTo("/com/example/petuflixx/hello-view.fxml", currentStage);
        } catch (SQLException e) {
            logger.severe("Error al registrar usuario: " + e.getMessage());
            showError(e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    @FXML
    private void onBackButtonClick() {
        NavigationUtils.navigateTo("/com/example/petuflixx/hello-view.fxml", (Stage) backButton.getScene().getWindow());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 