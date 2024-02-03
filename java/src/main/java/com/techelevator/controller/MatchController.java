package com.techelevator.controller;

import java.util.List;
import com.techelevator.dao.MatchDao;
import com.techelevator.model.Match;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private MatchDao MatchDao;

    public MatchController(MatchDao matchDao) {
        this.MatchDao = matchDao;
    }

    public List<Match> getAllMatches() {
        return MatchDao.getAllMatches();
    }

    @GetMapping("/{matchId}")
    public Match getMatchById(@PathVariable int matchId) {
        return MatchDao.getMatchById(matchId);
    }

}
