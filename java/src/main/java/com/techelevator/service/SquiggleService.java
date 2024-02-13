package com.techelevator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.model.Game;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SquiggleService {

    private final HttpClient httpClient;

    public SquiggleService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Game> fetchGamesForRound(int year, int round) {
        String url = "https://api.squiggle.com.au/?q=games;year=" + year + ";round=" + round;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("User-Agent", "My Footy Ladder (github.com/heatherpiper/myfootyladder)")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return parseGames(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
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
