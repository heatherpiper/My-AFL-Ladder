package com.techelevator.dao;

import com.techelevator.model.Game;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestingDatabaseConfig.class})
@SpringBootTest
@Transactional
public class JdbcWatchedGamesDaoIntegrationTests {

    @Autowired
    private JdbcWatchedGamesDao watchedGamesDao;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void findWatchedGames_ShouldReturnWatchedGamesForUser() {
        int userId = 1;
        List<Game> watchedGames = watchedGamesDao.findWatchedGames(userId);

        assertFalse(watchedGames.isEmpty());
        assertTrue(watchedGames.stream().anyMatch(game -> game.getId() == 1));
    }

    @Test
    public void findUnwatchedGames_ShouldReturnUnwatchedGamesForUser() {
        int userId = 1;
        List<Game> unwatchedGames = watchedGamesDao.findUnwatchedGames(userId);

        assertFalse(unwatchedGames.isEmpty());
        assertTrue(unwatchedGames.stream().anyMatch(game -> game.getId() == 3));
    }

    @Test
    public void findUnwatchedGamesByRound_ShouldReturnUnwatchedGamesForUserInRound() {
        int userId = 1;
        int round = 2;
        List<Game> unwatchedGamesByRound = watchedGamesDao.findUnwatchedGamesByRound(userId, round);

        assertFalse(unwatchedGamesByRound.isEmpty());
    }

    @Test
    public void addAndRemoveWatchedGame_ShouldModifyWatchedGamesCorrectly() {
        int userId = 3;
        int gameId = 3;

        watchedGamesDao.addWatchedGame(userId, gameId);
        assertTrue(watchedGamesDao.isGameWatched(userId, gameId));

        watchedGamesDao.removeWatchedGame(userId, gameId);
        assertFalse(watchedGamesDao.isGameWatched(userId, gameId));
    }

    @Test
    public void markAllGamesWatched_ShouldMarkAllGamesAsWatchedForUser() {
        int userId = 2;

        watchedGamesDao.markAllGamesWatched(userId);

        List<Game> watchedGames = watchedGamesDao.findWatchedGames(userId);
        assertFalse(watchedGames.isEmpty());

        Integer totalGames = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM games", Integer.class);
        int totalGamesCount = totalGames != null ? totalGames : 0;
        assertEquals(totalGamesCount, watchedGames.size());
    }

    @Test
    public void markAllGamesUnwatched_ShouldRemoveAllWatchedGamesForUser() {
        int userId = 2;
        watchedGamesDao.markAllGamesWatched(userId);

        watchedGamesDao.markAllGamesUnwatched(userId);

        List<Game> watchedGames = watchedGamesDao.findWatchedGames(userId);
        assertTrue(watchedGames.isEmpty());
    }
}
