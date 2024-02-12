package com.techelevator.controller;

import java.util.List;

import com.techelevator.model.Match;
import com.techelevator.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/matches")
public class MatchController {

    private MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @RequestMapping (path = "/matches", method = RequestMethod.GET)
    public List<Match> getMatches() {
        return matchService.getAllMatches();
    }

//    @GetMapping
//    public ResponseEntity<List<Match>> getAllMatches() {
//        List<Match> matches = matchService.getAllMatches();
//        if(matches.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(matches);
//    }

   @GetMapping("/{matchId}")
    public ResponseEntity<Match> getMatchById(@PathVariable int matchId) {
        Match match = matchService.getMatchById(matchId);
        if(match == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(match);
    }

}
