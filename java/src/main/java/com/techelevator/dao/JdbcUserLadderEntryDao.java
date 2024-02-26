package com.techelevator.dao;

import com.techelevator.model.UserLadderEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserLadderEntryDao implements UserLadderEntryDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcUserLadderEntryDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addUserLadderEntry(UserLadderEntry userLadderEntry) {
        String sql = "INSERT INTO user_ladder (user_id, team_id, points, percentage, position, team_name, wins, " +
                "losses, draws, points_for, points_against) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                userLadderEntry.getUserId(),
                userLadderEntry.getTeamId(),
                userLadderEntry.getPoints(),
                userLadderEntry.getPercentage(),
                userLadderEntry.getPosition(),
                userLadderEntry.getTeamName(),
                userLadderEntry.getWins(),
                userLadderEntry.getLosses(),
                userLadderEntry.getDraws(),
                userLadderEntry.getPointsFor(),
                userLadderEntry.getPointsAgainst());
    }

    @Override
    public void updateUserLadderEntry(UserLadderEntry userLadderEntry) {
        String sql = "UPDATE user_ladder SET points = ?, percentage = ?, position = ?, wins = ?, losses = ?, draws = " +
                "?, points_for = ?, points_against = ? WHERE user_id = ? AND team_id = ?";
        jdbcTemplate.update(sql,
                userLadderEntry.getPoints(),
                userLadderEntry.getPercentage(),
                userLadderEntry.getPosition(),
                userLadderEntry.getWins(),
                userLadderEntry.getLosses(),
                userLadderEntry.getDraws(),
                userLadderEntry.getPointsFor(),
                userLadderEntry.getPointsAgainst(),
                userLadderEntry.getUserId(),
                userLadderEntry.getTeamId());
    }


    public void deleteUserLadderEntry(int userId, int teamId) {
        String sql = "DELETE FROM user_ladder WHERE user_id = ? AND team_id = ?";
        jdbcTemplate.update(sql, userId, teamId);
    }

    @Override
    public UserLadderEntry getUserLadderEntry(int userId, int teamId) {
        String sql = "SELECT * FROM user_ladder WHERE user_id = ? AND team_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, teamId);
        if (results.next()) {
            return mapRowToUserLadder(results);
        } else {
            return null;
        }
    }

    @Override
    public List<UserLadderEntry> getAllUserLadderEntries(int userId) {
        List<UserLadderEntry> teamLadder = new ArrayList<>();
        String sql = "SELECT u.*, t.name AS team_name, u.wins, u.losses, u.draws, u.points_for, u.points_against " +
                "FROM user_ladder u " +
                "JOIN teams t ON u.team_id = t.team_id " +
                "WHERE u.user_id = ? " +
                "ORDER BY u.points DESC, u.percentage DESC";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            teamLadder.add(mapRowToUserLadder(results));
        }
        return teamLadder;
    }

    private UserLadderEntry mapRowToUserLadder(SqlRowSet row) {
        UserLadderEntry userLadder = new UserLadderEntry(row.getInt("user_id"),
                row.getInt("team_id"), row.getInt("points"), row.getDouble("percentage"), row.getInt("position"),
                row.getString("team_name"), row.getInt("wins"), row.getInt("losses"), row.getInt("draws"),
                row.getInt("points_for"), row.getInt("points_against"));
        return userLadder;
    }
}
