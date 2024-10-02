package com.example.movieparadise.Model;

public class SliderSide {
    String Movie_Type;
    String Thumbnail_URL;
    String category;
    String comedian;
    String director;
    String hero;
    String heroine;
    String movie_description;
    String movie_name;
    String music;
    String production;
    String year;
    String videoUrl;
    public SliderSide() {
    }
    public SliderSide(String movie_Type, String thumbnail_URL, String category, String comedian, String director, String hero, String heroine,
                      String movie_description, String movie_name, String music, String production, String videoUrl,String year) {
        Movie_Type = movie_Type;
        Thumbnail_URL = thumbnail_URL;
        this.category = category;
        this.comedian = comedian;
        this.director = director;
        this.hero = hero;
        this.heroine = heroine;
        this.movie_description = movie_description;
        this.movie_name = movie_name;
        this.music = music;
        this.production = production;
        this.year=year;
        this.videoUrl = videoUrl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMovie_Type() {
        return Movie_Type;
    }

    public void setMovie_Type(String movie_Type) {
        Movie_Type = movie_Type;
    }

    public String getThumbnail_URL() {
        return Thumbnail_URL;
    }

    public void setThumbnail_URL(String thumbnail_URL) {
        Thumbnail_URL = thumbnail_URL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComedian() {
        return comedian;
    }

    public void setComedian(String comedian) {
        this.comedian = comedian;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getHero() {
        return hero;
    }

    public void setHero(String hero) {
        this.hero = hero;
    }

    public String getHeroine() {
        return heroine;
    }

    public void setHeroine(String heroine) {
        this.heroine = heroine;
    }

    public String getMovie_description() {
        return movie_description;
    }

    public void setMovie_description(String movie_description) {
        this.movie_description = movie_description;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
