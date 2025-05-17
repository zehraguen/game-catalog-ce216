package org.example.gamecatalogapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
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
        mainLayOut.setStyle("-fx-background-color: #2c7da0;");

        //-------------------------------------------------
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");
        MenuItem help = new MenuItem("Instructions");
        helpMenu.getItems().add(help);
        help.setOnAction(e->showHelpMenu());
        MenuItem contact = new MenuItem("Contact");
        helpMenu.getItems().add(contact);
        contact.setOnAction(e -> showContact());
        MenuItem importItem = new MenuItem("Import");
        MenuItem exportItem = new MenuItem("Export");
        importItem.setOnAction(e -> handleImportFromDirectory(stage));
        exportItem.setOnAction(e -> exportGames(stage));
        fileMenu.getItems().addAll(importItem, exportItem);

        MenuBar menuBar = new MenuBar(fileMenu, helpMenu);
        menuBar.setStyle("""
                -fx-background-color: #9cc5d6;
                """);
        mainLayOut.getChildren().add(menuBar);

        Label filterLabel = new Label("None");
        filterLabel.setStyle("""
            -fx-background-color: #e67e22;
            -fx-text-fill: white;
            -fx-padding: 4 10 4 10;
            -fx-background-radius: 8;
            -fx-font-size: 13;
                    """);
        Button filterButton = new Button("  Filters:  ");

        filterButton.setStyle("-fx-background-color: #665f5b; -fx-font-size: 13; -fx-text-fill: white; -fx-background-radius: 6;");

        ArrayList<String> selectedFilters=new ArrayList<>();

        filterButton.setOnAction(e -> {
            //FilterWindow fw = new FilterWindow(filterLabel);
            filterWindow(filterLabel);
        });




        Label sortLabel = new Label("Sorted by:");
        sortLabel.setStyle("""
            -fx-background-color: #665f5b;
            -fx-font-size: 13;
            -fx-text-fill: white;
            -fx-padding: 4 10 4 10;
            -fx-background-radius: 6;
                """);
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
        VBox.setMargin(filters,new Insets(2,5,10,7));
        filters.setAlignment(Pos.CENTER_LEFT);
        filters.setSpacing(10);
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

        catalog.importJson("src/main/resources/games_data.json");
        gameTable.setItems(FXCollections.observableArrayList(catalog.getSpecificGameList()));

        Scene scene = new Scene(mainLayOut, 770, 600);
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

        VBox root = new VBox(15);
        root.setPadding(new Insets(0));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #2c7da0;");

        ImageView imageView = new ImageView(new Image(game.getImage(), true));
        imageView.setFitWidth(500);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setStyle("""
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 3);
""");

