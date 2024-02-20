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
import java.util.*;
import java.time.Year;

import com.techelevator.dao.GameDao;
import com.techelevator.model.YearAndRound;
import org.json.JSONArray;
import org.json.JSONObject;
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

    public List<Game> fetchGamesForYearAndRound(int year, int round) {
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

    public List<Game> fetchGamesUpToMostRecentRound(int year) {
        List<Game> allGames = new ArrayList<>();

        try {
            String url = String.format("https://api.squiggle.com.au/?q=games;year=%d", year);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("User-Agent", "My AFL Ladder (github.com/heatherpiper/My-AFL-Ladder)")
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray gamesArray = new JSONArray(response.body());

            //Determine highest completed round
            int highestCompletedRound = 0;
            for (int i = 0; i < gamesArray.length(); i++) {
                JSONObject game = gamesArray.getJSONObject(i);
                int round = game.getInt("round");
                int complete = game.getInt("complete");
                if (complete == 100 && round > highestCompletedRound) {
                    highestCompletedRound = round;
                }
            }
            // If no rounds are complete, fetch games from previous year (only)
            if (highestCompletedRound == 0 && year > LocalDate.now().getYear() - 2) {
                return fetchGamesUpToMostRecentRound(year -1);
            }

            // Fetch games for each round up to highest completed round, add games to list
            for (int round = 1; round <= highestCompletedRound; round++) {
                allGames.addAll(fetchGamesForYearAndRound(year, round));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allGames;
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
