module com.example.ejercicioh {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.ejercicioh to javafx.fxml;
    exports com.example.ejercicioh;
}