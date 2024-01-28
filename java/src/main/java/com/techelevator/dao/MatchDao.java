package com.techelevator.dao;


import com.techelevator.model.Match;

import java.util.List;

public interface MatchDao {

    List<Match> getAllMatches();

    Match getMatchById(int matchId);

    void markMatchWatched(int userId, int matchId);

    List<Match> getWatchedMatches(int userId);

    void markMatchUnwatched(int userId, int matchId);

    List<Match>getUnwatchedMatches(int userId);



}
