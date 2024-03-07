package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Team;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

@Component
public class JdbcTeamDao implements TeamDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTeamDao.class);

    private JdbcTemplate jdbcTemplate;

    public JdbcTeamDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Team findTeamById(int teamId) {
        String sql = "SELECT team_id, name FROM teams WHERE team_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{teamId}, (rs, rowNum) -> {
            Team team = new Team();
            team.setTeamId(rs.getInt("team_id"));
            team.setName(rs.getString("name"));
            return team;
        });
    }

    @Override
    public int findTeamIdByName(String name) {
        String sql = "SELECT team_id FROM teams WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, name);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No team found with name: {}", name);
            return -1;
        } catch (DataAccessException e) {
            logger.error("Data access error when trying to find team by name: {}", name, e);
            throw new DaoException("Data access error when trying to find team by name: " + name, e);
        }
    }

    @Override
    public String findTeamNameById(int teamId) {
        String sql = "SELECT name FROM teams WHERE team_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, teamId);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No team found with ID: {}", teamId);
            return null;
        } catch (DataAccessException e) {
            logger.error("Data access error when trying to find team by ID: {}", teamId, e);
            throw new DaoException("Data access error when trying to find team by ID: " + teamId, e);
        }
    }

    @Override
    public List<Team> findAllTeams() {
        String sql = "SELECT team_id, name FROM teams";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Team team = new Team();
            team.setTeamId(rs.getInt("team_id"));
            team.setName(rs.getString("name"));
            return team;
        });
    }

    // Created for testing purposes
    @Override
    public int saveTeam(String teamName) {
        String sql = "INSERT INTO teams (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"team_id"});
            ps.setString(1, teamName);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            return key.intValue();
        } else {
            throw new DataAccessResourceFailureException("Failed to obtain team_id after saving team");
        }
    }
}
