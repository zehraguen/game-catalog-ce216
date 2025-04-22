package com.example.demo;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FilterWindow extends Stage {

    public FilterWindow() {
        VBox root = new VBox(10);
        root.getChildren().add(new Label("The Filtering choices will be here"));

        Scene scene = new Scene(root, 300, 200);
        setTitle("Filter");
        setScene(scene);
    }
}
