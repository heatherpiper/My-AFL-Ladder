package com.heatherpiper.service;

import com.heatherpiper.dao.*;
import com.heatherpiper.model.Game;
import com.heatherpiper.model.UserLadderEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Service class for marking games as watched or unwatched and updating the user's ladder entries.
 */
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

    /**
     * Marks a list of games as watched for a user and updates the user's ladder entries sequentially.
     *
     * <p>This method first validates the user ID and the list of game IDs. If the user ID and game IDs are valid, it iterates over the list of game IDs,
     * and for each game ID, it marks the game as watched and updates the user's ladder entries.
     *
     * @param userId The ID of the user.
     * @param gameIds The list of game IDs to be marked as watched.
     * @throws IllegalArgumentException If the user does not exist or if the list of game IDs is empty, null, or contains duplicate game IDs.
     */
    public void markGamesAsWatchedSequentially(int userId, List<Integer> gameIds) {

        // Validate user ID and game IDs
        validateUserAndGameIds(userId, gameIds);

        // Mark games as watched and update ladder sequentially
        for (Integer gameId : gameIds) {
            markGameAsWatchedAndUpdateLadder(userId, gameId);
        }
    }

    /**
     * Marks a list of games as unwatched for a user and updates the user's ladder entries sequentially.
     *
     * <p>This method first validates the user ID and the list of game IDs. If the user ID and game IDs are valid, it iterates over the list of game IDs,
     * and for each game ID, it marks the game as unwatched and updates the user's ladder entries.
     *
     * @param userId The ID of the user.
     * @param gameIds The list of game IDs to be marked as unwatched.
     * @throws IllegalArgumentException If the user does not exist or if the list of game IDs is empty, null, or contains duplicate game IDs.
     */
    public void markGamesAsUnwatchedSequentially(int userId, List<Integer> gameIds) {

        // Validate user ID and game IDs
        validateUserAndGameIds(userId, gameIds);

        // Mark games as unwatched and update ladder sequentially
        for (Integer gameId : gameIds) {
            markGameAsUnwatchedAndUpdateLadder(userId, gameId);
        }
    }

    /**
     * Marks a game as unwatched and updates the user's ladder entries.
     *
     * <p>This method first checks if the game is currently marked as watched for the user. If it is, it validates the existence of the game,
     * marks the game as unwatched, calculates the points to be subtracted from both the home and away teams based on the game result,
     * updates the ladder for both teams, and recalculates the positions of each team in the user's ladder.
     *
     * <p>If the game is not currently marked as watched, no action is taken.
     *
     * <p>This method is transactional, meaning that if any step fails, all changes made within the transaction will be rolled back.
     *
     * @param userId The user ID.
     * @param gameId The game ID.
     * @throws IllegalArgumentException If the user or game does not exist, or if the game is not currently marked as watched.
     * @throws RuntimeException If an unexpected error occurs.
     */
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

    /**
     * Marks a game as watched and updates the user's ladder entries.
     *
     * <p>This method first checks if the game is already marked as watched for the user. If not, it validates the existence of the game,
     * marks the game as watched, calculates the points for home and away teams based on the game result, updates the ladder for both
     * teams, and recalculates the positions of each team in the user's ladder.
     *
     * <p>If the game is already marked as watched, no action is taken.
     *
     * <p>This method is transactional, meaning that if any step fails, all changes made within the transaction will be rolled back.
     *
     * @param userId The user ID.
     * @param gameId The game ID.
     * @throws IllegalArgumentException If the user or game does not exist, or if the game is already marked as watched.
     * @throws RuntimeException If an unexpected error occurs.
     */
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

    /**
     * Updates a user's ladder entry for a specific team.
     *
     * <p>This method first validates the team name and retrieves the team ID. It then fetches all watched games for the user
     * and recalculates the wins, losses, and draws based on these games. It checks if a ladder entry exists for the given
     * user and team, and if it does, it updates the wins, losses, draws, points, pointsFor, and pointsAgainst for the entry.
     * It also calculates the percentage of pointsFor relative to pointsAgainst and updates this value in the entry.
     *
     * <p>Finally, it updates the ladder entry in the database.
     *
     * @param userId The user ID.
     * @param teamName The team name.
     * @param points The points to be added to the team's current total points.
     * @param pointsForThisGame The points scored by the team in this game.
     * @param pointsAgainstThisGame The points conceded by the team in this game.
     * @throws IllegalArgumentException If the team name is null or empty, or if the team does not exist, or if the ladder entry does not
     * exist for the given user and team.
     */
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

    /**
     * Validate the user ID and game IDs. The user ID must be greater than zero and the user must exist. The game IDs
     * list must not be empty or null, and must not contain duplicates.
     *
     * @param userId The user ID.
     * @param gameIds The list of game IDs.
     * @throws IllegalArgumentException if a user does not exist for the given user ID or if the list of game IDs is empty, null, or
     * contains duplicate game IDs.
     */
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

    /**
     * Validate that a game exists for the given game ID.
     *
     * @param gameId The game ID.
     * @return the game object if it exists.
     */
    private Game validateGameExistence(int gameId) {
        Game game = gameDao.findGameById(gameId);
        if (game == null) {
            logger.error("Game does not exist for gameId: {}", gameId);
            throw new IllegalArgumentException("Game does not exist");
        }
        return game;
    }

    /**
     * Calculates the position of all teams in the ladder for the given user ID. The position is based on points, then
     * percentage, and is ordered in descending order.
     *
     * @param userId The user ID.
     */
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

    /**
     * Calculate the percentage of pointsFor relative to pointsAgainst. If pointsAgainst is zero, return 100.0 to avoid division by zero.
     *
     * @param pointsFor The points scored by a team.
     * @param pointsAgainst The points conceded by a team.
     * @return the percentage of pointsFor relative to pointsAgainst
     */
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

    /**
     * Reset the user's ladder entries and mark all games as unwatched. This operation is irreversible.
     *
     * @param userId The user ID.
     * @throws IllegalArgumentException if the user does not exist.
     */
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
