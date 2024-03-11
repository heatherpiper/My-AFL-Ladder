package com.heatherpiper.dao;

import com.heatherpiper.model.Team;

import java.util.List;

public interface TeamDao {

    Team findTeamById(int teamId);

    int findTeamIdByName(String name);

    String findTeamNameById(int teamId);

    List<Team> findAllTeams();

    int saveTeam(String teamName);
}
