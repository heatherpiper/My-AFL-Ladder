package com.techelevator.model;

public class UserTeamRanking {
    private int id; // Corresponds to the 'id' in the database
    private int userId; // Corresponds to 'user_id'
    private int teamId; // Corresponds to 'team_id'
    private int points; // Corresponds to 'points'

    // Constructor
    public UserTeamRanking() {
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    // Additional methods as needed
}

