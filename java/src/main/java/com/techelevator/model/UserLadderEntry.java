package com.techelevator.model;

public class UserLadderEntry {

    private int userId;
    private int teamId;
    private int points;
    private int percentage;
    private int position;

    public UserLadderEntry(int userId, int teamId, int points, int percentage, int position) {
        this.userId = userId;
        this.teamId = teamId;
        this.points = points;
        this.percentage = percentage;
        this.position = position;
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

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}