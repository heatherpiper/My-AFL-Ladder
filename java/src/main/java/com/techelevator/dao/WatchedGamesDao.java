package com.techelevator.dao;

import com.techelevator.model.Game;

import java.util.List;

public interface WatchedGamesDao {

    List<Game> findWatchedGames(int userId);

    List<Game> findUnwatchedGames(int userId);

    List<Game> findUnwatchedGamesByRound(int userId, int round);

    void addGameToWatchedList(int userId, int gameId);

    void removeGameFromWatchedList(int userId, int gameId);

    void markAllGamesWatched(int userId);

    void markAllGamesUnwatched(int userId);

    void markAllGamesInRoundWatched(int userId, int round);

    void markAllGamesInRoundUnwatched(int userId, int round);

}
