package com.techelevator.service;

import com.techelevator.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final JdbcWatchedGamesDao watchedGamesDao;
    private final JdbcUserLadderEntryDao userLadderEntryDao;
    private final JdbcUserDao userDao;
    private final JdbcGameDao gameDao;
    private final JdbcTeamDao teamDao;

    @Autowired
    public WatchedGamesService(JdbcWatchedGamesDao watchedGamesDao,
                               JdbcUserLadderEntryDao userLadderEntryDao, JdbcUserDao userDao, JdbcGameDao gameDao, JdbcTeamDao teamDao) {
        this.watchedGamesDao = watchedGamesDao;
        this.userLadderEntryDao = userLadderEntryDao;
        this.userDao = userDao;
        this.gameDao = gameDao;
        this.teamDao = teamDao;
    }

    public void markGamesAsWatchedSequentially(int userId, List<Integer> gameIds) {
        // Validate user ID
        if (userId <= 0 || !userDao.userExists(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }
        //Check for empty gameIds list
        if (gameIds == null || gameIds.isEmpty()) {
            throw new IllegalArgumentException("Game IDs list cannot be empty or null");
        }
        // Make sure gameIds list does not contain duplicates
        if (gameIds.size() != new HashSet<>(gameIds).size()) {
            throw new IllegalArgumentException("Game IDs list cannot contain duplicates");
        }

        for (Integer gameId : gameIds) {
            markGameAsWatchedAndUpdateLadder(userId, gameId);
        }
    }

    public void markGamesAsUnwatchedSequentially(int userId, List<Integer> gameIds) {
        // Validate user ID
        if (userId <= 0 || !userDao.userExists(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }
        // Check for empty gameIds list
        if (gameIds == null || gameIds.isEmpty()) {
            throw new IllegalArgumentException("Game IDs list cannot be empty or null");
        }
        // Make sure gameIds list does not contain duplicates
        if (gameIds.size() != new HashSet<>(gameIds).size()) {
            throw new IllegalArgumentException("Game IDs list cannot contain duplicates");
        }

        for (Integer gameId : gameIds) {
            markGameAsUnwatchedAndUpdateLadder(userId, gameId);
        }
    }

    @Transactional
    public void markGameAsUnwatchedAndUpdateLadder(int userId, int gameId) {
        // Validate user ID
        boolean userExists = userDao.userExists(userId);
        if (userId <= 0 || !userExists) {
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

        watchedGamesDao.removeWatchedGame(userId, gameId);

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
    }

    @Transactional
    public void markGameAsWatchedAndUpdateLadder(int userId, int gameId) {
        // Validate user ID
        boolean userExists = userDao.userExists(userId);
        if (userId <= 0 || !userExists) {
            throw new IllegalArgumentException("User does not exist");
        }

        // Validate game ID, make sure game exists
        Game game = gameDao.findGameById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game does not exist");
        }

        // Check if game is already marked as watched
        boolean isAlreadyWatched = watchedGamesDao.isGameWatched(userId, gameId);
        if (isAlreadyWatched) {
            throw new IllegalStateException("Game is already marked as watched");
        }

        watchedGamesDao.addWatchedGame(userId, gameId);

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
    }

    private void updateTeamLadder(int userId, String teamName, int points, int pointsForThisGame,
                                  int pointsAgainstThisGame) {
        // Validate team name
        int teamId = teamDao.findTeamIdByName(teamName);
        if (teamId <= 0) {
            throw new IllegalArgumentException("Invalid team name");
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

        userLadderEntryDao.updateUserLadderEntry(entry);
    }

    public void calculatePosition(int userId) {
        List<UserLadderEntry> entries = userLadderEntryDao.getAllUserLadderEntries(userId);

        entries.sort(Comparator.comparing(UserLadderEntry::getPoints).thenComparing(UserLadderEntry::getPercentage).reversed());

        for (int i = 0; i < entries.size(); i++) {
            UserLadderEntry entry = entries.get(i);
            entry.setPosition(i + 1);
            userLadderEntryDao.updateUserLadderEntry(entry);
        }

    }

    private double calculatePercentage(int pointsFor, int pointsAgainst) {
        if (pointsAgainst == 0) {
            return 100.0;
        } else {
            double percentage = ((double) pointsFor / pointsAgainst) * 100;
            BigDecimal bd = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }
}
