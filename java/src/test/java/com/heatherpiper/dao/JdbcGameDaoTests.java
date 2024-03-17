package com.heatherpiper.dao;

import com.heatherpiper.exception.DaoException;
import com.heatherpiper.model.Game;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JdbcGameDaoTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private JdbcGameDao jdbcGameDao;

    private Game expectedGame;

    @Before
    public void setup() {
        jdbcGameDao = new JdbcGameDao(jdbcTemplate);

        expectedGame = new Game();
        expectedGame.setId(1);
        expectedGame.setYear(2023);
        expectedGame.setUnixtime(1703277000);
        expectedGame.setRound(1);
        expectedGame.setHteam("Team A");
        expectedGame.setAteam("Team B");
        expectedGame.setHscore(100);
        expectedGame.setAscore(90);
        expectedGame.setWinner("Team A");
        expectedGame.setComplete(100);
    }

    @Test
    public void findGameById_WhenGameExists_ReturnsGame() {
        when(jdbcTemplate.queryForObject(any(String.class), any(RowMapper.class), eq(1)))
                .thenReturn(expectedGame);

        Game result = jdbcGameDao.findGameById(1);

        assertEquals(expectedGame, result);
    }

    @Test(expected = DaoException.class)
    public void findGameById_WhenGameDoesNotExist_ThrowsException() {
        when(jdbcTemplate.queryForObject(any(String.class), any(RowMapper.class), eq(999)))
                .thenThrow(new EmptyResultDataAccessException(1));

        jdbcGameDao.findGameById(999);
    }

    @Test
    public void findAllGames_ReturnsListOfGames() {
        List<Game> mockGames = new ArrayList<>();

        Game mockGame1 = new Game();
        mockGame1.setId(1);

        Game mockGame2 = new Game();
        mockGame2.setId(2);

        mockGames.add(mockGame1);
        mockGames.add(mockGame2);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(mockGames);

        List<Game> result = jdbcGameDao.findAllGames();

        assertFalse(result.isEmpty());
        assertEquals(mockGames.size(), result.size());
        verify(jdbcTemplate).query(eq("SELECT * FROM games"), any(RowMapper.class));
    }

    @Test
    public void findGamesByRound_ReturnsGamesForRound() {
        int round = 1;
        Game mockGame = new Game();
        mockGame.setRound(round);
        List<Game> mockGames = Arrays.asList(mockGame);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(round))).thenReturn(mockGames);

        List<Game> result = jdbcGameDao.findGamesByRound(round);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(round, result.get(0).getRound());
    }

    @Test
    public void findIncompleteGames_ReturnsIncompleteGames() {
        List<Game> mockIncompleteGames = Arrays.asList(new Game());
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(mockIncompleteGames);

        List<Game> result = jdbcGameDao.findIncompleteGames();

        assertFalse(result.isEmpty());
        verify(jdbcTemplate).query(eq("SELECT * FROM games WHERE complete != 100"), any(RowMapper.class));
    }

    @Test
    public void saveAll_SavesAllGames() {
        Game mockGame1 = new Game();
        mockGame1.setId(1);
        mockGame1.setRound(5);
        mockGame1.setYear(2021);
        mockGame1.setUnixtime(1703277000);
        mockGame1.setHteam("Team A");
        mockGame1.setAteam("Team B");
        mockGame1.setHscore(100);
        mockGame1.setAscore(80);
        mockGame1.setWinner("Team A");
        mockGame1.setComplete(100);

        Game mockGame2 = new Game();
        mockGame2.setId(2);
        mockGame2.setRound(5);
        mockGame2.setYear(2021);
        mockGame2.setUnixtime(1703277000);
        mockGame2.setHteam("Team C");
        mockGame2.setAteam("Team D");
        mockGame2.setHscore(90);
        mockGame2.setAscore(95);
        mockGame2.setWinner("Team D");
        mockGame2.setComplete(100);

        List<Game> gamesToSave = Arrays.asList(mockGame1, mockGame2);

        jdbcGameDao.saveAll(gamesToSave);

        verify(jdbcTemplate).batchUpdate(anyString(), any(BatchPreparedStatementSetter.class));
    }

}
