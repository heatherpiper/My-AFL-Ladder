package com.heatherpiper.controller;

import com.heatherpiper.dao.GameDao;
import com.heatherpiper.model.Game;
import com.heatherpiper.service.SquiggleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameDao gameDao;
    private final SquiggleService squiggleService;

    @Autowired
    public GameController(GameDao gameDao, SquiggleService squiggleService) {
        this.gameDao = gameDao;
        this.squiggleService = squiggleService;
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

    @PostMapping("/refreshGames")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> refreshGames(@RequestParam int year) {
        try {
            squiggleService.adminInitiatedRefresh(year);
            return ResponseEntity.ok(Map.of("message", "Game data successfully refreshed."));
        } catch (IllegalStateException e) {
            if (e.getMessage().contains("rate-limited")) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of("error", e.getMessage()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to refresh game data: " + e.getMessage()));
        }
    }
}