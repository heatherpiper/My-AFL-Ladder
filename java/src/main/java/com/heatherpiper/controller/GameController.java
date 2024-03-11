package com.heatherpiper.controller;

import com.heatherpiper.dao.GameDao;
import com.heatherpiper.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
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