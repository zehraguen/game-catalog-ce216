
import java.util.ArrayList;

public class Catalog {
    private ArrayList<Game> gameList;

    public static void main(String[] args) {
        
    }

    // Methods (empty for now)
    public void addGame(Game game) {
        gameList.add(game);
    }

    public void deleteGame(int id) {
        for(Game g:gameList){
            if(g.getId()==id){
                gameList.remove(g);
            }
        }
    }
    
    public void deleteGame(Game game) {
        gameList.remove(game);
    }

    public void editGame(Game game) {
        // Method implementation goes here
    }

    public void importJson(String filePath) {
        // Method implementation goes here
    }

    public void exportJson(String filePath) {
        // Method implementation goes here
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
