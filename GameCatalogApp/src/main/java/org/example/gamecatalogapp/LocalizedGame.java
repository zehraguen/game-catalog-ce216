package org.example.gamecatalogapp;

import java.util.ArrayList;

public class LocalizedGame extends Game {
    private String country;
    private ArrayList<String> translators;
    private ArrayList<String> dubbers;
    private String description;

    public LocalizedGame(String title, ArrayList<String> developer, int releaseYear, ArrayList<String> genre, int id, ArrayList<String> platform, double playTime, String image, String country, ArrayList<String> translators, ArrayList<String> dubbers, String description) {
        super(title, developer, releaseYear, genre, id, platform, playTime, image);
        this.country = country;
        this.translators = translators;
        this.dubbers = dubbers;
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public ArrayList<String> getTranslators() {
        return translators;
    }

    public ArrayList<String> getDubbers() {
        return dubbers;
    }

    public String getDescription() {
        return description;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTranslators(ArrayList<String> translators) {
        this.translators = translators;
    }

    public void setDubbers(ArrayList<String> dubbers) {
        this.dubbers = dubbers;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
