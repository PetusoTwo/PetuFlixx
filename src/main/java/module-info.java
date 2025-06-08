module com.example.petuflixx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires transitive com.google.gson;

    opens com.example.petuflixx to javafx.fxml;
    opens com.example.petuflixx.controllers to javafx.fxml;
    opens com.example.petuflixx.api to javafx.fxml;
    
    exports com.example.petuflixx;
    exports com.example.petuflixx.controllers;
    exports com.example.petuflixx.api;
}