package com.techelevator.service;

import javax.annotation.PostConstruct;
import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitializationService {

    @Autowired
    private SquiggleService squiggleService;

    @PostConstruct
    public void init() {
        int year = Year.now().getValue();
        int round = squiggleService.getMostRecentlyPlayedRound();

        squiggleService.fetchGamesForRound(year, round);
    }}
