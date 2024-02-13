package com.techelevator.dao;

import com.techelevator.model.LadderRound2023;

import java.util.List;

public interface LadderRound2023Dao {
    List<LadderRound2023> getAllLadder2023Data();

    LadderRound2023 getLadderByRoundNumber(int round);

    LadderRound2023 getPointsByLadderRound(int round);

    LadderRound2023 getPointsByLadderRoundAndTeam (int round, String team);

    LadderRound2023 getTeamByLadderRound (int round);
}
