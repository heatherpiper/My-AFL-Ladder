package com.techelevator.dao;

import com.techelevator.model.Team;
import com.techelevator.model.TeamLadderEntry;

import java.util.List;

public interface TeamLadderEntryDao {

    List<TeamLadderEntry> findTeamLadderForUser(int userId);

//    void updatePoints(int userId, int teamId, int gameId, int points);
//
//    int calculateTotalPoints(int userId, int teamId);
//
//    TeamLadderEntry findTeamLadderEntry(int userId, int teamId, int gameId);
//
//    void removeTeamLadderEntry(int userId, int gameId);

    // Include these methods here or implement logic in WatchedGames?


}
