package com.techelevator.dao;

import com.techelevator.model.Team;

import java.util.List;

public interface TeamDao {
    List<Team> getAllTeams();

    Team getTeamById(int teamId);
}
