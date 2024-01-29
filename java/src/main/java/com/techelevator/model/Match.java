package com.techelevator.model;

import java.time.LocalDateTime;

public class Match {

    private int matchId;
    private int team1Id;
    private int team2Id;
    private String team1Name;
    private String team2Name;
    private Integer winnerId;
    private String winnerName;
    private int round;
    private LocalDateTime startTime;
    private Integer team1PointsScored;
    private Integer team2PointsScored;

    public Match(int matchId, int team1Id , int team2Id, Integer winnerId, int round, LocalDateTime startTime, int venueId, Integer team1PointsScored, Integer team2PointsScored) {
        this.matchId = matchId;
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.round = round;
        this.startTime = startTime;
        this.team1PointsScored = team1PointsScored;
        this.team2PointsScored = team2PointsScored;
        this.winnerId = winnerId;
    }

    public Match() {

    }

    public int getTeam1Id() {
        return team1Id;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public void setTeam1PointsScored(Integer team1PointsScored) {
        this.team1PointsScored = team1PointsScored;
    }

    public void setTeam2PointsScored(Integer team2PointsScored) {
        this.team2PointsScored = team2PointsScored;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getTeam1() {
        return team1Id;
    }

    public void setTeam1Id(int team1Id) {
        this.team1Id = team1Id;
    }

    public int getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(int team2Id) {
        this.team2Id = team2Id;
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

    public Integer getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Integer winner) {
        this.winnerId = winner;
    }
}
