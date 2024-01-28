package com.techelevator.dao;

import com.techelevator.model.UserTeamRanking;

public interface UserTeamRankingDao {

    UserTeamRanking findRankingByUserIdAndTeamId(int userId, int teamId);

    void updateRanking(UserTeamRanking ranking);

    UserTeamRanking getAllTeamRankings(int userId, int teamId);

}
