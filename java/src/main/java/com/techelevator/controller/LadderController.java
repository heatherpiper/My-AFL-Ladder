package com.techelevator.controller;

import com.techelevator.dao.UserLadderEntryDao;
import com.techelevator.model.UserLadderEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/ladder")
public class LadderController {

    private final UserLadderEntryDao userLadderEntryDao;

    @Autowired
    public LadderController(UserLadderEntryDao userLadderEntryDao) {
        this.userLadderEntryDao = userLadderEntryDao;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserLadderEntry>> getAllUserLadderEntries(@PathVariable int userId) {
        List<UserLadderEntry> userLadderEntries = userLadderEntryDao.getAllUserLadderEntries(userId);
        return ResponseEntity.ok(userLadderEntries);
    }
}
