package com.techelevator;

public class Ladder2023 {
    private String round;
    private String team;
    private int gamesPlayed;
    private int totalPoints;
    private double scoreDifferential;

    public Ladder2023() {
    }

    public Ladder2023(String round, String team, int gamesPlayed, int totalPoints, double scoreDifferential) {
        this.round = round;
        this.team = team;
        this.gamesPlayed = gamesPlayed;
        this.totalPoints = totalPoints;
        this.scoreDifferential = scoreDifferential;
    }

    public String getRound() {
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

    public void setRound(String round) {
        this.round = round;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setGamesPlayed(String gamesPlayed) {
        this.gamesPlayed = Integer.parseInt(gamesPlayed);
    }

    public void setTotalPoint(String totalPoints) {
        this.totalPoints = Integer.parseInt(totalPoints);
    }

    public void setScoreDifferential(String scoreDifferential) {
        this.scoreDifferential = Double.parseDouble(scoreDifferential);
    }
}
