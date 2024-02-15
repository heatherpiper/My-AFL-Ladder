package com.techelevator.dao;

public interface UserLadderEntryDao {

    void addLadderEntry(int userId, int teamId, int gameId, int wins, int losses, int ties, int points, int totalPoints, int gamesPlayed);

    void updateLadderEntry(int userId, int teamId, int gameId, int wins, int losses, int ties, int points, int totalPoints, int gamesPlayed);

    void deleteLadderEntry(int userId, int teamId, int gameId);

    void deleteLadderEntriesByUser(int userId);

    void deleteLadderEntriesByUserAndGame(int userId, int gameId);

    void deleteLadderEntriesByUserAndRound(int userId, int round);

    void resetUserLadder(int userId);

    void deleteAllLadderEntriesByUserAndRound(int userId, int round);
}
