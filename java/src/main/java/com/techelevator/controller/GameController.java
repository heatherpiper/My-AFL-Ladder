package com.techelevator.controller;

import com.techelevator.dao.GameDao;
import com.techelevator.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameDao gameDao;

    @Autowired
    public GameController(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    @GetMapping("")
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = gameDao.findAllGames();
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable("id") int id) {
        Game game = gameDao.findGameById(id);
        if (game == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(game);
        }
    }

    @GetMapping("/round/{round}")
    public ResponseEntity<List<Game>> getGamesByRound(@PathVariable("round") int round) {
        List<Game> games = gameDao.findGamesByRound(round);
        return ResponseEntity.ok(games);
    }

    @GetMapping("/complete")
    public ResponseEntity<List<Game>> getCompleteGames() {
        List<Game> games = gameDao.findCompleteGames();
        return ResponseEntity.ok(games);
    }

    @GetMapping("/incomplete")
    public ResponseEntity<List<Game>> getIncompleteGames() {
        List<Game> games = gameDao.findIncompleteGames();
        return ResponseEntity.ok(games);
    }
}