package com.techelevator.model;

public class UserLadderEntry {

    private int userId;
    private int teamId;
    private int gameId;
    private int round;
    private int wins;
    private int losses;
    private int ties;
    private int points;
    private int totalPoints;
    private int gamesPlayed;

    public UserLadderEntry(int userId, int teamId, int gameId, int round, int wins, int losses, int ties, int points,
     int totalPoints, int gamesPlayed) {
        this.userId = userId;
        this.teamId = teamId;
        this.gameId = gameId;
        this.round = round;
        this.wins = wins;
        this.losses = losses;
        this.ties = ties;
        this.points = points;
        this.totalPoints = totalPoints;
        this.gamesPlayed = gamesPlayed;
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

    public int getRound() {
        return round;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTies() {
        return ties;
    }

    public int getPoints() {
        return points;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementLosses() {
        losses++;
    }

    public void incrementTies() {
        ties++;
    }

    public void incrementGamesPlayed() {
        gamesPlayed++;
    }

    public void incrementTotalPoints(int points) {
        totalPoints += points;
    }

    public void decrementWins() {
        wins--;
    }

    public void decrementLosses() {
        losses--;
    }

    public void decrementTies() {
        ties--;
    }

    public void decrementGamesPlayed() {
        gamesPlayed--;
    }

    public void decrementTotalPoints(int points) {
        totalPoints -= points;
    }

    public void reset() {
        wins = 0;
        losses = 0;
        ties = 0;
        points = 0;
        gamesPlayed = 0;
    }

}
