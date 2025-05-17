package org.example.gamecatalogapp;

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
        specificGameList.remove(game);
    }

    public void editGame(Game game) {
        // Method implementation goes here
    }

    public void importJson(String filePath) {
        gameList = new ArrayList<>();
        specificGameList = new ArrayList<>();

        Set<Integer> seenIds = new HashSet<>();  // to track already added IDs

        try {
            String content = new String(java.nio.file.Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int id = obj.getInt("id");

                if (seenIds.contains(id)) {
                    continue; // skip duplicate
                }
                seenIds.add(id); // mark this ID as seen

                String title = obj.getString("title");
                ArrayList<String> developer = jsonArrayToList(obj.getJSONArray("developer"));
                int releaseYear = obj.getInt("releaseYear");
                ArrayList<String> genre = jsonArrayToList(obj.getJSONArray("genre"));
                ArrayList<String> platform = jsonArrayToList(obj.getJSONArray("platform"));
                double playTime = obj.getDouble("playTime");
                String image = obj.getString("image");
                boolean localized=obj.getBoolean("localized");
                String country=obj.getString("country");
                String translators=obj.getString("translators");
                String dubertudio=obj.getString("duberstudio");

                boolean isloc=false;
                //if(localized.equalsIgnoreCase("true")) isloc=true;




                Game game = new Game(title, developer, releaseYear, genre, id, platform, playTime, image, localized,country,translators,dubertudio);

                gameList.add(game);
                specificGameList.add(game);
            }

            this.sortGames("alphabetical");

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
                specificGameList.sort(Game::compareNames);
                break;

            case "chronological":
                gameList.sort(Game::compareYears);
                specificGameList.sort(Game::compareYears);
                break;

            case "playtime":
                gameList.sort(Game::comparePlayTime);
                specificGameList.sort(Game::comparePlayTime);
                break;

            default:
                gameList.sort(Game::compareNames);
                specificGameList.sort(Game::compareNames);
        }
    }
    public void listSpecificGames(String sFilters) {
        specificGameList.clear();

        ArrayList<String> filters = new ArrayList<>();
        String[] filtersss = sFilters.split(" ");

        for(String s:filtersss){
            if(!s.contains("✓") && !s.isBlank())filters.add(s);
        }

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
            if (filter.equals("//")) {
                genreSection = false;
            } else if (genreSection) {
                selectedGenres.add(filter);
            } else {
                selectedYearRanges.add(filter);
            }
        }

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

    public void searchSpecificGames(String searchText) {
        specificGameList.clear();

        for (Game game : gameList) {
            if (game.getTitle().toLowerCase().startsWith(searchText.toLowerCase())) {
                specificGameList.add(game);
            }
        }
        for (Game game : gameList) {
            if (game.getTitle().toLowerCase().contains(searchText.toLowerCase())&&!game.getTitle().toLowerCase().startsWith(searchText.toLowerCase())) {
                specificGameList.add(game);
            }
        }
    }
}
