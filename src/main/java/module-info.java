module com.example.petuflixx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.petuflixx to javafx.fxml;
    exports com.example.petuflixx;
}