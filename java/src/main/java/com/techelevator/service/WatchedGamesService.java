package com.techelevator.service;

import com.techelevator.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Comparator;

import com.techelevator.model.Game;
import com.techelevator.model.UserLadderEntry;

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
        logger.info("Starting to mark games as watched sequentially for user {}, gameIds: {}", userId, gameIds);
        // Validate user ID
        if (userId <= 0 || !userDao.userExists(userId)) {
            logger.warn("Attempt to mark games watched with invalid userId: {}", userId);
            throw new IllegalArgumentException("User does not exist");
        }
        //Check for empty gameIds list
        if (gameIds == null || gameIds.isEmpty()) {
            logger.warn("Attempt to mark games watched for userId: {} with empty or null gameIds list", userId);
            throw new IllegalArgumentException("Game IDs list cannot be empty or null");
        }
        // Make sure gameIds list does not contain duplicates
        if (gameIds.size() != new HashSet<>(gameIds).size()) {
            logger.warn("Attempt to mark games watched for userId: {} with duplicate gameIds: {}", userId, gameIds);
            throw new IllegalArgumentException("Game IDs list cannot contain duplicates");
        }

        // Mark games as watched and update ladder sequentially
        for (Integer gameId : gameIds) {
            markGameAsWatchedAndUpdateLadder(userId, gameId);
        }
        logger.info("Completed marking games as watched sequentially for user {}", userId);
    }

    public void markGamesAsUnwatchedSequentially(int userId, List<Integer> gameIds) {
        logger.info("Starting to mark games as unwatched sequentially for user {}, gameIds: {}", userId, gameIds);
        // Validate user ID
        if (userId <= 0 || !userDao.userExists(userId)) {
            logger.warn("Attempt to mark games unwatched with invalid userId: {}", userId);
            throw new IllegalArgumentException("User does not exist");
        }
        // Check for empty gameIds list
        if (gameIds == null || gameIds.isEmpty()) {
            logger.warn("Attempt to mark games unwatched for userId: {} with empty or null gameIds list", userId);
            throw new IllegalArgumentException("Game IDs list cannot be empty or null");
        }
        // Make sure gameIds list does not contain duplicates
        if (gameIds.size() != new HashSet<>(gameIds).size()) {
            logger.warn("Attempt to mark games unwatched for userId: {} with duplicate gameIds: {}", userId, gameIds);
            throw new IllegalArgumentException("Game IDs list cannot contain duplicates");
        }

        // Mark games as unwatched and update ladder sequentially
        for (Integer gameId : gameIds) {
            markGameAsUnwatchedAndUpdateLadder(userId, gameId);
        }
        logger.info("Completed marking games as unwatched sequentially for user {}", userId);
    }

    @Transactional
    public void markGameAsUnwatchedAndUpdateLadder(int userId, int gameId) {
        try {
            // Validate user ID
            boolean userExists = userDao.userExists(userId);
            if (userId <= 0 || !userExists) {
                logger.warn("Attempt to mark games unwatched with invalid userId: {}", userId);
                throw new IllegalArgumentException("User does not exist");
            }

            // Validate game ID, make sure game exist
            Game game = gameDao.findGameById(gameId);
            if (game == null) {
                throw new IllegalArgumentException("Game does not exist");
            }

            // Check if game is already marked as unwatched
            boolean isAlreadyUnwatched = !watchedGamesDao.isGameWatched(userId, gameId);
            if (isAlreadyUnwatched) {
                throw new IllegalStateException("Game is already marked as unwatched");
            }

            // Mark game as unwatched
            watchedGamesDao.removeWatchedGame(userId, gameId);

            // Check if game was a draw or if there was a winner; calculate points for home and away teams
            String winner = game.getWinner();
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
            // Validate user ID
            boolean userExists = userDao.userExists(userId);
            if (userId <= 0 || !userExists) {
                logger.warn("Attempt to mark games watched with invalid userId: {}", userId);
                throw new IllegalArgumentException("User does not exist");
            }

            // Validate game ID
            Game game = gameDao.findGameById(gameId);
            if (game == null) {
                throw new IllegalArgumentException("Game does not exist");
            }

            // Check if game is already marked as watched
            boolean isAlreadyWatched = watchedGamesDao.isGameWatched(userId, gameId);
            if (isAlreadyWatched) {
                throw new IllegalStateException("Game is already marked as watched");
            }

            // Mark game as watched
            try {
                watchedGamesDao.addWatchedGame(userId, gameId);
            } catch (DataAccessException e) {
                logger.error("Database access exception for userId: {}, gameId: {}. Error: {}", userId, gameId, e.getMessage(), e);
                throw e;
            }

            // Check if game was a draw or if there was a winner; calculate points for home and away teams
            String winner = game.getWinner();
            boolean isDraw = game.getWinner() == null;
            int hteamPoints = isDraw ? 2 : game.getWinner().equals(game.getHteam()) ? 4 : 0;
            int ateamPoints = isDraw ? 2 : game.getWinner().equals(game.getAteam()) ? 4 : 0;

            // Update ladder for home team
            updateTeamLadder(userId, game.getHteam(), hteamPoints, game.getHscore(), game.getAscore());

            // Update ladder for away team
            updateTeamLadder(userId, game.getAteam(), ateamPoints, game.getAscore(), game.getHscore());

            // Recalculate position
            calculatePosition(userId);

        } catch (IllegalArgumentException e) {
            logger.error("Validation error in markGameAsWatchedAndUpdateLadder for userId: {}, gameId: {}. Error: {}",
                    userId, gameId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error in markGameAsWatchedAndUpdateLadder for userId: {}, gameId: {}", userId, gameId, e);
            throw new RuntimeException("An unexpected error occurred while processing gameId: " + gameId, e);
        }
        logger.info("Successfully marked game as watched and updated ladder for userId: {}, gameId: {}", userId, gameId);
    }

    private void updateTeamLadder(int userId, String teamName, int points, int pointsForThisGame, int pointsAgainstThisGame) {
        logger.debug("Updating team ladder for userId: {}, teamName: {}, points: {}", userId, teamName, points);

        // Validate team name
        int teamId = teamDao.findTeamIdByName(teamName);
        if (teamId <= 0) {
            throw new IllegalArgumentException("Invalid team name: " + teamName);
        }

        // Check that a ladder entry exists for the given user and team
        UserLadderEntry entry = userLadderEntryDao.getUserLadderEntry(userId, teamId);
        if (entry == null) {
            throw new IllegalArgumentException("Ladder entry does not exist for the given user and team");
        }

        // Update the ladder entry based on points scored in game
        entry.setPoints(entry.getPoints() + points);
        entry.setPointsFor(entry.getPointsFor() + pointsForThisGame);
        entry.setPointsAgainst(entry.getPointsAgainst() + pointsAgainstThisGame);

        // Calculate and set the new percentage
        entry.setPercentage(calculatePercentage(entry.getPointsFor(), entry.getPointsAgainst()));

        // Update wins, losses, draws based on the points awarded
        if (points == 4) {
            entry.setWins(entry.getWins() + 1);
        } else if (points == 0) {
            entry.setLosses(entry.getLosses() + 1);
        } else if (points == 2) {
            entry.setDraws(entry.getDraws() + 1);
        }

        // Update the ladder entry in the database
        userLadderEntryDao.updateUserLadderEntry(entry);
    }

    public void calculatePosition(int userId) {
        List<UserLadderEntry> entries = userLadderEntryDao.getAllUserLadderEntries(userId);

        //Sort the entries by points, then by percentage, and order entries in descending order
        entries.sort(Comparator.comparing(UserLadderEntry::getPoints).thenComparing(UserLadderEntry::getPercentage).reversed());

        // Assign rank based on sorted order
        for (int i = 0; i < entries.size(); i++) {
            UserLadderEntry entry = entries.get(i);
            entry.setPosition(i + 1);
            userLadderEntryDao.updateUserLadderEntry(entry);
        }
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
