package com.heatherpiper.dao;

import java.util.List;

import com.heatherpiper.model.UserLadderEntry;

public interface UserLadderEntryDao {
    void addUserLadderEntry(UserLadderEntry userLadderEntry);

    void updateUserLadderEntry(UserLadderEntry userLadderEntry);

    void updateUserLadderEntries(List<UserLadderEntry> userLadderEntries);

    void deleteUserLadderEntry(int userId, int teamId);

    UserLadderEntry getUserLadderEntry(int userId, int teamId);

    List<UserLadderEntry> getAllUserLadderEntries(int userId);
}
