package com.techelevator.controller;

import com.techelevator.dao.WatchedGamesDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Game;
import com.techelevator.model.GameWatchRequest;
import com.techelevator.service.WatchedGamesService;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/users/{userId}/watched-games")
public class WatchedGamesController {

    private final WatchedGamesDao watchedGamesDao;
    private final WatchedGamesService watchedGamesService;

    public WatchedGamesController(WatchedGamesDao watchedGamesDao, WatchedGamesService watchedGamesService) {
        this.watchedGamesDao = watchedGamesDao;
        this.watchedGamesService = watchedGamesService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/watch")
    public void addGamesToWatchedList(@PathVariable("userId") int userId, @RequestBody @NotNull GameWatchRequest request) {
        watchedGamesService.markGamesAsWatchedSequentially(userId, request.getGameIds());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/unwatch")
    public void removeGamesFromWatchedList(@PathVariable("userId") int userId, @RequestBody @NotNull GameWatchRequest request) {
        watchedGamesService.markGamesAsUnwatchedSequentially(userId, request.getGameIds());
    }

    @GetMapping
    public ResponseEntity<List<Game>> getWatchedGames(@PathVariable("userId") int userId) {
        List<Game> games = watchedGamesDao.findWatchedGames(userId);
        if (games.isEmpty()) {
            return ResponseEntity.ok().body(new ArrayList<>()); // return empty array if there are no watched games
        }
        return ResponseEntity.ok(games);
    }

    @GetMapping("/unwatched")
    public ResponseEntity<List<Game>> getUnwatchedGames(@PathVariable("userId") int userId) {
        List<Game> games = watchedGamesDao.findUnwatchedGames(userId);
        if (games.isEmpty()) {
            return ResponseEntity.ok().body(new ArrayList<>()); // return empty array if there are no unwatched games
        }
        return ResponseEntity.ok(games);
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity<Object> handleDatabaseException(Exception e) {
        Logger logger = LoggerFactory.getLogger(WatchedGamesController.class);
        logger.error("Database error: ", e);

        String errorMessage = "An error occurred while processing your request. Please try again later.";

        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({DaoException.class})
    public ResponseEntity<Object> handleDaoException(DaoException e) {
        Logger logger = LoggerFactory.getLogger(WatchedGamesController.class);
        logger.error("DAO error: ", e);

        String errorMessage = "An error occurred while processing your request. Please try again later.";

        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

