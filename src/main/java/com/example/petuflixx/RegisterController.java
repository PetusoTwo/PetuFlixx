package com.example.petuflixx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
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
    private Button registerButton;

    @FXML
    public void initialize() {
        // Asegurarse de que los botones estén habilitados
        if (registerButton != null) {
            registerButton.setDisable(false);
            registerButton.setDefaultButton(true);
        }
        if (backButton != null) {
            backButton.setDisable(false);
        }
        // Hacer que el campo de nombre sea el foco inicial
        nameField.requestFocus();
    }
    
    @FXML
    protected void onRegisterButtonClick() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if (password.equals(confirmPassword)) {
            System.out.println("Registro exitoso para: " + email);
            // Aquí iría la lógica para guardar el usuario
        } else {
            System.out.println("Las contraseñas no coinciden");
            // Aquí iría la lógica para mostrar un mensaje de error
        }
    }
    
    @FXML
    protected void onBackButtonClick() throws IOException {
        System.out.println("Volviendo a la pantalla principal desde registro");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 950);
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setTitle("PetuFlix | Sistema de Peliculas");
        stage.setScene(scene);
    }
} 