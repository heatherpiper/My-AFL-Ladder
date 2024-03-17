package com.heatherpiper.dao;

import com.heatherpiper.model.Game;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcWatchedGamesDaoTests extends BaseDaoTests {

    private JdbcWatchedGamesDao watchedGamesDao;

    @Autowired
    private DataSource dataSource;

    @Before
    public void setup() {
        watchedGamesDao = new JdbcWatchedGamesDao(dataSource);
    }

    @Test
    public void findWatchedGames_ShouldReturnExpectedResults() {
        int userId = 1;
        List<Game> results = watchedGamesDao.findWatchedGames(userId);

        assertNotNull(results);
    }

    @Test
    public void findUnwatchedGames_ShouldReturnExpectedResults() {
        int userId = 1;
        List<Game> results = watchedGamesDao.findUnwatchedGames(userId);

        assertNotNull(results);
    }

    @Test
    public void findUnwatchedGamesByRound_ShouldReturnExpectedGames() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("INSERT INTO games (id, round, year, unixtime, hteam, ateam, hscore, ascore, winner, " +
                "complete)" +
                " VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 1, 1, 2023, 1703277000, "Team A", "Team B", 100, 90, "Team A", 100);

        int userId = 1;
        int round = 1;

        List<Game> unwatchedGames = watchedGamesDao.findUnwatchedGamesByRound(userId, round);

        assertNotNull(unwatchedGames);
        assertFalse("Expected to find unwatched games for the round", unwatchedGames.isEmpty());
    }

    @Test
    public void addWatchedGame_ShouldAddGameToWatchedGames() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("INSERT INTO games (id, round, year, unixtime, hteam, ateam, hscore, ascore, winner, " +
                        "complete) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                1, 1, 2023, 1703277000, "Team A", "Team B", 100, 90, "Team A", 100);

        int userId = 1;
        int gameId = 1;

        watchedGamesDao.addWatchedGame(userId, gameId);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM watched_games WHERE user_id = ? AND game_id = ?", Integer.class, userId, gameId);
        assertNotNull("The watched game should be added", count);
        assertTrue("The watched game count should be greater than 0", count > 0);
    }

    @Test
    public void removeWatchedGame_ShouldRemoveGameFromWatchedGames() {
        int userId = 1;
        int gameId = 1;

        watchedGamesDao.removeWatchedGame(userId, gameId);

        boolean isWatched = watchedGamesDao.isGameWatched(userId, gameId);
        assertFalse(isWatched);
    }

    @Test
    public void markAllGamesInRoundWatched_ShouldMarkAllGamesInRoundAsWatched() {
        int userId = 1;
        int round = 1;

        watchedGamesDao.markAllGamesInRoundWatched(userId, round);

        List<Game> unwatchedGames = watchedGamesDao.findUnwatchedGamesByRound(userId, round);
        assertTrue(unwatchedGames.isEmpty());
    }

    @Test
    public void markAllGamesInRoundUnwatched_ShouldMarkAllGamesInRoundAsUnwatched() {
        int userId = 1;
        int round = 1;
        int gameId = 1;
        watchedGamesDao.markAllGamesInRoundWatched(userId, round);

        watchedGamesDao.markAllGamesInRoundUnwatched(userId, round);

        boolean isWatched = watchedGamesDao.isGameWatched(userId, gameId);
        assertFalse(isWatched);
    }

    @Test
    public void isGameWatched_ShouldReturnTrueIfGameIsWatched() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        int userId = 1;
        int gameId = 1;

        jdbcTemplate.update("INSERT INTO games (id, round, year, unixtime, hteam, ateam, hscore, ascore, winner, " +
                        "complete) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                gameId, 1, 2023, 1703277000, "Team A", "Team B", 100, 90, "Team A", 100);

        jdbcTemplate.update("INSERT INTO watched_games (user_id, game_id) VALUES (?, ?)",
                userId, gameId);

        boolean isWatched = watchedGamesDao.isGameWatched(userId, gameId);

        assertTrue(isWatched);

        jdbcTemplate.update("DELETE FROM watched_games WHERE user_id = ? AND game_id = ?", userId, gameId);
        jdbcTemplate.update("DELETE FROM games where id = ?", gameId);
    }
}

