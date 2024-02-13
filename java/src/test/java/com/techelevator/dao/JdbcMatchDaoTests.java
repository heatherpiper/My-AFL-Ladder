package com.techelevator.dao;

import com.techelevator.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcGameDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SqlRowSet sqlRowSet;

    @InjectMocks
    private JdbcGameDao jdbcMatchDao;

    @BeforeEach
    void setUp() {
        when(sqlRowSet.next()).thenReturn(true, false);
        when(sqlRowSet.getInt("match_id")).thenReturn(1);
        when(sqlRowSet.getInt("team1_id")).thenReturn(101);
        when(sqlRowSet.getInt("team2_id")).thenReturn(102);
        when(sqlRowSet.getString("team1_name")).thenReturn("Team One");
        when(sqlRowSet.getString("team2_name")).thenReturn("Team Two");
        when(sqlRowSet.getInt("winner_id")).thenReturn(101);
        when(sqlRowSet.getString("winner_name")).thenReturn("Team One");
        when(sqlRowSet.getInt("round")).thenReturn(1);
        when(sqlRowSet.getObject("start_time", LocalDateTime.class)).thenReturn(LocalDateTime.now());
        when(sqlRowSet.getObject("team1_points_scored", Integer.class)).thenReturn(100);
        when(sqlRowSet.getObject("team2_points_scored", Integer.class)).thenReturn(90);

        // Configure JdbcTemplate to return the mock SqlRowSet
        when(jdbcTemplate.queryForRowSet(anyString())).thenReturn(sqlRowSet);
    }

    @Test
    void getAllMatches_ShouldCallJdbcTemplateAndReturnMatches() {

        List<Game> games = jdbcMatchDao.getAllMatches();

        verify(jdbcTemplate).queryForRowSet(anyString());
        assertNotNull(games);
        assertEquals(1, games.size());

        Game game = games.get(0);
        assertNotNull(game);
        assertEquals(1, game.getMatchId());
        assertEquals(101, game.getTeam1Id());
        assertEquals(102, game.getTeam2Id());
        assertEquals("Team One", game.getTeam1Name());
        assertEquals("Team Two", game.getTeam2Name());
        assertEquals(Integer.valueOf(101), game.getWinnerId());
        assertEquals("Team One", game.getWinnerName());
        assertEquals(1, game.getRound());
        assertNotNull(game.getStartTime());
        assertEquals(100, game.getTeam1PointsScored());
        assertEquals(90, game.getTeam2PointsScored());
    }
}