// infoBox
        VBox rightInfoBox = new VBox(8);
        rightInfoBox.setMaxWidth(Double.MAX_VALUE);
        rightInfoBox.setMaxHeight(Double.MAX_VALUE);
        rightInfoBox.setPadding(new Insets(20));
        rightInfoBox.setPrefWidth(500);
        rightInfoBox.setPrefHeight(1000);
        rightInfoBox.setStyle("""
    -fx-background-color: #f2f2f2;
    -fx-background-radius: 10;
    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3);
""");


        VBox theBox=new VBox(0);
        HBox genreBox = new HBox(5);
        genreBox.setPadding(new Insets(5, 0, 5, 0));

        for (String s : game.getGenre()) {
            Label tag = new Label(s);
            tag.setStyle("""
        -fx-background-color: #e67e22;
        -fx-text-fill: white;
        -fx-padding: 4 10 4 10;
        -fx-background-radius: 8;
        -fx-font-size: 13;
    """);
            genreBox.getChildren().add(tag);
        }
        Label title=new Label(game.getTitle());
        title.setStyle("""
                -fx-text-fill: black;
                 -fx-font-size: 23;
                
                 """);


        theBox.getChildren().addAll(genreBox);

        VBox leftInfoBox = new VBox(8);
        leftInfoBox.setPrefWidth(500);
        leftInfoBox.setPrefHeight(1000);
        leftInfoBox.setMaxWidth(Double.MAX_VALUE);
        leftInfoBox.setMaxHeight(Double.MAX_VALUE);
        leftInfoBox.setPadding(new Insets(20));
        leftInfoBox.setStyle("""
    -fx-background-color: #f2f2f2;
    -fx-background-radius: 10;
    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3);
""");



        HBox infoBoxes = new HBox(10);
        infoBoxes.setPadding(new Insets(0));
        infoBoxes.setPrefWidth(Double.MAX_VALUE);
        infoBoxes.setPrefHeight(275);
        infoBoxes.setPadding(new Insets(5, 5, 5, 10));


        infoBoxes.getChildren().addAll(rightInfoBox, leftInfoBox);



        Label developer = createLabel(" Developer: " + String.join(", ", game.getDeveloper()), 14, false);
        Label year = createLabel(" Release Year: " + game.getReleaseYear(), 14, false);
        Label platform = createLabel(" Platform: " + String.join(", ", game.getPlatform()), 14, false);
        Label playtime = createLabel(" Playtime: " + game.getPlayTime() + " hrs", 14, false);

        // EXplainin Area???
        boolean loc= game.isLocalized();
        String isloc="";
        if(loc) isloc="Localized";
        else isloc="Not Localized";

        Label descriptionLabel = createLabel(isloc, 14, true);
        Label descriptionText = createLabel("DESCRIPTION", 13, false);
        descriptionText.setWrapText(true);

        rightInfoBox.getChildren().addAll(developer, year, platform, playtime
        );
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        Button editButton = new Button("Edit Game");
        Button deleteButton = new Button("Delete Game");
        editButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 6;");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 6;");




        editButton.setOnAction(e->editGameWindow(game));




        deleteButton.setOnAction(e -> {
            catalog.deleteGame(game);
            gameTable.setItems(FXCollections.observableArrayList(catalog.getGameList()));
            detailStage.close();
        });
        buttonBox.getChildren().addAll(editButton,deleteButton);
        buttonBox.setSpacing(5);
        rightInfoBox.getChildren().addAll(spacer,buttonBox);
        leftInfoBox.getChildren().addAll(title,descriptionLabel,descriptionText);

        VBox.setMargin(theBox, new Insets(0, 0, 0, 10));
        VBox.setMargin(infoBoxes,new Insets(0,0,0,0));

        root.getChildren().addAll(imageView,theBox, infoBoxes);



        Scene scene = new Scene(root, 500, 600);
        detailStage.setScene(scene);
        detailStage.show();
    }

    private Label createLabel(String text, int fontSize, boolean bold) {
        Label label = new Label(text);
        label.setFont(Font.font(fontSize));
        if (bold) {
            label.setStyle("-fx-font-weight: bold;");
        }
        return label;
    }








    public void filterWindow(Label l) {

        Stage filterStage = new Stage();
        filterStage.setTitle("Filter Window");

        ArrayList<ToggleButton> toggleButtonsForGenre=new ArrayList<>();
        ArrayList<ToggleButton> toggleButtonsForReleaseYear=new ArrayList<>();
        ArrayList<String> selectedFilters = new ArrayList<>();


        VBox root = new VBox(15);
        root.setStyle("-fx-background-color: #f2f2f2;");
        root.setPadding(new Insets(15));

        Label genreLabel = new Label("GENRES");
        Label yearLabel = new Label("RELEASE YEARS");

        genreLabel.setStyle("""
        -fx-font-size: 16px;
        -fx-font-weight: bold;
        -fx-underline: true;
        -fx-text-fill: #37474F;
        """);
        yearLabel.setStyle("""
         -fx-font-size: 16px;
         -fx-font-weight: bold;
         -fx-underline: true;
         -fx-text-fill: #37474F;
         """);


        FlowPane genrePane = new FlowPane();
        genrePane.setHgap(10);
        genrePane.setVgap(10);
        genrePane.setPrefWrapLength(280);

        String[] filters={"Simulation", "Strategy", "Sports", "RPG", "Racing", "Puzzle", "Indie"," Casual", "Adventure","Action","Localized"};


        for (String f : filters) {
            ToggleButton btn = createSelectableButton(f);
            toggleButtonsForGenre.add(btn);
            btn.setStyle("""
            -fx-background-color: #e67e22;
            -fx-text-fill: white;
            -fx-padding: 4 10 4 10;
            -fx-background-radius: 8;
            -fx-font-size: 13;
                    """);
            genrePane.getChildren().add(btn);
        }


        FlowPane yearPane = new FlowPane();
        yearPane.setHgap(10);
        yearPane.setVgap(10);
        yearPane.setPrefWrapLength(280);



        String[] filters2={"1990-2000","2001-2005","2006-2010","2011-2020"};

        for (String f : filters2) {
            ToggleButton btn = createSelectableButton(f);
            toggleButtonsForReleaseYear.add(btn);
            btn.setStyle("""
        -fx-background-color: #e67e22;
        -fx-text-fill: white;
        -fx-padding: 4 10 4 10;
        -fx-background-radius: 8;
        -fx-font-size: 13;
                    """);
            yearPane.getChildren().add(btn);
        }







        Button applyButton = new Button("Apply");
        applyButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 6;");


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





        HBox applyBox = new HBox(applyButton);
        applyBox.setAlignment(Pos.BOTTOM_RIGHT);
        applyBox.setPadding(new Insets(10, 10, 10, 10));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        root.getChildren().addAll(genreLabel, genrePane, yearLabel, yearPane,spacer,applyBox
        );

        Scene scene = new Scene(root, 400, 350);
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


    private void editGameWindow(Game game) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Game");

        VBox edit = new VBox(10);
        edit.setPadding(new javafx.geometry.Insets(15));

        TextField titleField = new TextField(game.getTitle());
        titleField.setPromptText("Title");

        TextField developerField = new TextField(String.join(" , ",game.getDeveloper()));
        developerField.setPromptText("Developer(s), comma separated");

        TextField yearField = new TextField(String.valueOf(game.getReleaseYear()));
        yearField.setPromptText("Release Year");

        TextField genreField = new TextField(String.join(" , ",game.getGenre()));
        genreField.setPromptText("Genre(s), comma separated");

        TextField playtimeField = new TextField(String.valueOf(game.getPlayTime()));
        playtimeField.setPromptText("Playtime (in hours)");

        TextField platformField = new TextField(String.join(" , ",game.getPlatform()));
        platformField.setPromptText("Platform(s), comma separated");

        TextField imageUrlField = new TextField(game.getImage());
        imageUrlField.setPromptText("Image URL (optional)");

        Button saveEditedGame = new Button("Save edited game");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        saveEditedGame.setOnAction(e -> {
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

                game.setTitle(title);
                game.setDeveloper(developers);
                game.setReleaseYear(releaseYear);
                game.setGenre(genres);
                game.setId(newId);
                game.setPlatform(platforms);
                game.setPlayTime(playtime);
                game.setImage(image);

                gameTable.setItems(FXCollections.observableArrayList(catalog.getGameList()));
                editStage.close();
            } catch (Exception ex) {
                errorLabel.setText("Invalid input. Please check the fields.");
                ex.printStackTrace();
            }
        });
        edit.getChildren().addAll(
                new Label("Edit the game"),
                titleField,
                developerField,
                yearField,
                genreField,
                playtimeField,
                platformField,
                imageUrlField,
                saveEditedGame,
                errorLabel
        );

        Scene scene = new Scene(edit, 400, 450);
        editStage.setScene(scene);
        editStage.show();
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

                Game newGame = new Game(title, developers, releaseYear, genres, newId, platforms, playtime, image,false,null,null,null);
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

    private void showHelpMenu(){
        TextArea text = new TextArea();
        text.setStyle("-fx-control-inner-background: #9cc5d6;");
        text.setText("""
                    Upload Games
                        * Go to 'File' menu and then click the 'Import' button.
                        * Select the file which contains game information in JSON format. Now all
                          games are added to the catalog.
                          
                    Add Game
                        * Click the 'Add Game' button.
                        * Enter the attributes of the game you want to add.
                    
                    Find Games
                        * Type the full name or part of the game name in search bar and click
                           'search' button.
                        * Sort the games according to your preference.
                        * Use filters to limit the games with genre or release year.
                        * Click the game on the list to reach more information about the game.
                        
                    Delete Game
                        * Select the game you want to delete from list.
                        * Click the 'Delete Game' button.
                        
                    Edit Game
                        * Select the game you want to edit from the list.
                        * Click the 'Edit Game' button.
                        * Change the attributes accordingly.        
                                      
                    """);
        text.setEditable(false);
        Scene helpScene = new Scene(text,450, 300);
        Stage stage1=new Stage();
        stage1.setTitle("Help Menu");
        stage1.setScene(helpScene);
        stage1.show();
    }

    public void showContact(){
        TextArea contactText = new TextArea();
        contactText.setText("""
                Ali BULBUL          +90 530 094 30 04
                Ata Kemal INANC     +90 507 179 94 32
                Duru Lila BEKOGLU   +90 534 528 07 99
                Zehra GUN           +90 537 515 08 55""");
        contactText.setEditable(false);
        Scene contactScene = new Scene(contactText, 230, 80);
        Stage stage2 = new Stage();
        stage2.setScene(contactScene);
        stage2.show();
    }

}