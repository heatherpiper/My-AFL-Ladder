package com.techelevator.dao;

import com.techelevator.model.Team;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcTeamDao implements TeamDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTeamDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Team> getAllTeams() {
        String sql = "SELECT team_id, name FROM teams";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Team team = new Team();
            team.setTeamId(rs.getInt("team_id"));
            team.setName(rs.getString("name"));
            return team;
        });
    }

    @Override
    public Team getTeamById(int teamId) {
        String sql = "SELECT team_id, name FROM teams WHERE team_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{teamId}, (rs, rowNum) -> {
            Team team = new Team();
            team.setTeamId(rs.getInt("team_id"));
            team.setName(rs.getString("name"));
            return team;
        });
    }

    // Implement other methods as needed
}
