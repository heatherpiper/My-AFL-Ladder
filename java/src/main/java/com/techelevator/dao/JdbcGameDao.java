package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Game;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    @Override
    public Game findGameById(int id) {
        String sql = "SELECT * FROM games WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, gameRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Error accessing data");
        }
    }

    @Override
    public List<Game> findAllGames() {
        String sql = "SELECT * FROM games";
        return jdbcTemplate.query(sql, gameRowMapper);
    }

    @Override
    public List<Game> findGamesByRound(int round) {
        String sql = "SELECT * FROM games WHERE round = ?";
        return jdbcTemplate.query(sql, gameRowMapper, round);
    }

    @Override
    public List<Game> findCompleteGames() {
        String sql = "SELECT * FROM games WHERE complete = 100";
        return jdbcTemplate.query(sql, gameRowMapper);
    }

    @Override
    public List<Game> findIncompleteGames() {
        String sql = "SELECT * FROM games WHERE complete != 100";
        return jdbcTemplate.query(sql, gameRowMapper);
    }

    @Override
    public String findWinnerByGameId(int id) {
        String sql = "SELECT winner FROM games WHERE id = ?";
        Object[] params = new Object[]{id};
        try {
            return jdbcTemplate.queryForObject(sql, params, String.class);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Error accessing data");
        }
    }

    public Game fetchGameDetails(int id) {
        String sql = "SELECT id, round, year, hteam, ateam, hscore, ascore, winner, complete FROM games WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, gameRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Error accessing data");
        }
    }

    @Override
    public void saveAll(List<Game> games) {
        String sql = "INSERT INTO games (id, round, year, hteam, ateam, hscore, ascore, winner, complete) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Game game = games.get(i);
                ps.setInt(1, game.getId());
                ps.setInt(2, game.getRound());
                ps.setInt(3, game.getYear());
                ps.setString(4, game.getHteam());
                ps.setString(5, game.getAteam());
                ps.setObject(6, game.getHscore());
                ps.setObject(7, game.getAscore());
                ps.setString(8, game.getWinner());
                ps.setInt(9, game.getComplete());
            }

            public int getBatchSize() {
                return games.size();
            }
        });
    }
}
