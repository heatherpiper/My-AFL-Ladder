package com.techelevator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.techelevator.dao.GameDao;
import com.techelevator.model.Game;

@ExtendWith(MockitoExtension.class)
class GameControllerTests {

    @Mock
    private GameDao gameDao;

    @InjectMocks
    private GameController gameController;


    @Test
    void getAllGames_ReturnsListOfGames() {
        List<Game> expectedGames = new ArrayList<>();
        expectedGames.add(new Game(1, 1, 2024, "Team A", "Team B", 100, 50, "Team A", 100));
        expectedGames.add(new Game(2, 1, 2023, "Team C", "Team D", 100, 50, "Team C", 100));
        when(gameDao.findAllGames()).thenReturn(expectedGames);

        ResponseEntity<List<Game>> response = gameController.getAllGames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedGames, response.getBody());
    }

    @Test
    void getCompleteGames_ReturnsOnlyCompleteGames() {
        List<Game> allGames = Arrays.asList(
            new Game(1, 1, 2024, "Home Team", "Away Team", 100, 50, "Home Team", 100),
            new Game(2, 2023, "Team C", "Team D", 0),
            new Game(3, 3, 2022, "Sydney Swans", "Melbourne Demons", 40, 50, null, 50)
        );

        List<Game> completeGames = allGames.stream()
        .filter(game -> game.getComplete() == 100)
        .collect(Collectors.toList());

        when(gameDao.findCompleteGames()).thenReturn(completeGames);

        ResponseEntity<List<Game>> response = gameController.getCompleteGames();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Game> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(completeGames.size(), responseBody.size());
        assertTrue(responseBody.containsAll(completeGames));
    }

    @Test
    void getCompleteGames_ReturnsEmptyListWhenNoCompleteGames() {
        when(gameDao.findCompleteGames()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Game>> response = gameController.getCompleteGames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getGame_ReturnsCorrectGame() {
        Game expectedGame = new Game(1, 1, 2024, "Home Team", "Away Team", 100, 50, "Home Team", 100);
        when(gameDao.findGameById(1)).thenReturn(expectedGame);

        ResponseEntity<Game> response = gameController.getGameById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedGame, response.getBody());
    }

    @Test
    void getGame_ReturnsNotFoundWhenGameDoesNotExist() {
        when(gameDao.findGameById(1)).thenReturn(null);

        ResponseEntity<Game> response = gameController.getGameById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getGamesByRound_ReturnsListOfGames() {
        int round = 1;
        List<Game> expectedGames = new ArrayList<>();
        expectedGames.add(new Game(1, round, 2024, "Team A", "Team B", 100, 50, "Team A", 100));
        expectedGames.add(new Game(2, round, 2023, "Team C", "Team D", 100, 50, "Team C", 100));
        when(gameDao.findGamesByRound(round)).thenReturn(expectedGames);

        ResponseEntity<List<Game>> response = gameController.getGamesByRound(round);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedGames, response.getBody());
    }
}
