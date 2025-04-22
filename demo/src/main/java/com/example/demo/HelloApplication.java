package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        importItem.setOnAction(e -> handleImportFromDirectory(stage));
        exportItem.setOnAction(e -> exportGames(stage));
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

        gameTable.setRowFactory(tv -> {
            TableRow<Game> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    Game clickedGame = row.getItem();
                    showGameDetails(clickedGame);
                }
            });
            return row;
        });

<<<<<<< HEAD
        HBox head = new HBox(8);
        head.setAlignment(Pos.CENTER);
        head.getChildren().addAll(sortLabel, sortBox, searchField, searchButton);
        head.setMinSize(10, 40);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        VBox.setMargin(head, new Insets(8));

        //-------------------------------------------------

<<<<<<< Updated upstream
=======
        Label filterLabel = new Label("None");
        Button filterButton = new Button("   Filters:  ");

        ArrayList<String> selectedFilters=new ArrayList<>();

        filterButton.setOnAction(e -> {
            FilterWindow fw = new FilterWindow(selectedFilters, filterLabel);
            fw.show();

        });

        HBox filterBox = new HBox(8);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.getChildren().addAll(filterButton, filterLabel);

        VBox.setMargin(filterBox, new Insets(8));
        //-------------------------------------------------

        ListView gameView = new ListView();
>>>>>>> Stashed changes
        VBox.setVgrow(gameView, Priority.ALWAYS);

        GridPane[] games = new GridPane[9];
        for(int i=0; i<9; i++) {
            games[i] = new GridPane();
            games[i].add(new Label("game "+ i), 0, 0);
        }

        GridPane gp = new GridPane();
        VBox.setVgrow(gp, Priority.ALWAYS);
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(20);
        gp.setVgap(16);

        gp.add(games[0], 0, 0);
        gp.add(games[1], 1, 0);
        gp.add(games[2], 2, 0);
        gp.add(games[3], 0, 1);
        gp.add(games[4], 1, 1);
        gp.add(games[5], 2,1);
        gp.add(games[6], 0, 2);
        gp.add(games[7], 1, 2);
        gp.add(games[8], 2, 2);

        //-------------------------------------------------

        Button prevButton = new Button("prev");
        Button nextButton = new Button("next");

=======
>>>>>>> f169d9908424efc8f3fbca6e03280d43208db3c1
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

    public void handleImportFromDirectory(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Game Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        Platform.runLater(() -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                catalog.importJson(file.getAbsolutePath());
                gameTable.setItems(FXCollections.observableArrayList(catalog.getGameList()));
            }
        });
    }

    public void exportGames(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Game Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setInitialFileName("games_export.json");
        Platform.runLater(() -> {
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                catalog.exportJson(file.getAbsolutePath());
                System.out.println("Exported to: " + file.getAbsolutePath());
            }
        });
    }

    private void showGameDetails(Game game) {
        Stage detailStage = new Stage();
        detailStage.setTitle(game.getTitle());

        VBox layout = new VBox(10);
        layout.setPadding(new javafx.geometry.Insets(15));

        ImageView imageView = new ImageView(new Image(game.getImage(), true));
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        Label title = new Label("Title: " + game.getTitle());
        Label developer = new Label("Developer: " + String.join(", ", game.getDeveloper()));
        Label year = new Label("Release Year: " + game.getReleaseYear());
        Label genre = new Label("Genre: " + String.join(", ", game.getGenre()));
        Label platform = new Label("Platform: " + String.join(", ", game.getPlatform()));
        Label playtime = new Label("Playtime: " + game.getPlayTime() + " hours");

        layout.getChildren().addAll(imageView, title, developer, year, genre, platform, playtime);

        Scene scene = new Scene(layout, 400, 500);
        detailStage.setScene(scene);
        detailStage.show();
    }
}