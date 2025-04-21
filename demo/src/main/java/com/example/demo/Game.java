package com.example.demo;

import java.util.ArrayList;

public class Game {
    private String title;
    private ArrayList <String> developer;
    private int releaseYear;
    private ArrayList <String> genre;
    private int id;
    private ArrayList <String> platform;
    private double playTime;
    private String image;

    public Game(String title, ArrayList<String> developer, int releaseYear, ArrayList<String> genre, int id, ArrayList <String> platform, double playTime, String image) {
        this.title = title;
        this.developer = developer;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.id = id;
        this.platform = platform;
        this.playTime = playTime;
        this.image = image;
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

    public int compareNames(Game g){
        return title.compareTo(g.getTitle());
    }

    public int compareYears(Game g){
        return releaseYear - g.getReleaseYear();
    }
}
