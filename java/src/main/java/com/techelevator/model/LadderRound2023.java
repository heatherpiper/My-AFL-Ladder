package com.techelevator.model;

public class LadderRound2023 {
    private int round;
    private String team;
    private int gamesPlayed;
    private int totalPoints;
    private double scoreDifferential;

    public LadderRound2023() {
    }

    public LadderRound2023(int round, String team, int gamesPlayed, int totalPoints, double scoreDifferential) {
        this.round = round;
        this.team = team;
        this.gamesPlayed = gamesPlayed;
        this.totalPoints = totalPoints;
        this.scoreDifferential = scoreDifferential;
    }

    public int getRound() {
        return round;
    }

    public String getTeam() {
        return team;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public double getScoreDifferential() {
        return scoreDifferential;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void setScoreDifferential(double scoreDifferential) {
        this.scoreDifferential = scoreDifferential;
    }
}
