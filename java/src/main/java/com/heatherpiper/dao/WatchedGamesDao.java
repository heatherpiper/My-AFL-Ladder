package com.heatherpiper.dao;

import com.heatherpiper.model.Game;

import java.util.List;

public interface WatchedGamesDao {

    List<Game> findWatchedGames(int userId);

    List<Game> findWatchedGamesByRound(int userId, int round);

    List<Game> findUnwatchedGames(int userId);

    List<Game> findUnwatchedGamesByRound(int userId, int round);

    void addWatchedGame(int userId, int gameId);

    void removeWatchedGame(int userId, int gameId);

    void markAllGamesWatched(int userId);

    void markAllGamesUnwatched(int userId);

    void markAllGamesInRoundWatched(int userId, int round);

    void markAllGamesInRoundUnwatched(int userId, int round);

    boolean isGameWatched(int userId, int gameId);

}
