package com.heatherpiper.dao;

import com.heatherpiper.model.Game;

import java.util.List;

public interface GameDao{

    Game findGameById(int id);

    List<Game> findAllGames();

    List<Game> findGamesByRound(int round);

    List<Game> findCompleteGames();

    List<Game> findIncompleteGames();

    String findWinnerByGameId(int id);

    void saveAll(List<Game> games);

}
