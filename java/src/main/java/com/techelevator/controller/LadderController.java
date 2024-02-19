package com.techelevator.controller;

import com.techelevator.dao.UserLadderEntryDao;
import com.techelevator.model.UserLadderEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ladder")
public class LadderController {

    private final UserLadderEntryDao userLadderEntryDao;

    @Autowired
    public LadderController(UserLadderEntryDao userLadderEntryDao) {
        this.userLadderEntryDao = userLadderEntryDao;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserLadderEntry>> getMostRecentLadderEntriesByTeam(@PathVariable("userId") int userId) {
        List<UserLadderEntry> userLadderEntries = userLadderEntryDao.findMostRecentLadderEntriesByTeam(userId);
        return ResponseEntity.ok(userLadderEntries);
    }
}
