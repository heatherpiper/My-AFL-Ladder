package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Team;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class JdbcTeamDao implements TeamDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTeamDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Team findTeamById(int teamId) {
        String sql = "SELECT team_id, name FROM teams WHERE id = ?";
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
            return jdbcTemplate.queryForObject(sql, new Object[]{name}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Error Accessing data");
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
}
