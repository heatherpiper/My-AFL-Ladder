package com.techelevator.dao;


import com.techelevator.model.Match;

import java.util.List;

public interface MatchDao {

    List<Match> getAllMatches();

    Match getMatchById(int matchId);

}
