package com.heatherpiper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heatherpiper.model.Game;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;

import com.heatherpiper.dao.GameDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.PrematureCloseException;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class SquiggleService {

    private Disposable gameUpdateSubscription;

    private final HttpClient httpClient;

    @Autowired
    private final GameDao gameDao;

    @Autowired
    private ObjectMapper objectMapper;

    public SquiggleService(HttpClient httpClient, GameDao gameDao) {
        this.httpClient = httpClient;
        this.gameDao = gameDao;
    }

    @PostConstruct
    public void init() {
        subscribeToGameUpdates();
    }

    public List<Game> fetchGamesForYearAndRound(int year, int round) {
        String url = "https://api.squiggle.com.au/?q=games;year=" + year + ";round=" + round;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("User-Agent", "Later Ladder (github.com/heatherpiper/Later-Ladder)")
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
                .header("User-Agent", "Later Ladder (github.com/heatherpiper/Later-Ladder)")
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
            String responseBody = response.body();

            List<Game> games = parseGames(responseBody);

            // Determine the highest completed round
            int highestCompletedRound = games.stream()
                    .filter(game -> game.getComplete() == 100)
                    .mapToInt(Game::getRound)
                    .max()
                    .orElse(-1);

            boolean isCurrentYearOrLater = year >= LocalDate.now().getYear();
            boolean shouldFallbackToPreviousYear = highestCompletedRound == -1 && !isCurrentYearOrLater;

            if (shouldFallbackToPreviousYear) {
                // If no games are completed, and it's not the current year or later, try the previous year
                return fetchGamesUpToMostRecentRound(year - 1);
            } else {
                for (int round = 0; round <= highestCompletedRound; round++) {
                    allGames.addAll(fetchGamesForYearAndRound(year, round));
                }
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

    public void subscribeToGameUpdates() {
        if (gameUpdateSubscription != null && !gameUpdateSubscription.isDisposed()) {
            gameUpdateSubscription.dispose();
        }

        Flux<String> eventStream = reactor.netty.http.client.HttpClient.create()
                .get()
                .uri("https://api.squiggle.com.au/sse/games")
                .responseContent()
                .asString()
                .windowUntil(s -> s.contains("\n\n"))
                .flatMap(w -> w.reduce(String::concat));

        Predicate<Throwable> isRetryableException = throwable -> throwable instanceof PrematureCloseException;

        gameUpdateSubscription = eventStream
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(2))
                        .filter(isRetryableException)
                        .maxBackoff(Duration.ofMinutes(1))
                        .doBeforeRetry(retrySignal -> {
                            System.out.println("Attempting to reconnect, attempt #" + (retrySignal.totalRetries() + 1));
                        }))
                .subscribe(
                        this::processSseEvent,
                        error -> System.err.println("Error on Game Event Stream: " + error),
                        () -> System.out.println("Game Event Stream Completed")
                );
    }

    private void processSseEvent(String sseEvent) {
        try {
            if (sseEvent.startsWith("event:")) {
                String eventType = extractEventType(sseEvent);
                if (eventType.equals("removeGame")) {
                    String data = extractData(sseEvent);
                    Game game = objectMapper.readValue(data, Game.class);
                    gameDao.saveAll(List.of(game));
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private String extractEventType(String sseEvent) {
        int eventStart = sseEvent.indexOf("event:");
        int dataStart = sseEvent.indexOf("data:");
        if (eventStart != -1 && dataStart != -1) {
            return sseEvent.substring(eventStart + 6, dataStart).trim();
        }
        return "";
    }

    private String extractData(String sseEvent) {
        int dataStart = sseEvent.indexOf("data:");
        if (dataStart != -1) {
            return sseEvent.substring(dataStart + 5).trim();
        }
        return "";
    }

    @PreDestroy
    public void onDestroy() {
        if (gameUpdateSubscription != null && !gameUpdateSubscription.isDisposed()) {
            gameUpdateSubscription.dispose();
        }
    }
}
