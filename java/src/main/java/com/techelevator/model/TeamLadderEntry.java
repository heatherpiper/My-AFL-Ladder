package com.techelevator.model;

public class TeamLadderEntry {
    private int userId;
    private int teamId;
    private int score;

    public TeamLadderEntry() {
    }

    public TeamLadderEntry(int userId, int teamId, int score) {
        this.userId =  userId;
        this.teamId = teamId;
        this.score = score;
    }
}

