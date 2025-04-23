module org.example.gamecatalogapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires json;

    opens org.example.gamecatalogapp to javafx.fxml;
    exports org.example.gamecatalogapp;
}
