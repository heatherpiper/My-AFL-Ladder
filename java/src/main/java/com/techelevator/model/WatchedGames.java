package com.techelevator.model;

public class WatchedGames {
    private int watchedGameId;
    private int userId;
    private int gameId;

    public WatchedGames() {
    }

    public WatchedGames(int watchedGameId, int userId, int gameId) {
        this.watchedGameId = watchedGameId;
        this.userId = userId;
        this.gameId = gameId;
    }

    public int getWatchedGameId() {
        return watchedGameId;
    }

    public int getUserId() {
        return userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setWatchedGameId(int watchedGameId) {
        this.watchedGameId = watchedGameId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
