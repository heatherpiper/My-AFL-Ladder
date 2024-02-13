package com.techelevator.model;

public class WatchedGames {
    private int userId;
    private int gameId;

    public WatchedGames() {
    }

    public WatchedGames(int userId, int gameId) {
        this.userId = userId;
        this.gameId = gameId;
    }

    public int getUserId() {
        return userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
