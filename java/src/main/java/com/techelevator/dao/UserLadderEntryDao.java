package com.techelevator.dao;

import java.util.List;

import com.techelevator.model.UserLadderEntry;

public interface UserLadderEntryDao {
    void addUserLadderEntry(UserLadderEntry userLadderEntry);

    void updateUserLadderEntry(UserLadderEntry userLadderEntry);

    void deleteUserLadderEntry(int userId, int teamId);

    UserLadderEntry getUserLadderEntry(int userId, int teamId);

    List<UserLadderEntry> getAllUserLadderEntries(int userId);

}
