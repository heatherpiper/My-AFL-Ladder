package com.techelevator.model;

public class TeamLadderEntry {
    private int userId;
    private int teamId;
    private int gameId;
    private int points;
    private int totalPoints;

    public TeamLadderEntry() {
    }

    public TeamLadderEntry(int userId, int teamId, int gameId, int points, int totalPoints) {
        this.userId =  userId;
        this.teamId = teamId;
        this.gameId = gameId;
        this.points = points;
        this.totalPoints = totalPoints;
    }

    public int getUserId() {
        return userId;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getGameId() {
        return gameId;
    }

    public int getPoints() {
        return points;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTotalPoints(int points) {
        this.totalPoints = totalPoints;
    }
}

