package com.techelevator.dao;

import com.techelevator.model.Team;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.Assert.*;

public class JdbcTeamDaoTests extends BaseDaoTests {

    private JdbcTeamDao jdbcTeamDao;

    @Autowired
    private DataSource dataSource;

    @Before
    public void setup() {
        jdbcTeamDao = new JdbcTeamDao(dataSource);
    }

    @Test
    public void findTeamById_ShouldReturnCorrectTeam() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int teamId = 1;
        jdbcTemplate.update("INSERT INTO teams (team_id, name) VALUES (?, ?) ON CONFLICT (team_id) DO NOTHING", teamId,
                "Team A");

        Team team = jdbcTeamDao.findTeamById(teamId);

        assertNotNull(team);
        assertEquals("Team A", team.getName());
    }

    @Test
    public void findTeamIdByName_ShouldReturnCorrectId() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("INSERT INTO teams (team_id, name) VALUES (?, ?)", 1, "Team A");

        int teamId = jdbcTeamDao.findTeamIdByName("Team A");

        assertEquals(1, teamId);
    }

    @Test
    public void findTeamNameById_ShouldReturnCorrectName() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("INSERT INTO teams (team_id, name) VALUES (?, ?)", 1, "Team A");

        String teamName = jdbcTeamDao.findTeamNameById(1);

        assertEquals("Team A", teamName);
    }

    @Test
    public void findAllTeams_ShouldReturnAllTeams() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("INSERT INTO teams (team_id, name) VALUES (?, ?)", 1, "Team A");
        jdbcTemplate.update("INSERT INTO teams (team_id, name) VALUES (?, ?)", 2, "Team B");

        List<Team> teams = jdbcTeamDao.findAllTeams();

        assertTrue(teams.size() >= 2);
    }
}
