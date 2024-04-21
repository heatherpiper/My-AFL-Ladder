package com.heatherpiper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heatherpiper.dao.GameDao;
import com.heatherpiper.model.Game;
import io.netty.handler.timeout.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SquiggleService {

    private static final Logger logger = LoggerFactory.getLogger(SquiggleService.class);

    private static final long MIN_REFRESH_INTERVAL = 5 * 60 * 1000; // 5 minutes

    private long lastRefreshTime = 0;

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
            logger.error("Failed to fetch games for year {} and round {}: ", year, round, e);
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
            logger.error("Failed to fetch games for year {}: ", year, e);
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
                    .header("User-Agent", "Later Ladder (github.com/heatherpiper/Later-Ladder)")
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
            logger.error("Failed to fetch games up to most recent round for year {}: ", year, e);
        }
        return allGames;
    }

    public void adminInitiatedRefresh(int year) {
        logger.info("Admin initiated refresh of games data for current year");

        // Validate year
        if (year < Year.now().getValue() - 2 || year > Year.now().getValue()) {
            logger.warn("Invalid year provided for refresh: {}", year);
            throw new IllegalArgumentException("Invalid year for refresh. Year must be within the last 2 years.");
        }

        // Rate limiting check
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRefreshTime < MIN_REFRESH_INTERVAL) {
            logger.warn("Refresh operation is rate-limited. Last refresh was less than 15 minutes ago.");
            throw new IllegalStateException("Refresh operation is rate-limited. Please wait before trying again.");
        }
        lastRefreshTime = currentTime;

        try {
            fetchGamesUpToMostRecentRound(year);
            logger.info("Successfully refreshed game data for year {}", year);
        } catch (Exception e) {
            logger.error("Error during admin-initiated refresh of games data for year {}: ", year, e);
            throw new RuntimeException("Failed to refresh game data due to an error. Please check the logs for more details.");
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
            logger.error("Failed to parse games from response body.", e);
            return List.of();
        }
    }

    public void subscribeToGameUpdates() {
        if (gameUpdateSubscription != null && !gameUpdateSubscription.isDisposed()) {
            gameUpdateSubscription.dispose();
            logger.info("Disposed the previous game update subscription.");
        }

        logger.info("Attempting to connect to the Squiggle SSE endpoint...");

        Flux<String> eventStream = reactor.netty.http.client.HttpClient.create()
                .get()
                .uri("https://api.squiggle.com.au/sse/games")
                .responseContent()
                .asString()
                .windowUntil(s -> s.contains("\n\n"))
                .flatMap(w -> w.reduce(String::concat));

        Predicate<Throwable> isRetryableException = throwable ->
                throwable instanceof IOException;

        gameUpdateSubscription = eventStream
                .doOnSubscribe(subscription -> logger.info("Subscribed to Game Event Stream"))
                .retryWhen(Retry.backoff(10, Duration.ofSeconds(3))
                        .filter(throwable -> throwable instanceof IOException || throwable instanceof TimeoutException)
                        .doBeforeRetry(retrySignal -> logger.info("Reconnecting attempt #{}, due to: {}", retrySignal.totalRetries() + 1,
                                retrySignal.failure().getMessage()))
                        .maxBackoff(Duration.ofMinutes(1)))
                .subscribe(
                        this::processSseEvent,
                        error -> logger.error("Error on Game Event Stream", error),
                        () -> logger.info("Game Event Stream completed")
                );
    }

    private void processSseEvent(String sseEvent) {
        String trimmedEvent = sseEvent.trim();
        try {
            String eventType = extractEventType(trimmedEvent);
            if ("removeGame".equals(eventType) || "addGame".equals(eventType)) {
                String data = extractData(trimmedEvent);
                Game game = objectMapper.readValue(data, Game.class);
                gameDao.saveAll(Collections.singletonList(game));
                logger.info("Processed '{}' event for Game ID: {}", eventType, game.getId());
            } else {
                logger.info("Received an unhandled event type: {}", eventType);
            }
        } catch (JsonProcessingException e) {
            logger.error("Error processing SSE event: {}", trimmedEvent, e);
        }
    }

    private String extractEventType(String sseEvent) {
        Pattern eventPattern = Pattern.compile("^event:(.*)$", Pattern.MULTILINE);
        Matcher matcher = eventPattern.matcher(sseEvent);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "unknown";
    }

    private String extractData(String sseEvent) {
        Pattern dataPattern = Pattern.compile("^data:(.*)", Pattern.MULTILINE);
        Matcher matcher = dataPattern.matcher(sseEvent);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "{}";
    }

    @PreDestroy
    public void onDestroy() {
        if (gameUpdateSubscription != null && !gameUpdateSubscription.isDisposed()) {
            gameUpdateSubscription.dispose();
        }
    }
}
