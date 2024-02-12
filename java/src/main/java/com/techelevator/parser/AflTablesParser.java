package com.techelevator.parser;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AflTablesParser {

    private static final String URL = "jdbc:postgresql://localhost/final_capstone";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres1";

    public static void main(String[] args) {
        try {
            JSONArray aflData = readJsonFromFile("C:\\Users\\Piper\\Projects\\My-AFL-Ladder\\scraper\\afltables_scraper\\season2023.json");
            insertDataIntoDB(aflData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONArray readJsonFromFile(String filePath) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONArray(content);
    }

    public static void insertDataIntoDB(JSONArray aflData) {

        String sql = "INSERT INTO ladder2023_data (round, team, games_played, total_points, score_differential) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Regular expression to extract round number
            Pattern pattern = Pattern.compile("\\bRd (\\d+)\\b");

            for (int i = 0; i < aflData.length(); i++) {
                JSONObject obj = aflData.getJSONObject(i);
                String roundStr = obj.getString("round");

                Matcher matcher = pattern.matcher(roundStr);
                int roundNumber = 0;
                if (matcher.find()) {
                    roundNumber = Integer.parseInt(matcher.group(1));
                }

                pstmt.setInt(1, roundNumber);
                pstmt.setString(2, obj.getString("team"));
                pstmt.setInt(3, obj.getInt("games_played"));
                pstmt.setInt(4, obj.getInt("total_points"));
                pstmt.setDouble(5, obj.getDouble("score_differential"));
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
