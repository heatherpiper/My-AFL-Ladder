package com.techelevator.model;

import java.time.LocalDateTime;

public class Match {

    private int matchId;
    private String team1;
    private String team2;
    private int round;
    private LocalDateTime startTime;
    private Integer team1PointsScored;
    private Integer team2PointsScored;
    private boolean isWatched;
    private String winner;

    public Match(int matchId, String team1, String team2, int round, LocalDateTime startTime, int venueId, Integer team1PointsScored, Integer team2PointsScored, boolean isWatched, String winner) {
        this.matchId = matchId;
        this.team1 = team1;
        this.team2 = team2;
        this.round = round;
        this.startTime = startTime;
        this.team1PointsScored = team1PointsScored;
        this.team2PointsScored = team2PointsScored;
        this.isWatched = isWatched;
        this.winner = winner;
    }

    public Match() {

    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getTeam1PointsScored() {
        return team1PointsScored;
    }

    public void setTeam1PointsScored(int team1PointsScored) {
        this.team1PointsScored = team1PointsScored;
    }

    public int getTeam2PointsScored() {
        return team2PointsScored;
    }

    public void setTeam2PointsScored(int team2PointsScored) {
        this.team2PointsScored = team2PointsScored;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setIsWatched(boolean isWatched) {
        this.isWatched = isWatched;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
