package com.techelevator.model;

public class Ladder2023 {
    private String round;
    private String team;
    private int gamesPlayed;
    private int totalPoints;
    private double scoreDifferential;

    public void setGamesPlayed(String gamesPlayed) {
        this.gamesPlayed = Integer.parseInt(gamesPlayed);
    }

    public void setTotalPoint(String totalPoints) {
        this.totalPoints = Integer.parseInt(totalPoints);
    }

    public void setScoreDifferential(String scoreDifferential) {
        this.scoreDifferential = Double.parseDouble(scoreDifferential)
    }
}
