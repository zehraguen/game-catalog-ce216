package org.example.gamecatalogapp;

import java.util.ArrayList;

public class Game {
    private String title;
    private ArrayList<String> developer;
    private int releaseYear;
    private ArrayList<String> genre;
    private int id;
    private ArrayList<String> platform;
    private double playTime;
    private String image;
    private boolean localized;
    private String country;

    public boolean isLocalized() {
        return localized;
    }

    public void setLocalized(boolean localized) {
        this.localized = localized;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTranslators() {
        return translators;
    }

    public void setTranslators(String translators) {
        this.translators = translators;
    }

    public String getDubstudios() {
        return dubberstudio;
    }

    public void setDubstudios(String dubstudios) {
        this.dubberstudio = dubstudios;
    }

    private String translators;
    private String dubberstudio;


    public Game(String title, ArrayList<String> developer, int releaseYear, ArrayList<String> genre, int id, ArrayList<String> platform, double playTime, String image, boolean localized, String country, String translators, String dubstudios) {
        this.title = title;
        this.developer = developer;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.id = id;
        this.platform = platform;
        if(playTime>100){
            playTime=playTime%100;
            if(playTime<10) playTime=playTime+12;
        }


        this.playTime = playTime;
        this.image = image;
        this.localized = localized;
        this.country = country;
        this.translators = translators;
        this.dubberstudio = dubstudios;
    }

    public ArrayList<String> getDeveloper() {
        return developer;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<String> getPlatform() {
        return platform;
    }

    public double getPlayTime() {
        return playTime;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public void setDeveloper(ArrayList<String> developer) {
        this.developer = developer;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPlatform(ArrayList<String> platform) {
        this.platform = platform;
    }

    public void setPlayTime(double playTime) {
        this.playTime = playTime;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int compareNames(Game g) {
        return title.compareTo(g.getTitle());
    }

    public int compareYears(Game g) {
        return releaseYear - g.getReleaseYear();
    }

    public int comparePlayTime(Game g) {
        return Double.compare(g.getPlayTime(), playTime);
    }

}