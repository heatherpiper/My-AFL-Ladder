package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Game;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcGameDao implements GameDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcGameDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Game> findAllGames() {
        String sql = "SELECT id, round, year, hteam, ateam, hscore, ascore, winner, complete FROM games";
        List<Game> games = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Game game = new Game();
            game.setId(rs.getInt("id"));
            game.setRound(rs.getInt("round"));
            game.setYear(rs.getInt("year"));
            game.setHteam(rs.getString("hteam"));
            game.setAteam(rs.getString("ateam"));

            // Check for null score values
            Integer hscore = rs.getObject("hscore", Integer.class);
            if (hscore != null) {
                game.setHscore(hscore);
            }
            Integer ascore = rs.getObject("ascore", Integer.class);
            if (ascore != null) {
                game.setAscore(ascore);
            }

            game.setWinner(rs.getString("winner"));
            game.setComplete(rs.getInt("complete"));
            return game;
        });
        return games;
    }

    public List<Game> findGamesByRound(int round) {
        String sql = "SELECT id, round, year, hteam, ateam, hscore, ascore, winner, complete FROM games WHERE round = ?";
        Object[] params = new Object[]{round};
        List<Game> games = jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Game game = new Game();
            game.setId(rs.getInt("id"));
            game.setRound(rs.getInt("round"));
            game.setYear(rs.getInt("year"));
            game.setHteam(rs.getString("hteam"));
            game.setAteam(rs.getString("ateam"));

            Integer hscore = rs.getObject("hscore", Integer.class);
            if (hscore != null) {
                game.setHscore(hscore);
            }
            Integer ascore = rs.getObject("ascore", Integer.class);
            if (ascore != null) {
                game.setAscore(ascore);
            }

            game.setWinner(rs.getString("winner"));
            game.setComplete(rs.getInt("complete"));
            return game;
        });
        return games;
    }

    public List<Game> findCompleteGames() {
        String sql = "SELECT id, round, year, hteam, ateam, hscore, ascore, winner, complete FROM games WHERE " +
                "complete = 100";
        List<Game> games = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Game game = new Game();
            game.setId(rs.getInt("id"));
            game.setRound(rs.getInt("round"));
            game.setYear(rs.getInt("year"));
            game.setHteam(rs.getString("hteam"));
            game.setAteam(rs.getString("ateam"));

            // Check for null score values
            Integer hscore = rs.getObject("hscore", Integer.class);
            if (hscore != null) {
                game.setHscore(hscore);
            }
            Integer ascore = rs.getObject("ascore", Integer.class);
            if (ascore != null) {
                game.setAscore(ascore);
            }

            game.setWinner(rs.getString("winner"));
            game.setComplete(rs.getInt("complete"));
            return game;
        });
        return games;
    }

    public List<Game> findIncompleteGames() {
        String sql = "SELECT id, round, year, hteam, ateam, hscore, ascore, winner, complete FROM games WHERE " +
                "complete != 100";
        List<Game> games = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Game game = new Game();
            game.setId(rs.getInt("id"));
            game.setRound(rs.getInt("round"));
            game.setYear(rs.getInt("year"));
            game.setHteam(rs.getString("hteam"));
            game.setAteam(rs.getString("ateam"));

            // Check for null score values
            Integer hscore = rs.getObject("hscore", Integer.class);
            if (hscore != null) {
                game.setHscore(hscore);
            }
            Integer ascore = rs.getObject("ascore", Integer.class);
            if (ascore != null) {
                game.setAscore(ascore);
            }

            game.setWinner(rs.getString("winner"));
            game.setComplete(rs.getInt("complete"));
            return game;
        });
        return games;
    }

    public String findWinnerByGameId(int id) {
        String sql = "SELECT winner FROM games WHERE id = ?";
        Object[] params = new Object[]{id};
        try {
            String winner = jdbcTemplate.queryForObject(sql, params, String.class);
            return winner;
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Error accessing data");
        }

    }

}
