package com.heatherpiper.dao;

import com.heatherpiper.exception.DaoException;
import com.heatherpiper.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class JdbcGameDao implements GameDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcGameDao.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcGameDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Game> gameRowMapper = (rs, rowNum) -> {
        Game game = new Game();
        game.setId(rs.getInt("id"));
        game.setRound(rs.getInt("round"));
        game.setYear(rs.getInt("year"));
        game.setUnixtime(rs.getInt("unixtime"));
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

    @Retryable(value = EmptyResultDataAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Game fetchGameDetails(int id) {
        String sql = "SELECT id, round, year, unixtime, hteam, ateam, hscore, ascore, winner, complete FROM games " +
                "WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, gameRowMapper);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Game with ID " + id + " not found", e);
            throw new DaoException("Game with ID " + id + " not found", e);
        }
    }

    @Retryable(value = DataAccessException.class, maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 2))
    @Override
    public void saveAll(List<Game> games) {
        String sql = "INSERT INTO games (id, round, year, unixtime, hteam, ateam, hscore, ascore, winner, complete) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
                "ON CONFLICT (id) DO UPDATE SET " +
                "round = EXCLUDED.round, " +
                "year = EXCLUDED.year, " +
                "unixtime = EXCLUDED.unixtime, " +
                "hteam = EXCLUDED.hteam, " +
                "ateam = EXCLUDED.ateam, " +
                "hscore = EXCLUDED.hscore, " +
                "ascore = EXCLUDED.ascore, " +
                "winner = EXCLUDED.winner, " +
                "complete = EXCLUDED.complete;";

        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Game game = games.get(i);
                    ps.setInt(1, game.getId());
                    ps.setInt(2, game.getRound());
                    ps.setInt(3, game.getYear());
                    ps.setInt(4, game.getUnixtime());
                    ps.setString(5, game.getHteam());
                    ps.setString(6, game.getAteam());
                    ps.setObject(7, game.getHscore());
                    ps.setObject(8, game.getAscore());
                    ps.setString(9, game.getWinner());
                    ps.setInt(10, game.getComplete());
                }

                public int getBatchSize() {
                    return games.size();
                }
            });
        } catch (DataAccessException e) {
            logger.error("Failed to save games batch due to a database access issue", e);
            throw new DaoException("Failed to save games batch", e);
        }
    }
}
