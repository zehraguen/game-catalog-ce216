package com.example.demo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.json.*;

public class Catalog {
    private ArrayList<Game> gameList;



    // Methods (empty for now)
    public void addGame(Game game) {
        gameList.add(game);
    }

    public void deleteGame(int id) {
        Game dg = null;
        for(Game g : gameList){
            if(g.getId() == id){
                dg = g;
            }
        }
        gameList.remove(dg);
    }
    
    public void deleteGame(Game game) {
        gameList.remove(game);
    }

    public void editGame(Game game) {
        // Method implementation goes here
    }

    public void importJson(String filePath) {
        gameList = new ArrayList<>();
        try {
            String content = new String(java.nio.file.Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String title = obj.getString("title");
                ArrayList<String> developer = jsonArrayToList(obj.getJSONArray("developer"));
                int releaseYear = obj.getInt("releaseYear");
                ArrayList<String> genre = jsonArrayToList(obj.getJSONArray("genre"));
                int id = obj.getInt("id");
                ArrayList<String> platform = jsonArrayToList(obj.getJSONArray("platform"));
                double playTime = obj.getDouble("playTime");
                String image = obj.getString("image");

                Game game = new Game(title, developer, releaseYear, genre, id, platform, playTime, image);
                gameList.add(game);
            }
        } catch (Exception e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }


    private ArrayList<String> jsonArrayToList(JSONArray jsonArray) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }

    public void exportJson(String filePath) {
        JSONArray jsonArray = new JSONArray();

        for (Game game : gameList) {
            JSONObject obj = new JSONObject();

            obj.put("title", game.getTitle());
            obj.put("developer", game.getDeveloper());
            obj.put("releaseYear", game.getReleaseYear());
            obj.put("genre", game.getGenre());
            obj.put("id", game.getId());
            obj.put("platform", game.getPlatform());
            obj.put("playTime", game.getPlayTime());
            obj.put("image", game.getImage());

            jsonArray.put(obj);
        }

        try {
            java.nio.file.Files.write(
                    Paths.get(filePath),
                    jsonArray.toString(4).getBytes()  // Pretty-print with 4-space indentation
            );
            System.out.println("Games exported successfully to: " + filePath);
        } catch (Exception e) {
            System.out.println("Error exporting JSON file: " + e.getMessage());
        }
    }




    public void listGames(String orderBy) {
        // Method implementation goes here
    }

    public void sortGames(String criteria) {
        switch (criteria){
            case "alphabetical":
                gameList.sort(Game::compareNames);
                break;

            case "chronological":
                gameList.sort(Game::compareYears);
                break;

            default:
                gameList.sort(Game::compareNames);
        }
    }

    public void helpMenu() {
        // Method implementation goes here
    }
}
