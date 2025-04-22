package com.example.baban;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.json.*;

public class Catalog {
    private ArrayList<Game> gameList;
    private ArrayList<Game> specificGameList;

    public Catalog() {
        gameList = new ArrayList<>();
        specificGameList = new ArrayList<>();
    }

    public ArrayList<Game> getGameList() {
        return gameList;
    }
    public ArrayList<Game> getSpecificGameList(){return specificGameList;}

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

            case "playtime":
                gameList.sort(Game::comparePlayTime);
                break;

            default:
                gameList.sort(Game::compareNames);
        }
    }
    public void listSpecificGames(String sFilters) {
        specificGameList.clear();
        ArrayList<String> filters = new ArrayList<>();
        String[] filtersss = sFilters.split(" ");
        for(String s:filtersss){
            filters.add(s);
        }


        System.out.println("filters: " + filters);

        // If no filters are selected, just return the full list
        if (filters == null || filters.isEmpty() || (filters.size() == 1 && filters.get(0).isBlank())) {
            specificGameList.addAll(gameList);
            return;
        }

        ArrayList<String> selectedGenres = new ArrayList<>();
        ArrayList<String> selectedYearRanges = new ArrayList<>();

        boolean genreSection = true;
        for (String filter : filters) {
            filter.trim();
            System.out.println("FİLTERRR "+filter);
            if (filter.equals("//")) {
                genreSection = false;
            } else if (genreSection) {
                selectedGenres.add(filter);
            } else {
                selectedYearRanges.add(filter);
            }
        }

        System.out.println("here" + selectedGenres);
        System.out.println(selectedYearRanges);

        for (Game game : gameList) {
            boolean genreMatches = selectedGenres.isEmpty();
            for (String genre : selectedGenres) {
                if (game.getGenre().contains(genre)) {
                    genreMatches = true;
                    break;
                }
            }

            boolean yearMatches = selectedYearRanges.isEmpty();
            for (String range : selectedYearRanges) {
                String[] parts = range.split("-");
                if (parts.length == 2) {
                    int min = Integer.parseInt(parts[0]);
                    int max = Integer.parseInt(parts[1]);
                    int year = game.getReleaseYear();
                    if (year >= min && year <= max) {
                        yearMatches = true;
                        break;
                    }
                }
            }

            if (genreMatches && yearMatches) {
                specificGameList.add(game);
            }
        }
    }

    public void helpMenu() {
        // Method implementation goes here
    }
}
