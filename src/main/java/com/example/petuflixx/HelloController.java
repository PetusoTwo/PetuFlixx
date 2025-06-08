package com.example.petuflixx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        // Asegurarse de que los botones estén habilitados
        if (loginButton != null) {
            loginButton.setDisable(false);
            loginButton.setDefaultButton(true);
        }
        if (registerButton != null) {
            registerButton.setDisable(false);
        }
    }

    @FXML
    protected void onLoginButtonClick() throws IOException {
        System.out.println("Navegando a la pantalla de login");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 950);
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setTitle("PetuFlix | Login");
        stage.setScene(scene);
    }

    @FXML
    protected void onRegisterButtonClick() throws IOException {
        System.out.println("Navegando a la pantalla de registro");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 950);
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setTitle("PetuFlix | Registro");
        stage.setScene(scene);
    }
}