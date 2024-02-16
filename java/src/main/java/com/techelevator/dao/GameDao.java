package com.techelevator.dao;

import com.techelevator.model.Game;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameDao extends JpaRepository<Game, Integer>{

    Game findGameById(int id);

    List<Game> findAllGames();

    List<Game> findGamesByRound(int round);

    List<Game> findCompleteGames();

    List<Game> findIncompleteGames();

    String findWinnerByGameId(int id);

    void saveAll(List<Game> games);

}
