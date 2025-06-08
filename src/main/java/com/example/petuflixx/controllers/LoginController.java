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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    private UserDAO userDAO;

    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        userDAO = new UserDAO();
        logger.info("LoginController inicializado");
    }

    @FXML
    private void onLoginButtonClick() {
        logger.info("Iniciando proceso de login");
        
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error de validación", "Por favor complete todos los campos");
            return;
        }

        // Buscar usuario en la base de datos
        User user = userDAO.getUserByEmail(email);
        
        if (user == null) {
            logger.warning("Usuario no encontrado: " + email);
            showAlert(Alert.AlertType.ERROR, "Error de autenticación", "Usuario no registrado");
            return;
        }

        // Verificar contraseña
        if (!user.getPassword().equals(password)) {
            logger.warning("Contraseña incorrecta para el usuario: " + email);
            showAlert(Alert.AlertType.ERROR, "Error de autenticación", "Contraseña incorrecta");
            return;
        }

        logger.info("Login exitoso para el usuario: " + email);
        navigateToMain(user.getName());
    }

    private void navigateToMain(String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/petuflixx/main-view.fxml"));
            Parent root = loader.load();
            
            // Obtener el controlador y establecer el nombre de usuario
            MainController mainController = loader.getController();
            mainController.setCurrentUser(userName);
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al navegar a la pantalla principal", e);
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la pantalla principal");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void onRegisterButtonClick() {
        Stage currentStage = (Stage) registerButton.getScene().getWindow();
        NavigationUtils.navigateTo("/com/example/petuflixx/register-view.fxml", currentStage);
    }
} 