package com.techelevator.dao;

import com.techelevator.model.Match;
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
class JdbcMatchDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SqlRowSet sqlRowSet;

    @InjectMocks
    private JdbcMatchDao jdbcMatchDao;

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

        List<Match> matches = jdbcMatchDao.getAllMatches();

        verify(jdbcTemplate).queryForRowSet(anyString());
        assertNotNull(matches);
        assertEquals(1, matches.size());

        Match match = matches.get(0);
        assertNotNull(match);
        assertEquals(1, match.getMatchId());
        assertEquals(101, match.getTeam1Id());
        assertEquals(102, match.getTeam2Id());
        assertEquals("Team One", match.getTeam1Name());
        assertEquals("Team Two", match.getTeam2Name());
        assertEquals(Integer.valueOf(101), match.getWinnerId());
        assertEquals("Team One", match.getWinnerName());
        assertEquals(1, match.getRound());
        assertNotNull(match.getStartTime());
        assertEquals(100, match.getTeam1PointsScored());
        assertEquals(90, match.getTeam2PointsScored());
    }
}

