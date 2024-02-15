package com.techelevator.dao;

public interface LadderEntryDao {

    void addLadderEntry(int teamId, int wins, int losses, int ties, int points, int totalPoints, int gamesPlayed);

    void updateLadderEntry(int teamId, int wins, int losses, int ties, int points, int totalPoints, int gamesPlayed);

    void deleteLadderEntry(int teamId);

    void deleteLadderEntriesByRound(int round);

    void resetUserLadder(int teamId);
}
