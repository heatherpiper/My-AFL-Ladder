package com.techelevator.dao;

import com.techelevator.model.TeamLadderEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class JdbcTeamLadderEntryDao implements TeamLadderEntryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTeamLadderEntryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TeamLadderEntry> findTeamLadderForUser(int userId) {
        String sql = "SELECT * FROM team_ladder WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            TeamLadderEntry entry = new TeamLadderEntry();
            entry.setUserId(rs.getInt("user_id"));
            entry.setTeamId(rs.getInt("team_id"));
            entry.setPoints(rs.getInt("points"));
            entry.setTotalPoints(rs.getInt("total_points"));
            return entry;
        });
    }
}
