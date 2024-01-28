package com.techelevator.dao;

import com.techelevator.model.Match;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcMatchDao implements MatchDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcMatchDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Match> getAllMatches() {
        String sql = "SELECT * FROM matches";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        List<Match> matches = new ArrayList<>();
        while (results.next()) {
            Match match = new Match();
            match.setMatchId(results.getInt("match_id"));
            match.setTeam1(results.getString("team1"));
            match.setTeam2(results.getString("team2"));
            match.setRound(results.getInt("round"));
            match.setStartTime(results.getObject("start_time", LocalDateTime.class));
            match.setTeam1PointsScored(results.getObject("team1_points_scored", Integer.class));
            match.setTeam2PointsScored(results.getObject("team2_points_scored", Integer.class));
            match.setIsWatched(results.getBoolean("is_watched"));
            match.setWinner(results.getString("winner"));
            matches.add(match);
        }
        return matches;
    }

    @Override
    public Match getMatchById(int matchId) {
        String sql = "SELECT * FROM matches WHERE match_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, matchId);
        if (results.next()) {
            Match match = new Match();
            match.setMatchId(results.getInt("match_id"));
            match.setTeam1(results.getString("team1"));
            match.setTeam2(results.getString("team2"));
            match.setRound(results.getInt("round"));
            match.setStartTime(results.getObject("start_time", LocalDateTime.class));
            match.setTeam1PointsScored(results.getObject("team1_points_scored", Integer.class));
            match.setTeam2PointsScored(results.getObject("team2_points_scored", Integer.class));
            match.setIsWatched(results.getBoolean("is_watched"));
            match.setWinner(results.getString("winner"));
            return match;
        }
        return null;
    }

    @Override
    public void markMatchWatched(int userId, int matchId) {
        String sql = "INSERT INTO watched_matches (user_id, match_id) VALUES (? ?)";

        jdbcTemplate.update(sql, userId, matchId);
    }

    @Override
    public void markMatchUnwatched(int userId, int matchId) {
        String sql = "DELETE FROM watched_matches WHERE user_id = ? AND match_id = ?";

        jdbcTemplate.update(sql, userId, matchId);
    }

    @Override
    public List<Match> getWatchedMatches(int userId) {
        List<Match> watchedMatches = new ArrayList<>();
        String sql = "SELECT m.* FROM matches m " +
                "INNER JOIN watched_matches wm ON m.match_id = wm.match_id " +
                "WHERE wm.user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Match match = new Match();
            match.setMatchId(results.getInt("match_id"));
            match.setTeam1(results.getString("team1"));
            match.setTeam2(results.getString("team2"));
            match.setRound(results.getInt("round"));
            match.setStartTime(results.getObject("start_time", LocalDateTime.class));
            match.setTeam1PointsScored(results.getObject("team1_points_scored", Integer.class));
            match.setTeam2PointsScored(results.getObject("team2_points_scored", Integer.class));
            match.setIsWatched(results.getBoolean("is_watched"));
            match.setWinner(results.getString("winner"));

            watchedMatches.add(match);
        }
        return watchedMatches;
    }

    @Override
    public List<Match> getUnwatchedMatches(int userId) {
        List<Match> unwatchedMatches = new ArrayList<>();
        String sql = "SELECT m.* FROM matches m " +
                "LEFT JOIN watched_matches wm ON m.match_id = wm.match_id AND wm.user_id = ? " +
                "WHERE wm.match_id IS NULL";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Match match = new Match();
            match.setMatchId(results.getInt("match_id"));
            match.setTeam1(results.getString("team1"));
            match.setTeam2(results.getString("team2"));
            match.setRound(results.getInt("round"));
            match.setStartTime(results.getObject("start_time", LocalDateTime.class));
            match.setTeam1PointsScored(results.getObject("team1_points_scored", Integer.class));
            match.setTeam2PointsScored(results.getObject("team2_points_scored", Integer.class));
            match.setIsWatched(false);
            match.setWinner(results.getString("winner"));

            unwatchedMatches.add(match);
        }
        return unwatchedMatches;
    }
}
