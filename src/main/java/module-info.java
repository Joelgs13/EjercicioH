module com.example.ejercicioh {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ejercicioh to javafx.fxml;
    exports com.example.ejercicioh;
}