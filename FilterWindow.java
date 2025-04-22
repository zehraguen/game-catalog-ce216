package com.example.baban;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FilterWindow extends Stage {

    public ArrayList<String> list;

    public FilterWindow(Label l) {

        ArrayList<ToggleButton> toggleButtonsForGenre=new ArrayList<>();
        ArrayList<ToggleButton> toggleButtonsForReleaseYear=new ArrayList<>();
        ArrayList<String> selectedFilters = new ArrayList<>();


        VBox root = new VBox(10);
        VBox filterBox = new VBox(10);
        VBox filterBox2=new VBox(10);

        String[] filters={"RPG","Action","Co-op","Simulation","Strategy","Horror"};


        for (String f : filters) {
            ToggleButton btn = createSelectableButton(f);
            toggleButtonsForGenre.add(btn);
            filterBox.getChildren().add(btn);
        }


        ScrollPane scrollPaneForGenre = new ScrollPane(filterBox);
        scrollPaneForGenre.setPrefWidth(150);
        scrollPaneForGenre.setPrefHeight(200);


        String[] filters2={"1990-2000","2001-2005","2006-2010","2011-2020"};

        for (String f : filters2) {
            ToggleButton btn = createSelectableButton(f);
            toggleButtonsForReleaseYear.add(btn);
            filterBox2.getChildren().add(btn);
        }


        ScrollPane scrollPaneForReleaseYear = new ScrollPane(filterBox2);
        scrollPaneForGenre.setPrefWidth(150);
        scrollPaneForGenre.setPrefHeight(200);





        Button applyButton = new Button("Apply");

        applyButton.setOnAction(event -> {
            selectedFilters.clear(); //reset for every choicing action again

            for(ToggleButton b: toggleButtonsForGenre){
                if(b.isSelected()){
                    selectedFilters.add(b.getText());// without check mark
                }
            }
            selectedFilters.add(" // ");

            for(ToggleButton b: toggleButtonsForReleaseYear){
                if(b.isSelected()){
                    selectedFilters.add(b.getText());// without check mark
                }
            }



            String s=new String();
            for (String s1 : selectedFilters){
                s+=" "+s1;
            }





            l.setText(s);
            if(s.isBlank()){
                l.setText("None");
            }
            list = selectedFilters;
            close();

        });


        GridPane gp=new GridPane();
        Label genre=new Label("GENRES");
        Label relaseYear=new Label("RELASE YEAR");
        gp.add(genre,0,0); gp.add(scrollPaneForGenre,0,1);
        gp.add(relaseYear,1,0); gp.add(scrollPaneForReleaseYear,1,1);
        gp.setHgap(40);
        scrollPaneForGenre.setMinViewportWidth(100);
        gp.setAlignment(Pos.CENTER);
        genre.setAlignment(Pos.CENTER);

        list=selectedFilters;




        root.getChildren().addAll(gp, applyButton);




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

    public ArrayList<String> getList() {
        return list;
    }
}
