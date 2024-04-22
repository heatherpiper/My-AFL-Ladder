package com.heatherpiper.dao;

import com.heatherpiper.model.Game;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class JdbcWatchedGamesDao implements WatchedGamesDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcWatchedGamesDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<Game> gameRowMapper = (rs, rowNum) -> {
        Game game = new Game();
        game.setId(rs.getInt("id"));
        game.setRound(rs.getInt("round"));
        game.setYear(rs.getInt("year"));
        game.setDate(rs.getString("date"));
        game.setHteam(rs.getString("hteam"));
        game.setAteam(rs.getString("ateam"));
        game.setHscore(rs.getObject("hscore", Integer.class));
        game.setAscore(rs.getObject("ascore", Integer.class));
        game.setWinner(rs.getString("winner"));
        game.setComplete(rs.getInt("complete"));
        return game;
    };

    @Override
    public List<Game> findWatchedGames(int userId) {
        String sql = "SELECT g.* FROM games g INNER JOIN watched_games wg ON g.id = wg.game_id WHERE wg.user_id = ? " +
                "ORDER BY g.date ASC";
        return jdbcTemplate.query(sql, gameRowMapper, userId);
    }

    @Override
    public List<Game> findUnwatchedGames(int userId) {
        String sql = "SELECT g.* FROM games g " +
                "LEFT JOIN watched_games wg ON g.id = wg.game_id AND wg.user_id = ? " +
                "WHERE wg.game_id IS NULL " +
                "ORDER BY g.date ASC";
        return jdbcTemplate.query(sql, new Object[]{userId}, gameRowMapper);
    }

    @Override
    public List<Game> findUnwatchedGamesByRound(int userId, int round) {
        String sql = "SELECT g.* FROM games g " +
                "LEFT JOIN watched_games wg ON g.id = wg.game_id AND wg.user_id = ? " +
                "WHERE wg.game_id IS NULL AND g.round = ?";
        return jdbcTemplate.query(sql, new Object[]{userId, round}, gameRowMapper);
    }

    @Override
    public void addWatchedGame(int userId, int gameId) {
        String sql = "INSERT INTO watched_games (user_id, game_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, gameId);
    }

    @Override
    public void removeWatchedGame(int userId, int gameId) {
        String sql = "DELETE FROM watched_games WHERE user_id = ? AND game_id = ?";
        jdbcTemplate.update(sql, userId, gameId);
    }

    @Override
    public void markAllGamesWatched(int userId) {
        // check if game already exists in watched_list for user; if not, insert into watched_games
        String sql = "INSERT INTO watched_games (user_id, game_id) " +
                "SELECT ?, g.id FROM games g " +
                "WHERE NOT EXISTS ( " +
                "    SELECT 1 FROM watched_games wg " +
                "    WHERE wg.game_id = g.id AND wg.user_id = ?)";
        jdbcTemplate.update(sql, userId, userId); // pass userId twice to fill both placeholders
    }

    @Override
    public void markAllGamesUnwatched(int userId) {
        String sql = "DELETE FROM watched_games WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public void markAllGamesInRoundWatched(int userId, int round) {
        String sql = "INSERT INTO watched_games (user_id, game_id) " +
                "SELECT ?, g.id FROM games g " +
                "WHERE g.round = ? AND NOT EXISTS (" +
                "SELECT 1 FROM watched_games wg WHERE wg.game_id = g.id AND wg.user_id = ?)";
        jdbcTemplate.update(sql, userId, round, userId);
    }

    @Override
    public void markAllGamesInRoundUnwatched(int userId, int round) {
        String sql = "DELETE FROM watched_games WHERE user_id = ? AND game_id IN (" +
                "SELECT id FROM games WHERE round = ?)";
        jdbcTemplate.update(sql, userId, round);
    }

    @Override
    public boolean isGameWatched(int userId, int gameId) {
        String sql = "SELECT COUNT(*) FROM watched_games WHERE user_id = ? AND game_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, gameId}, Integer.class);
        return count != null && count > 0;
    }
}
