package com.techelevator.service;

import com.techelevator.dao.MatchDao;
import com.techelevator.model.Match;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private final MatchDao matchDao;

    public MatchService(MatchDao matchDao) {
        this.matchDao = matchDao;
    }

    public List<Match> getAllMatches() {
        return matchDao.getAllMatches();
    }

    public Match getMatchById(int matchId) {
        return matchDao.getMatchById(matchId);
    }
}