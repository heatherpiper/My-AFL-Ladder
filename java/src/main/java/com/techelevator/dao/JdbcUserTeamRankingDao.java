package com.techelevator.dao;

import com.techelevator.model.UserTeamRanking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcUserTeamRankingDao implements UserTeamRankingDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserTeamRankingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserTeamRanking findRankingByUserIdAndTeamId(int userId, int teamId) {
        String sql = "SELECT * FROM user_team_rankings WHERE user_id = ? AND team_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{userId, teamId});

        if (rowSet.next()) {
            UserTeamRanking ranking = new UserTeamRanking();
            ranking.setId(rowSet.getInt("id"));
            ranking.setUserId(rowSet.getInt("user_id"));
            ranking.setTeamId(rowSet.getInt("team_id"));
            ranking.setPoints(rowSet.getInt("points"));
            return ranking;
        } else {
            return null;
        }
    }

    @Override
    public void updateRanking(UserTeamRanking ranking) {

    }

    @Override
    public UserTeamRanking getAllTeamRankings(int userId, int teamId) {

    }
}
