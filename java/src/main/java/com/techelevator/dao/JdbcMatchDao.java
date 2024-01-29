package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Match;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.*;

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
        String sql = "SELECT m.match_id, m.round, m.start_time, m.team1_points_scored, m.team2_points_scored, " +
                "t1.name as team1_name, t2.name as team2_name, tw.name as winner_name " +
                "FROM matches m " +
                "LEFT JOIN teams t1 ON m.team1_id = t1.team_id " +
                "LEFT JOIN teams t2 ON m.team2_id = t2.team_id " +
                "LEFT JOIN teams tw ON m.winner_id = tw.team_id";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        try {
            List<Match> matches = new ArrayList<>();
            while (results.next()) {
                Match match = new Match();
                match.setMatchId(results.getInt("match_id"));
                match.setTeam1Id(results.getInt("team1_id"));
                match.setTeam2Id(results.getInt("team2_id"));
                match.setTeam1Name(results.getString("team1_name"));
                match.setTeam2Name(results.getString("team2_name"));
                match.setWinnerId(results.getInt("winner_id"));
                match.setWinnerName(results.getString("winner_name"));
                match.setRound(results.getInt("round"));
                match.setStartTime(results.getObject("start_time", LocalDateTime.class));
                match.setTeam1PointsScored(results.getObject("team1_points_scored", Integer.class));
                match.setTeam2PointsScored(results.getObject("team2_points_scored", Integer.class));

                matches.add(match);
            }
            return matches;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        }
    }

    @Override
    public Match getMatchById(int matchId) {
        String sql = "SELECT m.match_id, m.team1_id, m.team2_id, m.round, m.start_time, m.team1_points_scored, m.team2_points_scored, m.winner_id, t1.name AS team1_name, t2.name AS team2_name, tw.name AS winner_name " +
                "FROM matches m " +
                "LEFT JOIN teams t1 ON m.team1_id = t1.team_id " +
                "LEFT JOIN teams t2 ON m.team2_id = t2.team_id " +
                "LEFT JOIN teams tw ON m.winner_id = tw.team_id " +
                "WHERE m.match_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, matchId);

            if (results.next()) {
                Match match = new Match();
                match.setMatchId(results.getInt("match_id"));
                match.setTeam1Id(results.getInt("team1_id"));
                match.setTeam2Id(results.getInt("team2_id"));
                match.setTeam1Name(results.getString("team1_name"));
                match.setTeam2Name(results.getString("team2_name"));
                match.setWinnerId(results.getInt("winner_id"));
                match.setWinnerName(results.getString("winner_name"));
                match.setRound(results.getInt("round"));
                match.setStartTime(results.getObject("start_time", LocalDateTime.class));
                match.setTeam1PointsScored(results.getObject("team1_points_scored", Integer.class));
                match.setTeam2PointsScored(results.getObject("team2_points_scored", Integer.class));
                return match;
            }
            return null;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database.", e);
        } catch (DataAccessException e) {
            throw new DaoException("Error accessing data");
        }
    }
}
