package com.techelevator.controller;

import com.techelevator.dao.UserDao;
import com.techelevator.dao.WatchedGamesDao;
import com.techelevator.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/users/{userId}/watched-games")
public class WatchedGamesController {

    private final WatchedGamesDao watchedGamesDao;

    @Autowired
    public WatchedGamesController(WatchedGamesDao watchedGamesDao) {
        this.watchedGamesDao = watchedGamesDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/watch/{gameId}")
    public void addGameToWatchedList(@PathVariable("userId") int userId,
                                                  @PathVariable("gameId") int gameId) {
        watchedGamesDao.addGameToWatchedList(userId, gameId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/unwatch/{gameId}")
    public void removeGameFromWatchedList(@PathVariable("userId") int userId, @PathVariable("gameId") int gameId) {
        watchedGamesDao.removeGameFromWatchedList(userId, gameId);
    }

    @GetMapping("/")
    public ResponseEntity<List<Game>> findWatchedGames(@PathVariable("userId") int userId) {
        List<Game> games = watchedGamesDao.findWatchedGames(userId);
        if (games.isEmpty()) {
            return ResponseEntity.ok().body(new ArrayList<>()); // return empty array if there are no watched games
        }
        return ResponseEntity.ok(games);
    }

    @GetMapping("/unwatched")
    public ResponseEntity<List<Game>> findUnwatchedGames(@PathVariable("userId") int userId) {
        List<Game> games = watchedGamesDao.findUnwatchedGames(userId);
        if (games.isEmpty()) {
            return ResponseEntity.ok().body(new ArrayList<>()); // return empty array if there are no unwatched games
        }
        return ResponseEntity.ok(games);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/watch/all")
    public void markAllGamesWatched(@PathVariable("userId") int userId) {
        watchedGamesDao.markAllGamesWatched(userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/unwatch/all")
    public void markAllGamesUnwatched(@PathVariable("userId") int userId) {
        watchedGamesDao.markAllGamesUnwatched(userId);
    }
}

