package com.heatherpiper.model;

public class UserLadderEntry {
    private int userId;
    private int teamId;
    private int points;
    private double percentage;
    private int position;
    private String teamName;
    private int wins;
    private int losses;
    private int draws;
    private int pointsFor;
    private int pointsAgainst;

    public UserLadderEntry(int userId, int teamId, int points, double percentage, int position,
                           String teamName, int wins, int losses, int draws, int pointsFor, int pointsAgainst) {
        this.userId = userId;
        this.teamId = teamId;
        this.points = points;
        this.percentage = percentage;
        this.position = position;
        this.teamName = teamName;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.pointsFor = pointsFor;
        this.pointsAgainst = pointsAgainst;
    }

    public UserLadderEntry() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getPointsFor() {
        return pointsFor;
    }

    public void setPointsFor(int pointsFor) {
        this.pointsFor = pointsFor;
    }

    public int getPointsAgainst() {
        return pointsAgainst;
    }

    public void setPointsAgainst(int pointsAgainst) {
        this.pointsAgainst = pointsAgainst;
    }
}