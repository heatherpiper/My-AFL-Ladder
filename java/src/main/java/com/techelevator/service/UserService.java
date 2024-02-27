package com.techelevator.service;

import com.techelevator.dao.JdbcTeamDao;
import com.techelevator.dao.UserLadderEntryDao;
import com.techelevator.model.UserLadderEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserLadderEntryDao userLadderEntryDao;

    @Autowired
    private JdbcTeamDao jdbcTeamDao;

    public void createDefaultUserLadder(int userId) {
        for (int teamId = 1; teamId <= 18; teamId++) {
            UserLadderEntry defaultEntry = new UserLadderEntry();
            defaultEntry.setUserId(userId);
            defaultEntry.setTeamId(teamId);
            defaultEntry.setPoints(0);
            defaultEntry.setPercentage(100);
            defaultEntry.setPosition(0);

            String teamName = jdbcTeamDao.findTeamNameById(teamId);
            defaultEntry.setTeamName(teamName);

            defaultEntry.setWins(0);
            defaultEntry.setLosses(0);
            defaultEntry.setDraws(0);
            defaultEntry.setPointsFor(0);
            defaultEntry.setPointsAgainst(0);

            userLadderEntryDao.addUserLadderEntry(defaultEntry);
        }
    }

}