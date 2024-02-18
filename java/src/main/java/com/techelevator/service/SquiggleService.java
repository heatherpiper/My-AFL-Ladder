package com.techelevator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.model.Game;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.Year;
import java.util.Comparator;

import com.techelevator.dao.GameDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SquiggleService {

    private final HttpClient httpClient;

    @Autowired
    private GameDao gameDao;

    public SquiggleService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Game> fetchGamesForRound(int year, int round) {
        String url = "https://api.squiggle.com.au/?q=games;year=" + year + ";round=" + round;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("User-Agent", "My AFL Ladder (github.com/heatherpiper/My-AFL-Ladder)")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<Game> games = parseGames(response.body());

            if (!games.isEmpty()) {
                gameDao.saveAll(games);
            }
            return games;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Game> fetchGamesForYear(int year) {
        String url = "https://api.squiggle.com.au/?q=games;year=" + year;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("User-Agent", "My AFL Ladder (github.com/heatherpiper/My-AFL-Ladder)")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<Game> games = parseGames(response.body());

            if (!games.isEmpty()) {
                gameDao.saveAll(games);
            }
            return games;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public int getMostRecentlyPlayedRound() {
        // Make sure current season has started before fetching games; if not, fetch previous season
        LocalDate currentDate = LocalDate.now();
        LocalDate march7 = LocalDate.of(currentDate.getYear(), 3, 7);
        int yearToFetch;

        if (!currentDate.isBefore(march7)) {
            yearToFetch = currentDate.getYear();
        } else {
            yearToFetch = currentDate.getYear() - 1;
        }

        List<Game> games = fetchGamesForYear(yearToFetch);
        
        if (games.isEmpty()) {
            return -1;
        }

        games.sort(Comparator.comparing(Game::getRound));

        int mostRecentlyPlayedRound = games.get(games.size() - 1).getRound();
        return mostRecentlyPlayedRound;
    }
    

    private List<Game> parseGames(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Game> gamesList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode gamesNode = rootNode.path("games");
            if (gamesNode.isArray()) {
                for (JsonNode gameNode : gamesNode) {
                    Game game = objectMapper.treeToValue(gameNode, Game.class);
                    gamesList.add(game);
                }
            }
            return gamesList;
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
