package com.example.demo;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FilterWindow extends Stage {

    public FilterWindow() {
        VBox root = new VBox(10);
        root.getChildren().add(new Label("The Filtering choices will be here"));
        VBox filterBox = new VBox(10);
        filterBox.getChildren().addAll(
                createSelectableButton("RPG"),
                createSelectableButton("Action"),
                createSelectableButton("Co-op"),
                createSelectableButton("Story"),
                createSelectableButton("Simulation"),
                createSelectableButton("Strategy"),
                createSelectableButton("Platform"),
                createSelectableButton("Horror"));

        ScrollPane scrollPane=new ScrollPane(filterBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefHeight(200);

        Button applyButton = new Button("Apply");

        root.getChildren().addAll(scrollPane,applyButton);


        Scene scene = new Scene(root, 300, 200);
        setTitle("Filter");
        setScene(scene);
    }
    private ToggleButton createSelectableButton(String text) {
        ToggleButton button = new ToggleButton(text);
        button.setOnAction(e -> {
            if (button.isSelected()) {
                button.setText("✓ " + text);
            } else {
                button.setText(text);
            }
        });
        return button;
    }

}
