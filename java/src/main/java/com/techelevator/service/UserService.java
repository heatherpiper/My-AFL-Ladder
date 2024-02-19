package com.techelevator.service;

import com.techelevator.dao.UserLadderEntryDao;
import com.techelevator.model.UserLadderEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserLadderEntryDao userLadderEntryDao;

    public void createDefaultUserLadder(int userId) {
        for (int teamId = 1; teamId <= 18; teamId++) {
            UserLadderEntry defaultEntry = new UserLadderEntry();
            defaultEntry.setUserId(userId);
            defaultEntry.setTeamId(teamId);
            defaultEntry.setPoints(0);
            defaultEntry.setPercentage(0);
            defaultEntry.setPosition(0);

            userLadderEntryDao.addUserLadderEntry(defaultEntry);
        }
    }

}