package com.techelevator.dao;

import com.techelevator.model.Team;

import java.util.List;

public interface TeamDao {

    Team findTeamById(int teamId);

    int findTeamIdByName(String name);

    List<Team> findAllTeams();
}
