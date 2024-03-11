package com.heatherpiper.dao;

import com.heatherpiper.model.UserLadderEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserLadderEntryDao implements UserLadderEntryDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUserLadderEntryDao.class);

    private JdbcTemplate jdbcTemplate;

    public JdbcUserLadderEntryDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addUserLadderEntry(UserLadderEntry userLadderEntry) {
        String sql = "INSERT INTO user_ladder (user_id, team_id, points, percentage, position, team_name, wins, " +
                "losses, draws, points_for, points_against) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            logger.debug("Attempting to add user ladder entry for userId: {}, teamId: {}, TeamName: {}",
                    userLadderEntry.getUserId(), userLadderEntry.getTeamId(), userLadderEntry.getTeamName());

            int rowsAffected = jdbcTemplate.update(sql,
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

            if (rowsAffected > 0) {
                logger.info("Successfully added user ladder entry for userId: {}, teamId: {}",
                        userLadderEntry.getUserId(), userLadderEntry.getTeamId());
            } else {
                logger.warn("No user ladder entry was added for userId: {}, teamId: {}. Check if the input data is correct.",
                        userLadderEntry.getUserId(), userLadderEntry.getTeamId());
            }
        } catch (DataAccessException e) {
            logger.error("Exception while adding user ladder entry for userId: {}, teamId: {}",
                    userLadderEntry.getUserId(), userLadderEntry.getTeamId(), e);
            throw e;
        }
    }

    @Override
    public void updateUserLadderEntry(UserLadderEntry userLadderEntry) {
        String sql = "UPDATE user_ladder SET points = ?, percentage = ?, position = ?, wins = ?, losses = ?, draws = " +
                "?, points_for = ?, points_against = ? WHERE user_id = ? AND team_id = ?";
        try {
            logger.debug("Attempting to update user ladder entry for userId: {}, teamId: {} with values: Points = {}, " +
                            "Percentage = {}, Position = {}, Wins = {}, Losses = {}, Draws = {}, PointsFor = {}, PointsAgainst = {}",
                    userLadderEntry.getUserId(), userLadderEntry.getTeamId(), userLadderEntry.getPoints(),
                    userLadderEntry.getPercentage(), userLadderEntry.getPosition(), userLadderEntry.getWins(),
                    userLadderEntry.getLosses(), userLadderEntry.getDraws(), userLadderEntry.getPointsFor(),
                    userLadderEntry.getPointsAgainst());

            int updatedRows = jdbcTemplate.update(sql,
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

            if (updatedRows > 0) {
                logger.info("Successfully updated user ladder entry for userId: {}, teamId: {}", userLadderEntry.getUserId(), userLadderEntry.getTeamId());
            } else {
                logger.warn("No user ladder entry was updated for userId: {}, teamId: {}. This may indicate the entry does not exist.", userLadderEntry.getUserId(), userLadderEntry.getTeamId());
            }
        } catch (DataAccessException e) {
            logger.error("Exception while updating user ladder entry for userId: {}, teamId: {}", userLadderEntry.getUserId(), userLadderEntry.getTeamId(), e);
            throw e;
        }
    }


    public void deleteUserLadderEntry(int userId, int teamId) {
        String sql = "DELETE FROM user_ladder WHERE user_id = ? AND team_id = ?";
        try {
            logger.debug("Attempting to delete user ladder entry for userId: {}, teamId: {}", userId, teamId);

            int rowsAffected = jdbcTemplate.update(sql, userId, teamId);
            if (rowsAffected > 0) {
                logger.info("Successfully deleted user ladder entry for userId: {}, teamId: {}", userId, teamId);
            } else {
                logger.warn("No user ladder entry was deleted for userId: {}, teamId: {}. This may indicate the entry does not exist.", userId, teamId);
            }
        } catch (DataAccessException e) {
            logger.error("Exception while deleting user ladder entry for userId: {}, teamId: {}", userId, teamId, e);
            throw e;
        }
    }

    @Override
    public UserLadderEntry getUserLadderEntry(int userId, int teamId) {
        String sql = "SELECT * FROM user_ladder WHERE user_id = ? AND team_id = ?";
        try {
            logger.debug("Fetching user ladder entry for userId: {}, teamId: {}", userId, teamId);

            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, teamId);
            if (results.next()) {
                UserLadderEntry entry = mapRowToUserLadder(results);
                logger.info("Found user ladder entry for userId: {}, teamId: {}", userId, teamId);
                return entry;
            } else {
                logger.warn("No user ladder entry found for userId: {}, teamId: {}", userId, teamId);
                return null;
            }
        } catch (DataAccessException e) {
            logger.error("Exception while fetching user ladder entry for userId: {}, teamId: {}", userId, teamId, e);
            throw e;
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
        try {
            logger.debug("Fetching all user ladder entries for userId: {}", userId);

            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                teamLadder.add(mapRowToUserLadder(results));
            }

            logger.info("Found {} ladder entries for userId: {}", teamLadder.size(), userId);
            return teamLadder;
        } catch (DataAccessException e) {
            logger.error("Exception while fetching all user ladder entries for userId: {}", userId, e);
            throw e;
        }
    }

    private UserLadderEntry mapRowToUserLadder(SqlRowSet row) {
        UserLadderEntry userLadder = new UserLadderEntry(row.getInt("user_id"),
                row.getInt("team_id"), row.getInt("points"), row.getDouble("percentage"), row.getInt("position"),
                row.getString("team_name"), row.getInt("wins"), row.getInt("losses"), row.getInt("draws"),
                row.getInt("points_for"), row.getInt("points_against"));
        return userLadder;
    }
}
