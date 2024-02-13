package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Game;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class JdbcGameDao implements GameDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcGameDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<Game> gameRowMapper = (rs, rowNum) -> {
        Game game = new Game();
        game.setId(rs.getInt("id"));
        game.setRound(rs.getInt("round"));
        game.setYear(rs.getInt("year"));
        game.setHteam(rs.getString("hteam"));
        game.setAteam(rs.getString("ateam"));
        game.setHscore(rs.getObject("hscore", Integer.class));
        game.setAscore(rs.getObject("ascore", Integer.class));
        game.setWinner(rs.getString("winner"));
        game.setComplete(rs.getInt("complete"));
        return game;
    };

    public List<Game> findAllGames() {
        String sql = "SELECT id, round, year, hteam, ateam, hscore, ascore, winner, complete FROM games";
        return jdbcTemplate.query(sql, gameRowMapper);
    }

    public List<Game> findGamesByRound(int round) {
        String sql = "SELECT id, round, year, hteam, ateam, hscore, ascore, winner, complete FROM games WHERE round = ?";
        return jdbcTemplate.query(sql, gameRowMapper, round);
    }

    public List<Game> findCompleteGames() {
        String sql = "SELECT id, round, year, hteam, ateam, hscore, ascore, winner, complete FROM games WHERE complete = 100";
        return jdbcTemplate.query(sql, gameRowMapper);
    }

    public List<Game> findIncompleteGames() {
        String sql = "SELECT id, round, year, hteam, ateam, hscore, ascore, winner, complete FROM games WHERE complete != 100";
        return jdbcTemplate.query(sql, gameRowMapper);
    }

    public String findWinnerByGameId(int id) {
        String sql = "SELECT winner FROM games WHERE id = ?";
        Object[] params = new Object[]{id};
        try {
            return jdbcTemplate.queryForObject(sql, params, String.class);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Error accessing data");
        }
    }
}
