package com.example.petuflixx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;
    
    @FXML
    private Button loginButton;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    
    @FXML
    protected void initialize() {
        // Este método se llama automáticamente después de cargar el FXML
        // Asegurarse de que los botones estén habilitados
        if (loginButton != null) {
            loginButton.setDisable(false);
        }
    }

    @FXML
    protected void onLoginButtonClick() throws IOException {
        System.out.println("Se hizo clic en INICIAR SESIÓN");
        
        // Cargar la vista de login
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 950);
        
        // Obtener el Stage actual
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setTitle("PetuFlix | Login");
        stage.setScene(scene);
    }

    @FXML
    protected void onRegisterButtonClick() throws IOException {
        System.out.println("Se hizo clic en REGISTRARSE");
        
        // Cargar la vista de registro
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 950);
        
        // Obtener el Stage actual
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setTitle("PetuFlix | Registro");
        stage.setScene(scene);
    }
}