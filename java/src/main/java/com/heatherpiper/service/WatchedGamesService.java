package com.heatherpiper.service;

import com.heatherpiper.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Comparator;

import com.heatherpiper.model.Game;
import com.heatherpiper.model.UserLadderEntry;

@Service
public class WatchedGamesService {

    private static final Logger logger = LoggerFactory.getLogger(WatchedGamesService.class);

    private final JdbcWatchedGamesDao watchedGamesDao;
    private final JdbcUserLadderEntryDao userLadderEntryDao;
    private final JdbcUserDao userDao;
    private final JdbcGameDao gameDao;
    private final JdbcTeamDao teamDao;

    @Autowired
    public WatchedGamesService(JdbcWatchedGamesDao watchedGamesDao, JdbcUserLadderEntryDao userLadderEntryDao,
                               JdbcUserDao userDao, JdbcGameDao gameDao, JdbcTeamDao teamDao) {
        this.watchedGamesDao = watchedGamesDao;
        this.userLadderEntryDao = userLadderEntryDao;
        this.userDao = userDao;
        this.gameDao = gameDao;
        this.teamDao = teamDao;
    }

    public void markGamesAsWatchedSequentially(int userId, List<Integer> gameIds) {

        // Validate user ID and game IDs
        validateUserAndGameIds(userId, gameIds);

        // Mark games as watched and update ladder sequentially
        for (Integer gameId : gameIds) {
            markGameAsWatchedAndUpdateLadder(userId, gameId);
        }
    }

    public void markGamesAsUnwatchedSequentially(int userId, List<Integer> gameIds) {

        // Validate user ID and game IDs
        validateUserAndGameIds(userId, gameIds);

        // Mark games as unwatched and update ladder sequentially
        for (Integer gameId : gameIds) {
            markGameAsUnwatchedAndUpdateLadder(userId, gameId);
        }
    }

    @Transactional
    public void markGameAsUnwatchedAndUpdateLadder(int userId, int gameId) {
        try {
            // Before proceeding, ensure game is currently watched
            boolean isCurrentlyWatched = watchedGamesDao.isGameWatched(userId, gameId);

            if (isCurrentlyWatched) {

                // Validate game existence
                Game game = validateGameExistence(gameId);

                // Mark game as unwatched
                watchedGamesDao.removeWatchedGame(userId, gameId);

                // Check if game was a draw or if there was a winner; calculate points for home and away teams
                boolean isDraw = game.getWinner() == null;
                int hteamPointsReversed = isDraw ? -2 : game.getWinner().equals(game.getHteam()) ? -4 : 0;
                int ateamPointsReversed = isDraw ? -2 : game.getWinner().equals(game.getAteam()) ? -4 : 0;

                // Reverse update ladder for home team
                updateTeamLadder(userId, game.getHteam(), hteamPointsReversed, -game.getHscore(), -game.getAscore());

                // Reverse update ladder for away team
                updateTeamLadder(userId, game.getAteam(), ateamPointsReversed, -game.getAscore(), -game.getHscore());

                // Recalculate position
                calculatePosition(userId);

                logger.info("Successfully marked game as unwatched and updated ladder for userId: {}, gameId: {}", userId,
                        gameId);
            } else {
                logger.info("Game with ID: {} is not marked as watched for user ID: {}. No action taken.", gameId,
                        userId);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Validation error in markGameAsUnwatchedAndUpdateLadder for userId: {}, gameId: {}. Error: {}",
                    userId, gameId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error in markGameAsUnwatchedAndUpdateLadder for userId: {}, gameId: {}", userId,
                    gameId, e);
            throw new RuntimeException("An unexpected error occurred while processing gameId: " + gameId, e);
        }
    }

