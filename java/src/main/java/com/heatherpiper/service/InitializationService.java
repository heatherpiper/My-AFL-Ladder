package com.heatherpiper.service;

import javax.annotation.PostConstruct;
import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class InitializationService {

    @Autowired
    private SquiggleService squiggleService;

    @PostConstruct
    public void init() {
        int currentYear = Year.now().getValue();
        fetchGamesAsync(currentYear);
    }

    @Async
    public void fetchGamesAsync(int year) {
        squiggleService.fetchGamesUpToMostRecentRound(year);
    }
}
