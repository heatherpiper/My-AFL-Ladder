package com.heatherpiper.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.heatherpiper.dao.GameDao;
import com.heatherpiper.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SquiggleServiceTests {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private GameDao mockGameDao;

    private SquiggleService squiggleService;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        String jsonExample = "{\"games\":[{\"winner\":\"Collingwood\",\"roundname\":\"Round 1\"," +
                "\"hteam\":\"Geelong\",\"agoals\":19,\"venue\":\"M.C.G.\",\"updated\":\"2023-03-17 22:37:21\",\"ascore\":125,\"unixtime\":1679042400,\"round\":1,\"id\":34261,\"localtime\":\"2023-03-17 19:40:00\",\"hscore\":103,\"complete\":100,\"year\":2023,\"ateam\":\"Collingwood\",\"hgoals\":16,\"date\":\"2023-03-17 19:40:00\",\"ateamid\":4,\"is_grand_final\":0,\"tz\":\"+11:00\",\"hbehinds\":7,\"hteamid\":7,\"is_final\":0,\"winnerteamid\":4,\"timestr\":\"Full Time\",\"abehinds\":11}]}";

        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(jsonExample);

        when(mockHttpClient.send(any(HttpRequest.class), any())).thenAnswer((Answer<HttpResponse<String>>) invocation -> mockResponse);

        squiggleService = new SquiggleService(mockHttpClient, mockGameDao);
    }

    @Test
    public void fetchGamesForYearAndRound_SuccessfullyFetchesAndSavesGames() throws Exception {
        List<Game> games = squiggleService.fetchGamesForYearAndRound(2023, 1);

        // Verify that games were fetched and saved
        assertFalse(games.isEmpty());
        assertEquals(1, games.size());
        Game firstGame = games.get(0);
        assertEquals("Collingwood", firstGame.getAteam());
        assertEquals(103, firstGame.getHscore());

        // Verify that the game was saved
        verify(mockGameDao).saveAll(anyList());
    }

    @Test
    public void fetchGamesForYearAndRound_HandlesHttpClientErrorGracefully() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any()))
                .thenThrow(new IOException("Failed to fetch data"));

        List<Game> games = squiggleService.fetchGamesForYearAndRound(2023, 1);

        assertTrue(games.isEmpty());

        // Verify no interactions with GameDao if no games are fetched
        verify(mockGameDao, never()).saveAll(anyList());
    }

    @Test
    public void fetchGamesForRoundZero_ShouldReturnGames() throws Exception {
        String roundZeroJson = "{\"games\":[{\"winner\":\"Collingwood\",\"roundname\":\"Round 0\",\"hteam\":\"Geelong\",\"agoals\":19,\"venue\":\"M.C.G.\",\"updated\":\"2023-03-17 22:37:21\",\"ascore\":125,\"unixtime\":1679042400,\"round\":0,\"id\":34261,\"localtime\":\"2023-03-17 19:40:00\",\"hscore\":103,\"complete\":100,\"year\":2023,\"ateam\":\"Collingwood\",\"hgoals\":16,\"date\":\"2023-03-17 19:40:00\",\"ateamid\":4,\"is_grand_final\":0,\"tz\":\"+11:00\",\"hbehinds\":7,\"hteamid\":7,\"is_final\":0,\"winnerteamid\":4,\"timestr\":\"Full Time\",\"abehinds\":11}]}";

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(roundZeroJson);

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenAnswer(invocation -> mockResponse);

        List<Game> games = squiggleService.fetchGamesForYearAndRound(2023, 0);

        assertNotNull(games, "Expected non-null list of games");
        assertFalse(games.isEmpty(), "Expected non-empty list of games for round 0");
    }

    @Test
    public void fetchGamesUpToMostRecentRound_ShouldReturn2024Round0Games() throws Exception {
        MockitoAnnotations.openMocks(this);

        HttpClient mockHttpClient = mock(HttpClient.class);
        SquiggleService squiggleService = new SquiggleService(mockHttpClient, mockGameDao);


        String roundZero2024Json = "{\"games\":[{\"localtime\":\"2024-03-08 18:40:00\",\"id\":35701,\"year\":2024," +
                "\"ateam\":\"Carlton\",\"hgoals\":12,\"hscore\":85,\"complete\":100,\"winner\":\"Carlton\",\"roundname\":\"Opening Round\",\"unixtime\":1709887200,\"ascore\":86,\"round\":0,\"hteam\":\"Brisbane Lions\",\"agoals\":13,\"venue\":\"Gabba\",\"updated\":\"2024-03-08 22:23:39\",\"hteamid\":2,\"is_final\":0,\"winnerteamid\":3,\"timestr\":\"Full Time\",\"abehinds\":8,\"ateamid\":3,\"date\":\"2024-03-08 19:40:00\",\"hbehinds\":13,\"is_grand_final\":0,\"tz\":\"+11:00\"}]}";

        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(roundZero2024Json);

        when(mockHttpClient.send(any(HttpRequest.class), any())).thenAnswer((Answer<HttpResponse<String>>) invocation -> mockResponse);

        // Act: Fetch games up to the most recent round for 2024
        List<Game> games = squiggleService.fetchGamesUpToMostRecentRound(2024);

        // Assertions
        assertNotNull(games, "Expected non-null list of games");
        assertFalse(games.isEmpty(), "Expected at least one game for Round 0 of 2024");
        assertEquals(2024, games.get(0).getYear(), "Expected the game to be from the year 2024");
        assertEquals(0, games.get(0).getRound(), "Expected the game to be from Round 0");
    }

}
