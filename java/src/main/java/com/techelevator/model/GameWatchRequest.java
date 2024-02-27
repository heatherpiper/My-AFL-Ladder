package com.techelevator.model;

import java.util.List;

public class GameWatchRequest {
    private int userId;
    private List<Integer> gameIds;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getGameIds() {
        return gameIds;
    }

    public void setGameIds(List<Integer> gameIds) {
        this.gameIds = gameIds;
    }
}
