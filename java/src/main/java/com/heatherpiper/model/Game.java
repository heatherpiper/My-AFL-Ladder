package com.heatherpiper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {
    @JsonProperty("id")
    private int id;

    @JsonProperty("round")
    private int round;

    @JsonProperty("year")
    private int year;

    @JsonProperty("date")
    private String date; // ISO 8601 date format

    @JsonProperty("hteam")
    private String hteam;

    @JsonProperty("ateam")
    private String ateam;

    @JsonProperty("hscore")
    private Integer hscore; // Use Integer to allow for null values

    @JsonProperty("ascore")
    private Integer ascore; // Use Integer to allow for null values

    @JsonProperty("winner")
    private String winner;

    @JsonProperty("complete")
    private int complete; // percentage of gameplay completed

    public Game() {
    }

    public Game(int round, int year, String hteam, String ateam) {
        this.round = round;
        this.year = year;
        this.hteam = hteam;
        this.ateam = ateam;
    }

    public Game(int round, int year, String hteam, String ateam, int complete) {
        this.round = round;
        this.year = year;
        this.hteam = hteam;
        this.ateam = ateam;
        this.complete = 0;
    }

    public Game(int id, int round, int year, String date, String hteam, String ateam, Integer hscore, Integer ascore, String winner,
                int complete) {
        this.id = id;
        this.round = round;
        this.year = year;
        this.date = date;
        this.hteam = hteam;
        this.ateam = ateam;
        this.hscore = hscore;
        this.ascore = ascore;
        this.winner = winner;
        this.complete = complete;
    }

    public int getId() {
        return id;
    }

    public int getRound() {
        return round;
    }

    public int getYear() {
        return year;
    }

    public String getDate() {
        return date;
    }

    public String getHteam() {
        return hteam;
    }

    public String getAteam() {
        return ateam;
    }

    public Integer getHscore() {
        return hscore;
    }

    public Integer getAscore() {
        return ascore;
    }

    public String getWinner() {
        return winner;
    }

    public int getComplete() {
        return complete;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHteam(String hteam) {
        this.hteam = hteam;
    }

    public void setAteam(String ateam) {
        this.ateam = ateam;
    }

    public void setHscore(Integer hscore) {
        if (hscore != null && hscore < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        this.hscore = hscore;
    }

    public void setAscore(Integer ascore) {
        if (ascore != null && ascore < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        this.ascore = ascore;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public boolean isComplete() {
        return complete == 100;
    }
}
