package com.techelevator.service;

import javax.annotation.PostConstruct;
import java.time.Year;

import com.techelevator.model.YearAndRound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitializationService {

    @Autowired
    private SquiggleService squiggleService;

    @PostConstruct
    public void init() {
        int currentYear = Year.now().getValue();

        squiggleService.fetchGamesUpToMostRecentRound(currentYear);
    }
}
