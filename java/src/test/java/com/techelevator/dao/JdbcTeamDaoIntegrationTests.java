package com.techelevator.dao;

import com.techelevator.model.Team;
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

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {TestingDatabaseConfig.class, JdbcTeamDao.class})
public class JdbcTeamDaoIntegrationTests {

    @Autowired
    private JdbcTeamDao jdbcTeamDao;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Test
    public void findTeamById_ShouldReturnCorrectTeam() {
        int teamId = 1;

        Team team = jdbcTeamDao.findTeamById(teamId);

        assertNotNull(team);
        assertEquals("Team A", team.getName());
    }

    @Test
    public void findTeamIdByName_ShouldReturnCorrectId() {
        String teamName = "Team A";

        int teamId = jdbcTeamDao.findTeamIdByName(teamName);

        assertEquals(1, teamId);
    }

    @Test
    public void findTeamNameById_ShouldReturnCorrectName() {
        int teamId = 1;

        String teamName = jdbcTeamDao.findTeamNameById(teamId);

        assertNotNull(teamName);
        assertEquals("Team A", teamName);
    }

    @Test
    public void findAllTeams_ShouldReturnAllTeams() {
        List<Team> teams = jdbcTeamDao.findAllTeams();

        assertNotNull(teams);
        assertFalse(teams.isEmpty());
        assertEquals(4, teams.size());
    }
}
