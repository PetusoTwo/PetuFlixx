package com.example.petuflixx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    protected void onLoginButtonClick() {
        // Aquí iría la lógica de autenticación
        System.out.println("Intento de login con: " + emailField.getText());
    }
    
    @FXML
    protected void onBackButtonClick() throws IOException {
        // Volver a la pantalla principal
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 950);
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.setScene(scene);
    }
}