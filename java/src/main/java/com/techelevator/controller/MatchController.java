package com.techelevator.controller;

import java.util.List;
import com.techelevator.dao.MatchDao;
import com.techelevator.model.Match;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private MatchDao matchDao;

    public MatchController(MatchDao matchDao) {
        this.matchDao = matchDao;
    }

    @PostMapping("/watched")
    public void markMatchAsWatched(@RequestParam int userId, @RequestParam int matchId) {
        matchDao.addWatchedMatch(userId, matchId);
    }

    @GetMapping("/watched")
    public List<Match> getWatchedMatches(@RequestParam int userId) {
        return matchDao.getWatchedMatches(userId);
    }
}
