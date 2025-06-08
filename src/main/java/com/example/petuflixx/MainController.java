package com.example.petuflixx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MainController {
    @FXML
    private TextField searchField;
    
    @FXML
    private Button profileButton;
    
    @FXML
    public void initialize() {
        // Inicialización de la vista principal
        System.out.println("Inicializando vista principal");
    }
    
    @FXML
    protected void onSearchButtonClick() {
        String searchText = searchField.getText();
        System.out.println("Buscando: " + searchText);
        // Aquí iría la lógica de búsqueda
    }
    
    @FXML
    protected void onProfileButtonClick() {
        System.out.println("Abriendo perfil de usuario");
        // Aquí iría la lógica para mostrar el perfil
    }
    
    @FXML
    protected void onCategoryButtonClick(javafx.event.ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String category = clickedButton.getText();
        System.out.println("Categoría seleccionada: " + category);
        // Aquí iría la lógica para filtrar por categoría
    }
    
    @FXML
    protected void onMovieCardClick(javafx.event.ActionEvent event) {
        VBox movieCard = (VBox) event.getSource();
        System.out.println("Película seleccionada");
        // Aquí iría la lógica para mostrar detalles de la película
    }
} 