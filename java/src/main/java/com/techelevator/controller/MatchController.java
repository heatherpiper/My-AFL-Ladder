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

    public List<Match> getAllMatches() {
        return matchDao.getAllMatches();
    }

    @GetMapping("/{matchId}")
    public Match getMatchById(@PathVariable int matchId) {
        return matchDao.getMatchById(matchId);
    }

    @PostMapping("/watched")
    public void markMatchWatched(@RequestParam int userId, @RequestParam int matchId) {
        matchDao.markMatchWatched(userId, matchId);
    }

    @GetMapping("/watched")
    public List<Match> getWatchedMatches(@RequestParam int userId) {
        return matchDao.getWatchedMatches(userId);
    }

    @GetMapping("/unwatched")
    public List<Match> getUnwatchedMatches(@RequestParam int userId) {
        return matchDao.getUnwatchedMatches(userId);
    }

}
