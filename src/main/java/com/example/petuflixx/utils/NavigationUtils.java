package com.example.petuflixx.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NavigationUtils {
    private static final Logger logger = Logger.getLogger(NavigationUtils.class.getName());

    public static void navigateTo(String fxmlPath, Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtils.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(NavigationUtils.class.getResource("/styles.css").toExternalForm());
            
            currentStage.setScene(scene);
            currentStage.setMaximized(true);
            currentStage.show();
            
            logger.info("Navegación exitosa a: " + fxmlPath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al navegar a: " + fxmlPath, e);
        }
    }
} 