package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Team;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class JdbcTeamDao implements TeamDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTeamDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Team> getAllTeams() {
        String sql = "SELECT * FROM teams";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        List<Team> teams = new ArrayList<>();
        while (results.next()) {
            Team team = new Team();
            team.setTeamId(results.getInt("team_id"));
            team.setName(results.getString("name"));

            teams.add(team);
        }
        return teams;
    }

    @Override
    public Team getTeamById(int teamId) {
        String sql = "SELECT team_id, name FROM teams WHERE team_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, teamId);

            if (results.next()) {
                Team team = new Team();
                team.setTeamId(results.getInt("team_id"));
                team.setName(results.getString("name"));
                return team;
            }
            return null;
        } catch (DataAccessException e) {
            throw new DaoException("Error accessing data");
        }
    }
}
