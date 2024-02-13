package com.techelevator.service;

import com.techelevator.dao.GameDao;
import com.techelevator.model.Game;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private final GameDao gameDao;

    public MatchService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public List<Game> getAllMatches() {
        return gameDao.getAllMatches();
    }

    public Game getMatchById(int matchId) {
        return gameDao.getMatchById(matchId);
    }
}