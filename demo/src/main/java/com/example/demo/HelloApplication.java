package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        /*
        VBox vbox = new VBox();
        HBox head = new HBox(8);
        vbox.setAlignment(Pos.CENTER);

        Label label = new Label("Information");
        TextField tf = new TextField();
        HBox.setHgrow(tf, Priority.ALWAYS);
        Button benjamin = new Button("add");

        ListView listView = new ListView<>();
        VBox.setVgrow(listView, Priority.ALWAYS);

        HBox end = new HBox(8);
        end.setAlignment(Pos.CENTER_RIGHT);
        Button button = new Button("OK");
        end.getChildren().add(button);

        VBox.setMargin(head, new Insets(8));
        VBox.setMargin(listView, new Insets(8));
        VBox.setMargin(end, new Insets(8));

        head.getChildren().addAll(
                label, tf, benjamin
        );

        vbox.getChildren().addAll(
                head, listView, end
        );

        Scene scene = new Scene(vbox, 400,
                300);
        stage.setTitle("CE216 - Layouts");
        stage.setScene(scene);
        stage.show();
         */


        VBox mainLayOut = new VBox();
        mainLayOut.setAlignment(Pos.TOP_CENTER);

        //-------------------------------------------------

        Button sortButton = new Button("Listed by:");
        Label sortLabel = new Label("Alphabetical");
        TextField searchField = new TextField();
        searchField.setText("search");
        searchField.setOnMouseClicked(e -> searchField.setText(""));
        Button searchButton = new Button("Search");

        sortButton.setOnAction(e -> {
            if (sortLabel.getText()=="Alphabetical") {
                sortByYears();
                sortLabel.setText("Release year");
            }
            else {
                sortByAlphabeticalOrder();
                sortLabel.setText("Alphabetical");
            }
        });

        searchButton.setOnAction(e -> {
            String s = searchField.getText();
            System.out.println(s);
        });

        HBox head = new HBox(8);
        head.setAlignment(Pos.CENTER);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        head.getChildren().addAll( sortButton, sortLabel, searchField, searchButton);

        VBox.setMargin(head, new Insets(8));

        //-------------------------------------------------

        Label filterLabel = new Label("None");
        Button filterButton = new Button("   Filters:  ");
        filterButton.setOnAction(e -> filterLabel.setText(filterLabel.getText() + " criteria"));

        HBox filterBox = new HBox(8);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.getChildren().addAll(filterButton, filterLabel);

        VBox.setMargin(filterBox, new Insets(8));
        //-------------------------------------------------

        ListView gameView = new ListView();
        VBox.setVgrow(gameView, Priority.ALWAYS);

        //-------------------------------------------------

        Button helpButton = new Button("Help");
        helpButton.setOnAction(e -> helpMenu());
        Button prevButton = new Button("prev");
        Button nextButton = new Button("next");

        HBox end = new HBox(8);
        end.setAlignment(Pos.BOTTOM_RIGHT);
        end.getChildren().addAll(helpButton, prevButton, nextButton);
        VBox.setMargin(end, new Insets(8));

        //-------------------------------------------------


        mainLayOut.getChildren().add(head);
        mainLayOut.getChildren().add(filterBox);
        mainLayOut.getChildren().add(gameView);
        mainLayOut.getChildren().add(end);

        Scene scene = new Scene(mainLayOut, 400,
                300);
        stage.setTitle("Game Catalog");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }

    public static void helpMenu() {
        System.out.println("This is the help menu.");
    }
    public static void sortByYears() {
        System.out.println("Sorted by release years.");
    }
    public static void sortByAlphabeticalOrder() {
        System.out.println("Sorted by alphabetical order.");
    }
    public  static String getSearchText(TextField tf) { return tf.getText(); }
}
