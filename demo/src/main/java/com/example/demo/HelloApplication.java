package com.example.demo;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    private final Catalog catalog = new Catalog();
    private final TableView<Game> gameTable = new TableView<>();

    @Override
    public void start(Stage stage) throws IOException {
        VBox mainLayOut = new VBox();

        //-------------------------------------------------
        Menu fileMenu = new Menu("File");
        MenuItem importItem = new MenuItem("Import");
        MenuItem exportItem = new MenuItem("Export");
        importItem.setOnAction(e -> handleImportFromDirectory());
        exportItem.setOnAction(e -> exportGames());
        fileMenu.getItems().addAll(importItem, exportItem);

        Menu filterMenu = new Menu("Filter");
        MenuBar menuBar = new MenuBar(fileMenu, filterMenu, new Menu("Help"));
        mainLayOut.getChildren().add(menuBar);

        Label sortLabel = new Label("Sorted by:");
        ComboBox<String> sortBox = new ComboBox<>();
        sortBox.getItems().addAll("Alphabetical", "Release year", "Play time");
        sortBox.setValue("Alphabetical");
        sortBox.setOnAction(e -> {
            String criteria = switch (sortBox.getValue()) {
                case "Alphabetical" -> "alphabetical";
                case "Release year" -> "chronological";
                case "Play time" -> "playtime";
                default -> "alphabetical";
            };
            catalog.sortGames(criteria);
            gameTable.setItems(FXCollections.observableArrayList(catalog.getGameList()));
        });

        TextField searchField = new TextField();
        searchField.setPromptText("search");
        searchField.setMinHeight(30);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> System.out.println(searchField.getText()));

        HBox head = new HBox(8, sortLabel, sortBox, searchField, searchButton);
        head.setAlignment(javafx.geometry.Pos.CENTER);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        VBox.setMargin(head, new javafx.geometry.Insets(8));
        mainLayOut.getChildren().add(head);

        //-------------------------------------------------

        TableColumn<Game, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Game, Number> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getReleaseYear()));

        TableColumn<Game, Number> playtimeCol = new TableColumn<>("Playtime");
        playtimeCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPlayTime()));

        TableColumn<Game, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(data -> new SimpleStringProperty(String.join(", ", data.getValue().getGenre())));

        gameTable.getColumns().addAll(titleCol, yearCol, playtimeCol, genreCol);
        VBox.setVgrow(gameTable, Priority.ALWAYS);
        mainLayOut.getChildren().add(gameTable);

        HBox end = new HBox(8);
        end.setAlignment(javafx.geometry.Pos.BOTTOM_RIGHT);
        end.getChildren().addAll(new Button("prev"), new Button("next"));
        VBox.setMargin(end, new javafx.geometry.Insets(8));
        mainLayOut.getChildren().add(end);

        Scene scene = new Scene(mainLayOut, 600, 450);
        stage.setTitle("Game Catalog");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void handleImportFromDirectory() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Game Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            catalog.importJson(file.getAbsolutePath());
            gameTable.setItems(FXCollections.observableArrayList(catalog.getGameList()));
        }
    }

    public void exportGames() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Game Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setInitialFileName("games_export.json");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            catalog.exportJson(file.getAbsolutePath());
            System.out.println("Exported to: " + file.getAbsolutePath());
        }
    }
}