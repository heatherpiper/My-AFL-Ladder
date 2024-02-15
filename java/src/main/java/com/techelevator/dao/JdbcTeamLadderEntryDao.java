package com.techelevator.dao;

import com.techelevator.model.TeamLadderEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class JdbcTeamLadderEntryDao implements TeamLadderEntryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTeamLadderEntryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TeamLadderEntry> findTeamLadderForUser(int userId) {
        String sql = "SELECT * FROM team_ladder WHERE user_id = ?";
        return jdbcTemplate.query(sql, teamLadderEntryRowMapper, userId);
    }

    @Override
    public void updatePoints(int userId, int teamId, int gameId, int points) {
        String sql = "UPDATE team_ladder SET points = ? WHERE user_id = ? AND team_id = ? AND game_id = ?";
        jdbcTemplate.update(sql, points, userId, teamId, gameId);
    }

    @Override
    public int calculateTotalPoints(int userId, int teamId) {
        String sql = "SELECT SUM(points) FROM team_ladder WHERE user_id = ? AND team_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, teamId);
    }

    @Override
    public TeamLadderEntry findTeamLadderEntry(int userId, int teamId, int gameId) {
        String sql = "SELECT * FROM team_ladder WHERE user_id = ? AND team_id = ? AND game_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, teamLadderEntryRowMapper, userId, teamId, gameId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void removePoints(int userId, int gameId) {
        String sql = "DELETE FROM team_ladder WHERE user_id = ? AND game_id = ?";
        jdbcTemplate.update(sql, userId, gameId);
    }

    private final RowMapper<TeamLadderEntry> teamLadderEntryRowMapper = (rs, rowNum) -> {
        TeamLadderEntry teamLadderEntry = new TeamLadderEntry();
        teamLadderEntry.setUserId(rs.getInt("user_id"));
        teamLadderEntry.setTeamId(rs.getInt("team_id"));
        teamLadderEntry.setGameId(rs.getInt("game_id"));
        teamLadderEntry.setPoints(rs.getInt("points"));
        teamLadderEntry.setTotalPoints(rs.getInt("total_points"));
        return teamLadderEntry;
    };
}
