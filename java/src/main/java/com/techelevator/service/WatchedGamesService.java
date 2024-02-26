package com.techelevator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import com.techelevator.model.Game;
import com.techelevator.dao.JdbcGameDao;
import com.techelevator.dao.JdbcTeamDao;
import com.techelevator.model.UserLadderEntry;
import com.techelevator.dao.JdbcWatchedGamesDao;
import com.techelevator.dao.JdbcUserLadderEntryDao;

@Service
public class WatchedGamesService {

    private final JdbcWatchedGamesDao watchedGamesDao;
    private final JdbcUserLadderEntryDao userLadderEntryDao;
    private final JdbcGameDao gameDao;
    private final JdbcTeamDao teamDao;

    @Autowired
    public WatchedGamesService(JdbcWatchedGamesDao watchedGamesDao,
                               JdbcUserLadderEntryDao userLadderEntryDao, JdbcGameDao gameDao, JdbcTeamDao teamDao) {
        this.watchedGamesDao = watchedGamesDao;
        this.userLadderEntryDao = userLadderEntryDao;
        this.gameDao = gameDao;
        this.teamDao = teamDao;
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

    @Transactional
    public void markGameAsUnwatchedAndUpdateLadder(int userId, int gameId) {
        watchedGamesDao.removeWatchedGame(userId, gameId);

        Game game = gameDao.findGameById(gameId);

        String winner = game.getWinner();
        boolean isDraw = winner.equals("draw");
        int hteamPointsReversed = isDraw ? -2 : winner.equals(game.getHteam()) ? -4 : 0;
        int ateamPointsReversed = isDraw ? -2 : winner.equals(game.getAteam()) ? -4 : 0;

        // Reverse update ladder for home team
        updateTeamLadder(userId, game.getHteam(), hteamPointsReversed, -game.getHscore(), -game.getAscore());

        // Reverse update ladder for away team
        updateTeamLadder(userId, game.getAteam(), ateamPointsReversed, -game.getAscore(), -game.getHscore());

        // Recalculate position
        calculatePosition(userId);
    }

    @Transactional
    public void markGameAsWatchedAndUpdateLadder(int userId, int gameId) {
        watchedGamesDao.addWatchedGame(userId, gameId);

        Game game = gameDao.findGameById(gameId);

        String winner = game.getWinner();
        boolean isDraw = winner.equals("draw");
        int hteamPoints = isDraw ? 2 : winner.equals(game.getHteam()) ? 4 : 0;
        int ateamPoints = isDraw ? 2 : winner.equals(game.getAteam()) ? 4 : 0;

        // Update ladder for home team
        updateTeamLadder(userId, game.getHteam(), hteamPoints, game.getHscore(), game.getAscore());

        // Update ladder for away team
        updateTeamLadder(userId, game.getAteam(), ateamPoints, game.getAscore(), game.getHscore());

        // Recalculate position
        calculatePosition(userId);
    }

    private void updateTeamLadder(int userId, String teamName, int points, int pointsForThisGame,
                                  int pointsAgainstThisGame) {
        int teamId = teamDao.findTeamIdByName(teamName);
        UserLadderEntry entry = userLadderEntryDao.getUserLadderEntry(userId, teamId);


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

    private double calculatePercentage(int pointsFor, int pointsAgainst) {
        return pointsAgainst == 0 ? 100.0 : ((double) pointsFor / pointsAgainst) * 100;
    }
}
