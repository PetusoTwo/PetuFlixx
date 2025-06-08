package com.example.petuflixx.controllers;

import com.example.petuflixx.database.UserDAO;
import com.example.petuflixx.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController {
    private static final Logger logger = Logger.getLogger(RegisterController.class.getName());
    private UserDAO userDAO;

    @FXML
    private TextField nameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        userDAO = new UserDAO();
        logger.info("RegisterController inicializado");
    }

    @FXML
    private void onRegisterButtonClick() {
        logger.info("Iniciando proceso de registro");
        
        // Validar campos
        if (!validateFields()) {
            return;
        }

        // Crear objeto User
        User user = new User(
            nameField.getText().trim(),
            emailField.getText().trim(),
            passwordField.getText()
        );

        // Intentar registrar usuario
        boolean registroExitoso = userDAO.registerUser(user);
        
        if (registroExitoso) {
            logger.info("Usuario registrado exitosamente");
            showAlert(Alert.AlertType.INFORMATION, "Registro exitoso", "Usuario registrado correctamente");
            navigateToLogin();
        } else {
            logger.severe("Error al registrar usuario - registroExitoso es false");
            showAlert(Alert.AlertType.ERROR, "Error de registro", "No se pudo registrar el usuario");
        }
    }

    private boolean validateFields() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error de validación", "Todos los campos son obligatorios");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error de validación", "Las contraseñas no coinciden");
            return false;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Error de validación", "El formato del email no es válido");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/petuflixx/login-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al navegar a la pantalla de login", e);
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la pantalla de login");
        }
    }

    @FXML
    private void onBackButtonClick() {
        navigateToLogin();
    }
} 