    @Transactional
    public void markGameAsWatchedAndUpdateLadder(int userId, int gameId) {
        try {
            // Before proceeding, ensure the game is not already watched
            boolean isAlreadyWatched = watchedGamesDao.isGameWatched(userId, gameId);

            if (!isAlreadyWatched) {
                // Validate game existence
                Game game = validateGameExistence(gameId);

                // Mark game as watched
                watchedGamesDao.addWatchedGame(userId, gameId);

                // Check if game was a draw or if there was a winner; calculate points for home and away teams
                boolean isDraw = game.getWinner() == null;
                int hteamPoints = isDraw ? 2 : game.getWinner().equals(game.getHteam()) ? 4 : 0;
                int ateamPoints = isDraw ? 2 : game.getWinner().equals(game.getAteam()) ? 4 : 0;

                // Update ladder for home team
                updateTeamLadder(userId, game.getHteam(), hteamPoints, game.getHscore(), game.getAscore());

                // Update ladder for away team
                updateTeamLadder(userId, game.getAteam(), ateamPoints, game.getAscore(), game.getHscore());

                // Recalculate position
                calculatePosition(userId);

                logger.info("Successfully marked game as watched and updated ladder for userId: {}, gameId: {}", userId, gameId);
            } else {
                logger.info("Game with ID: {} is already watched for user ID: {}. No action taken.", gameId, userId);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Validation error in markGameAsWatchedAndUpdateLadder for userId: {}, gameId: {}. Error: {}",
                    userId, gameId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error in markGameAsWatchedAndUpdateLadder for userId: {}, gameId: {}", userId, gameId, e);
            throw new RuntimeException("An unexpected error occurred while processing gameId: " + gameId, e);
        }
    }

    private void updateTeamLadder(int userId, String teamName, int points, int pointsForThisGame, int pointsAgainstThisGame) {

        // Validate team name
        if (teamName == null || teamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name must not be empty or null.");
        }

        int teamId = teamDao.findTeamIdByName(teamName);
        if (teamId <= 0) {
            throw new IllegalArgumentException("Invalid team name: " + teamName);
        }

        // Fetch all watched games for the user to recalculate wins, losses, and draws
        List<Game> watchedGames = watchedGamesDao.findWatchedGames(userId);

        int wins = 0, losses = 0, draws = 0;
        for (Game game : watchedGames) {
            boolean isHomeTeam = game.getHteam().equals(teamName);
            boolean isAwayTeam = game.getAteam().equals(teamName);

            if (isHomeTeam || isAwayTeam) {
                if (game.getWinner() == null) {
                    draws++;
                } else if (game.getWinner().equals(teamName)) {
                    wins++;
                } else {
                    losses++;
                }
            }
        }

        // Check that a ladder entry exists for the given user and team
        UserLadderEntry entry = userLadderEntryDao.getUserLadderEntry(userId, teamId);
        if (entry == null) {
            throw new IllegalArgumentException("Ladder entry does not exist for the given user and team");
        }

        // Update wins, losses, and draws
        entry.setWins(wins);
        entry.setLosses(losses);
        entry.setDraws(draws);

        // Update points, pointsFor, and pointsAgainst
        entry.setPoints(entry.getPoints() + points);
        entry.setPointsFor(entry.getPointsFor() + pointsForThisGame);
        entry.setPointsAgainst(entry.getPointsAgainst() + pointsAgainstThisGame);

        // Calculate percentage
        entry.setPercentage(calculatePercentage(entry.getPointsFor(), entry.getPointsAgainst()));

        // Update the ladder entry
        userLadderEntryDao.updateUserLadderEntry(entry);
    }

    private void validateUserAndGameIds(int userId, List<Integer> gameIds) {
        if (userId <= 0 || !userDao.userExists(userId)) {
            logger.warn("Invalid userId: {}", userId);
            throw new IllegalArgumentException("User does not exist");
        }
        if (gameIds == null || gameIds.isEmpty()) {
            logger.warn("Empty or null gameIds list for userId: {}", userId);
            throw new IllegalArgumentException("Game IDs list cannot be empty or null");
        }
        if (gameIds.size() != new HashSet<>(gameIds).size()) {
            logger.warn("Duplicate gameIds for userId: {}", userId);
            throw new IllegalArgumentException("Game IDs list cannot contain duplicates");
        }
    }

    private Game validateGameExistence(int gameId) {
        Game game = gameDao.findGameById(gameId);
        if (game == null) {
            logger.error("Game does not exist for gameId: {}", gameId);
            throw new IllegalArgumentException("Game does not exist");
        }
        return game;
    }

    public void calculatePosition(int userId) {
        List<UserLadderEntry> entries = userLadderEntryDao.getAllUserLadderEntries(userId);

        //Sort the entries by points, then by percentage, and order entries in descending order
        entries.sort(Comparator.comparing(UserLadderEntry::getPoints).thenComparing(UserLadderEntry::getPercentage).reversed());

        // Assign rank based on sorted order
        for (int i = 0; i < entries.size(); i++) {
            UserLadderEntry entry = entries.get(i);
            entry.setPosition(i + 1);
        }
        userLadderEntryDao.updateUserLadderEntries(entries);

        logger.info("Updated ranks for all teams in ladder for userId: {}", userId);
    }

    private double calculatePercentage(int pointsFor, int pointsAgainst) {
        // Avoid division by zero
        if (pointsAgainst == 0) {
            return 100.0;
        } else {
            /* Calculate percentage of pointsFor relative to pointAgainst. Use BigDecimal to ensure precision when
            rounding to 2 decimal places */
            double percentage = ((double) pointsFor / pointsAgainst) * 100;
            BigDecimal bd = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }

    public void resetUserLadderAndMarkAllGamesUnwatched(int userId) {
        boolean userExists = userDao.userExists(userId);
        if (userId <= 0 || !userExists) {
            throw new IllegalArgumentException("User does not exist");
        }

        List<UserLadderEntry> entries = userLadderEntryDao.getAllUserLadderEntries(userId);
        for (UserLadderEntry entry : entries) {
            entry.setPoints(0);
            entry.setPercentage(100);
            entry.setPosition(0);
            entry.setWins(0);
            entry.setLosses(0);
            entry.setDraws(0);
            entry.setPointsFor(0);
            entry.setPointsAgainst(0);
            userLadderEntryDao.updateUserLadderEntry(entry);
        }
        watchedGamesDao.markAllGamesUnwatched(userId);
    }
}
