package com.techelevator.model;

public class UserLadderEntry {
    private int userLadderEntryId;
    private int userId;
    private int teamId;
    private int points;
    private int percentage;
    private int position;
    private String teamName;

    public UserLadderEntry(int userLadderEntryId, int userId, int teamId, int points, int percentage, int position,
                           String teamName) {
        this.userLadderEntryId = userLadderEntryId;
        this.userId = userId;
        this.teamId = teamId;
        this.points = points;
        this.percentage = percentage;
        this.position = position;
        this.teamName = teamName;
    }

    public UserLadderEntry() {
    }

    public int getUserLadderEntryId() {
        return userLadderEntryId;
    }

    public void setUserLadderEntryId(int userLadderEntryId) {
        this.userLadderEntryId = userLadderEntryId;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}