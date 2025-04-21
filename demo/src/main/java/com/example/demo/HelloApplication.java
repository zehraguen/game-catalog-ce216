package com.example.demo;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Random;

public class HelloApplication extends Application {
    private final Catalog catalog = new Catalog();
    private final ListView<String> gameView = new ListView<>();

    @Override
    public void start(Stage stage) throws IOException {

        VBox mainLayOut = new VBox();
        mainLayOut.setAlignment(Pos.TOP_CENTER);

        //-------------------------------------------------
        Menu fileMenu = new Menu(("File"));
        fileMenu.getItems().add(new MenuItem("Import"));
        fileMenu.getItems().add(new MenuItem("Export"));
        fileMenu.getItems().get(0).setOnAction(e -> handleImportFromDirectory());
        fileMenu.getItems().get(1).setOnAction(e -> exportGames());

        Menu filterGenre = new Menu("Genre");
        filterGenre.getItems().add(new MenuItem(""));

        Menu filterYear = new Menu(("Release Year"));
        ToggleGroup tg = new ToggleGroup();
        for(int i=2025; i>1989; i--){
            filterYear.getItems().add(new RadioMenuItem("" + i));
        }

        Menu filterMenu = new Menu("Filter");
        filterMenu.getItems().add(filterGenre);
        filterMenu.getItems().add(filterYear);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(filterMenu);
        menuBar.getMenus().add(new Menu("Help"));
        mainLayOut.getChildren().add(menuBar);

        Label sortLabel = new Label("Sorted by:");

        ComboBox<String> sortBox = new ComboBox<>();
        sortBox.getItems().addAll("Alphabetical", "Release year", "Play time");
        sortBox.setValue("Alphabetical");
        //sortBox.setOnAction(e -> sortGames("alphabetical"));
        sortBox.setOnAction(e -> {
            String selected = sortBox.getValue();
            String criteria = switch (selected) {
                case "Alphabetical" -> "alphabetical";
                case "Release year" -> "chronological";
                case "Play time" -> "playtime";
                default -> "alphabetical";
            };

            catalog.sortGames(criteria);
            gameView.setItems(getGameTitles()); // refresh the ListView
        });

        TextField searchField = new TextField();
        searchField.setPromptText("search");
        searchField.setOnMouseClicked(e -> searchField.setText(""));
        searchField.setMinHeight(30);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String s = searchField.getText();
            System.out.println(s);
        });

        HBox head = new HBox(8);
        head.setAlignment(Pos.CENTER);
        head.getChildren().addAll(sortLabel, sortBox, searchField, searchButton);
        head.setMinSize(10, 40);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        VBox.setMargin(head, new Insets(8));

        //-------------------------------------------------

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

        HBox end = new HBox(8);
        end.setAlignment(Pos.BOTTOM_RIGHT);
        end.getChildren().addAll(prevButton, nextButton);
        VBox.setMargin(end, new Insets(8));

        //-------------------------------------------------


        mainLayOut.getChildren().add(head);
        mainLayOut.getChildren().add(gameView);
        mainLayOut.getChildren().add(end);

        Scene scene = new Scene(mainLayOut, 600,
                450);
        stage.setTitle("Game Catalog");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    public ObservableList<String> getGameTitles() {
        ArrayList<String> titles = new ArrayList<>();
        for (Game game : catalog.getGameList()) {
            titles.add(game.getTitle());
        }
        return javafx.collections.FXCollections.observableArrayList(titles);
    }

    public void importGames() {
        catalog.importJson("src/main/resources/games_data.json");
        gameView.setItems(getGameTitles());
    }

    public void handleImportFromDirectory() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Game Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            catalog.importJson(file.getAbsolutePath());
            gameView.setItems(getGameTitles());
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



    public void sortGames(String criteria) {
        catalog.sortGames(criteria);
        //gameView.setItems(getGameTitles());
    }


    public static void helpMenu() {
        System.out.println("This is the help menu.");
    }
    public  static String getSearchText(TextField tf) { return tf.getText(); }
}