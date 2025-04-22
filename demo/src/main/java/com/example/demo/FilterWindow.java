package com.example.demo;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FilterWindow extends Stage {

    public FilterWindow(ArrayList<String> list, Label l) {

        ArrayList<ToggleButton> toggleButtons=new ArrayList<>();
        ArrayList<String> selectedFilters = new ArrayList<>();


        VBox root = new VBox(10);
        root.getChildren().add(new Label("The Filtering choices will be here"));
        VBox filterBox = new VBox(10);
        String[] filters={"RPG","Action","Co-op","Simulation","Strategy","Horror"};

        for (String f : filters) {
            ToggleButton btn = createSelectableButton(f);
            toggleButtons.add(btn);
            filterBox.getChildren().add(btn);
        }

        ScrollPane scrollPane=new ScrollPane(filterBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefHeight(200);

        Button applyButton = new Button("Apply");

        applyButton.setOnAction(event -> {
            selectedFilters.clear(); //reset for every choicing action again
            for(ToggleButton b: toggleButtons){
                if(b.isSelected()){
                    selectedFilters.add(b.getText().replace(" ✓",""));// without check mark
                }
            }

            String s=new String();
            for (String s1 : selectedFilters){
                s+=" "+s1;
            }

            l.setText(s);
            close();

        });

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
