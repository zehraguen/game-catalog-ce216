package org.example.gamecatalogapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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

        MenuBar menuBar = new MenuBar(fileMenu, new Menu("Help"));
        mainLayOut.getChildren().add(menuBar);

        Label filterLabel = new Label("None");
        Button filterButton = new Button("   Filters:  ");

        ArrayList<String> selectedFilters=new ArrayList<>();

        filterButton.setOnAction(e -> {
            //FilterWindow fw = new FilterWindow(filterLabel);
            filterWindow(filterLabel);
        });




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
            gameTable.setItems(FXCollections.observableArrayList(catalog.getSpecificGameList()));
        });

        TextField searchField = new TextField();
        searchField.setPromptText("search");
        searchField.setMinHeight(30);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            catalog.searchSpecificGames(searchField.getText());
            gameTable.setItems(FXCollections.observableArrayList(catalog.getSpecificGameList()));
        });

        HBox head = new HBox(8, sortLabel, sortBox, searchField, searchButton);
        head.setAlignment(Pos.CENTER);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        VBox.setMargin(head, new javafx.geometry.Insets(8));
        mainLayOut.getChildren().add(head);





        HBox filters=new HBox(filterButton,filterLabel);
        filters.setAlignment(Pos.CENTER_LEFT);
        filters.setSpacing(8);
        mainLayOut.getChildren().add(filters);






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

        HBox end = new HBox(8);
        end.setAlignment(Pos.BOTTOM_RIGHT);
        Button addGameButton = new Button("Add Game");
        addGameButton.setOnAction(e -> {
            showAddGameWindow();
        });
        end.getChildren().add( addGameButton );
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
                gameTable.setItems(FXCollections.observableArrayList(catalog.getSpecificGameList()));
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

    public void showGameDetails(Game game) {
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

        Scene scene = new Scene(layout, 400, 460);
        detailStage.setScene(scene);
        detailStage.show();
    }

    public void filterWindow(Label l) {

        Stage filterStage = new Stage();
        filterStage.setTitle("Filter Window");

        ArrayList<String> list = new ArrayList<>();
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

            catalog.listSpecificGames(l.getText());
            gameTable.setItems(FXCollections.observableArrayList(catalog.getSpecificGameList()));

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
        filterStage.setScene(scene);
        filterStage.show();
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



    private void showAddGameWindow() {
        Stage addStage = new Stage();
        addStage.setTitle("Add New Game");

        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(15));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField developerField = new TextField();
        developerField.setPromptText("Developer(s), comma separated");

        TextField yearField = new TextField();
        yearField.setPromptText("Release Year");

        TextField genreField = new TextField();
        genreField.setPromptText("Genre(s), comma separated");

        TextField playtimeField = new TextField();
        playtimeField.setPromptText("Playtime (in hours)");

        TextField platformField = new TextField();
        platformField.setPromptText("Platform(s), comma separated");

        TextField imageUrlField = new TextField();
        imageUrlField.setPromptText("Image URL (optional)");

        Button addButton = new Button("Add Game");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        addButton.setOnAction(e -> {
            try {
                String title = titleField.getText().trim();
                ArrayList<String> developers = new ArrayList<>();
                for (String dev : developerField.getText().trim().split(",")) {
                    if (!dev.isBlank()) developers.add(dev.trim());
                }

                int releaseYear = Integer.parseInt(yearField.getText().trim());

                ArrayList<String> genres = new ArrayList<>();
                for (String g : genreField.getText().trim().split(",")) {
                    if (!g.isBlank()) genres.add(g.trim());
                }

                double playtime = Double.parseDouble(playtimeField.getText().trim());

                ArrayList<String> platforms = new ArrayList<>();
                for (String p : platformField.getText().trim().split(",")) {
                    if (!p.isBlank()) platforms.add(p.trim());
                }

                String image = imageUrlField.getText().trim();
                if (image.isEmpty()) {
                    image = "https://via.placeholder.com/300";
                }

                // Generate new ID
                int maxId = catalog.getGameList().stream().mapToInt(Game::getId).max().orElse(0);
                int newId = maxId + 1;

                Game newGame = new Game(title, developers, releaseYear, genres, newId, platforms, playtime, image);
                catalog.getGameList().add(newGame);
                gameTable.setItems(FXCollections.observableArrayList(catalog.getGameList()));
                addStage.close();
            } catch (Exception ex) {
                errorLabel.setText("Invalid input. Please check the fields.");
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(
                new Label("Add a New Game"),
                titleField,
                developerField,
                yearField,
                genreField,
                playtimeField,
                platformField,
                imageUrlField,
                addButton,
                errorLabel
        );

        Scene scene = new Scene(root, 400, 450);
        addStage.setScene(scene);
        addStage.show();
    }


}