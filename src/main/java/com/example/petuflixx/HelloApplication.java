package com.example.petuflixx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloApplication extends Application {
    private static final Logger logger = Logger.getLogger(HelloApplication.class.getName());

    @Override
    public void start(Stage stage) {
        try {
            logger.info("Iniciando aplicación PetuFlix");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/petuflixx/hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            
            stage.setTitle("PetuFlix | Sistema de Películas");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
            
            logger.info("Aplicación iniciada correctamente");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al iniciar la aplicación", e);
            throw new RuntimeException("Error al cargar la interfaz de usuario", e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}