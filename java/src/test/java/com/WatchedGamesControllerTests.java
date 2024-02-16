package com;

import com.techelevator.controller.WatchedGamesController;
import com.techelevator.dao.WatchedGamesDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

public class WatchedGamesControllerTests {

    private WatchedGamesController controller;
    private WatchedGamesDao dao;

    @BeforeEach
    public void setup() {
        dao = Mockito.mock(WatchedGamesDao.class);
        controller = new WatchedGamesController(dao);
    }

    @Test
    public void addGameToWatchedList_ShouldCallDaoMethod() {
        int userId = 1;
        int gameId = 1;

        controller.addGameToWatchedList(userId, gameId);

        verify(dao).addWatchedGame(userId, gameId);
    }

    @Test
    public void addGameToWatchedList_ShouldThrowException_WhenDaoMethodThrowsException() {
        int userId = 1;
        int gameId = 1;

        doThrow(DaoException.class).when(dao).addWatchedGame(userId, gameId);

        try {
            controller.addGameToWatchedList(userId, gameId);
        } catch (Exception ex) {
            assertEquals(DaoException.class, ex.getClass());
        }
    }

    @Test
    public void removeGameFromWatchedList_ShouldCallDaoMethod() {
        int userId = 1;
        int gameId = 1;

        controller.removeGameFromWatchedList(userId, gameId);

        verify(dao).removeWatchedGame(userId, gameId);
    }

    @Test
    public void findWatchedGames_ShouldReturnWatchedGames() {
        int userId = 1;
        List<Game> watchedGames = Arrays.asList(
            new Game(1, 2020, "Team 1", "Team 2"),
            new Game(2, 2021, "Team 3", "Team 4")
        );
    
        when(dao.findWatchedGames(userId)).thenReturn(watchedGames);
    
        ResponseEntity<List<Game>> response = controller.findWatchedGames(userId);
    
        assertEquals(watchedGames, response.getBody());
    }

    @Test
    public void findWatchedGames_shouldReturnEmptyList_whenNoWatchedGamesFound() {
        int userId = 1;

        when(dao.findWatchedGames(userId)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Game>> response = controller.findWatchedGames(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ArrayList<>(), response.getBody());
    }

    @Test
    public void findWatchedGames_shouldReturnListOfGames_whenWatchedGamesFound() {
        int userId = 1;
        List<Game> games = new ArrayList<>();
        games.add(new Game(1, 2023, "Home Team", "Away Team"));
        games.add(new Game(2, 2024, "Sydney Swans", "Melbourne Demons"));

        when(dao.findWatchedGames(userId)).thenReturn(games);

        ResponseEntity<List<Game>> response = controller.findWatchedGames(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(games, response.getBody());
    }

    @Test
    public void findUnwatchedGames_shouldReturnEmptyList_whenNoUnwatchedGamesFound() {
        int userId = 1;

        when(dao.findUnwatchedGames(userId)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Game>> response = controller.findUnwatchedGames(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ArrayList<>(), response.getBody());
    }

    @Test
    public void findUnwatchedGames_shouldReturnListOfGames_whenUnwatchedGamesFound() {
        int userId = 1;
        List<Game> games = new ArrayList<>();
        games.add(new Game(1, 2023, "Home Team", "Away Team"));
        games.add(new Game(2, 2024, "Sydney Swans", "Melbourne Demons"));

        when(dao.findUnwatchedGames(userId)).thenReturn(games);

        ResponseEntity<List<Game>> response = controller.findUnwatchedGames(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(games, response.getBody());
    }

    @Test
    public void markAllGamesWatched_shouldCallWatchedGamesDao() {
        int userId = 1;

        controller.markAllGamesWatched(userId);

        verify(dao).markAllGamesWatched(userId);
    }

    @Test
    public void markAllGamesUnwatched_shouldCallWatchedGamesDao() {
        int userId = 1;

        controller.markAllGamesUnwatched(userId);

        verify(dao).markAllGamesUnwatched(userId);
    }

    @Test
    public void removeGameFromWatchedList_ShouldThrowException_WhenDaoMethodThrowsException() {
        int userId = 1;
        int gameId = 1;

        doThrow(DaoException.class).when(dao).removeWatchedGame(userId, gameId);

        try {
            controller.removeGameFromWatchedList(userId, gameId);
        } catch (Exception ex) {
            assertEquals(DaoException.class, ex.getClass());
        }
    }

    @Test
    public void handleDatabaseException_shouldReturnInternalServerError() {
        DaoException ex = new DaoException("Test message");
        ResponseEntity<Object> response = controller.handleDatabaseException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing your request. Please try again later.", response.getBody());
    }

    @Test
    public void handleDaoException_shouldReturnInternalServerError() {
        DaoException ex = new DaoException("Test message");
        ResponseEntity<Object> response = controller.handleDaoException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing your request. Please try again later.", response.getBody());
    }
}
