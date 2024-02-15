package com.techelevator.service;

import org.springframework.beans.factory.annotation.Autowired;
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

public class WatchedGamesService {

    private final JdbcWatchedGamesDao watchedGamesDao;
    private final JdbcUserLadderEntryDao userLadderEntryDao;
    private final JdbcGameDao gameDao;
    private final JdbcTeamDao teamDao;

    @Autowired
    public WatchedGamesService(JdbcWatchedGamesDao watchedGamesDao, JdbcUserLadderEntryDao userLadderEntryDao, JdbcGameDao gameDao, JdbcTeamDao teamDao) {
        this.watchedGamesDao = watchedGamesDao;
        this.userLadderEntryDao = userLadderEntryDao;
        this.gameDao = gameDao;
        this.teamDao = teamDao;
    }

    public void calculatePosition(int userId) {
        List<UserLadderEntry> entries = userLadderEntryDao.getAllUserLadderEntries(userId);

        Collections.sort(entries, Comparator.comparing(UserLadderEntry::getPoints).thenComparing(UserLadderEntry::getPercentage).reversed());

        for (int i = 0; i < entries.size(); i++) {
            UserLadderEntry entry = entries.get(i);
            entry.setPosition(i + 1);
            userLadderEntryDao.updateUserLadderEntry(entry);
        }

    }

    @Transactional
    public void markGameAsWatched(int userId, int gameId) {
        watchedGamesDao.addWatchedGame(userId, gameId);

        Game game = gameDao.findGameById(gameId);

        // Determine points earned by each team
        int hteamPoints;
        int ateamPoints;
        if (game.getHscore() > game.getAscore()) {
            hteamPoints = 4;
            ateamPoints = 0;
        } else if (game.getHscore() < game.getAscore()) {
            hteamPoints = 0;
            ateamPoints = 4;
        } else {
            hteamPoints = 2;
            ateamPoints = 2;
        }
        
        int hteamId = teamDao.findTeamIdByName(game.getHteam());
        int ateamId = teamDao.findTeamIdByName(game.getAteam());

        // Create UserLadderEntry objects for each team
        UserLadderEntry hteamEntry = new UserLadderEntry();
        hteamEntry.setUserId(userId);
        hteamEntry.setTeamId(hteamId);
        hteamEntry.setPoints(hteamPoints);

        UserLadderEntry ateamEntry = new UserLadderEntry();
        ateamEntry.setUserId(userId);
        ateamEntry.setTeamId(ateamId);
        ateamEntry.setPoints(ateamPoints);

        // Update user_ladder table in the database
        userLadderEntryDao.updateUserLadderEntry(ateamEntry);
        userLadderEntryDao.updateUserLadderEntry(hteamEntry);

        // Recalculate position
        calculatePosition(userId);
    }
    
}
