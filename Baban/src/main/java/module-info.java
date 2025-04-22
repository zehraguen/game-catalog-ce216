module com.example.baban {
    requires javafx.controls;
    requires javafx.fxml;
    requires json;


    opens com.example.baban to javafx.fxml;
    exports com.example.baban;
}