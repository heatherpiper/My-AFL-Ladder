package com.techelevator.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Year;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.techelevator.model.Game;

@ExtendWith(MockitoExtension.class)
public class SquiggleServiceTests {

    @Mock
    private SquiggleService squiggleService;

    @Test
    public void getMostRecentlyPlayedRound_returnsRoundOfLastGame() {
        // Arrange
        int currentYear = Year.now().getValue();
        Game game1 = new Game();
        game1.setRound(1);
        Game game2 = new Game();
        game2.setRound(2);
        List<Game> games = Arrays.asList(game1, game2);
        when(squiggleService.fetchGamesForYear(anyInt())).thenReturn(games);

        int mostRecentlyPlayedRound = squiggleService.getMostRecentlyPlayedRound();
        System.out.println(games);
        assertEquals(2, mostRecentlyPlayedRound);
    }
}