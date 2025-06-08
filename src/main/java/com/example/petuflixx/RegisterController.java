package com.example.petuflixx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    protected void onRegisterButtonClick() {
        // Aquí iría la lógica de registro
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
        // Volver a la pantalla principal
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 950);
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.setScene(scene);
    }
} 