package com.example.petuflixx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button backButton;
    
    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        // Asegurarse de que los botones estén habilitados
        if (loginButton != null) {
            loginButton.setDisable(false);
            loginButton.setDefaultButton(true);
        }
        if (backButton != null) {
            backButton.setDisable(false);
        }
        // Hacer que el campo de email sea el foco inicial
        emailField.requestFocus();
    }
    
    @FXML
    protected void onLoginButtonClick() throws IOException {
        System.out.println("Intento de login con: " + emailField.getText());
        // Aquí iría la lógica de autenticación
        
        // Por ahora, redirigimos directamente a la vista principal
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 950);
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setTitle("PetuFlix | Principal");
        stage.setScene(scene);
    }
    
    @FXML
    protected void onBackButtonClick() throws IOException {
        System.out.println("Volviendo a la pantalla principal desde login");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 950);
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setTitle("PetuFlix | Sistema de Peliculas");
        stage.setScene(scene);
    }
}