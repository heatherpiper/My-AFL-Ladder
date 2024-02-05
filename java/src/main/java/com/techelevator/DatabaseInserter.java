package com.techelevator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class DatabaseInserter {
    public static void insertData(List<Ladder2023> dataList) {
        String jdbcURL = "jdbc:postgresql://localhost:5432/final_capstone";
        String username = "postgres";
        String password = "postgres1";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            String sql = "INSERT INTO afl_data (round, team, games_played, total_points, score_differential) VALUES (?, ?, ?, ?, ?)";

            for (Ladder2023 data : dataList) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, data.getRound());
                statement.setString(2, data.getTeam());
                statement.setInt(3, data.getGamesPlayed());
                statement.setInt(4, data.getTotalPoints());
                statement.setDouble(5, data.getScoreDifferential());
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
