package com.heatherpiper.controller;

import com.heatherpiper.dao.UserLadderEntryDao;
import com.heatherpiper.model.UserLadderEntry;
import com.heatherpiper.service.WatchedGamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/ladder")
public class LadderController {

    private final UserLadderEntryDao userLadderEntryDao;
    private final WatchedGamesService watchedGamesService;

    @Autowired
    public LadderController(UserLadderEntryDao userLadderEntryDao, WatchedGamesService watchedGamesService) {
        this.userLadderEntryDao = userLadderEntryDao;
        this.watchedGamesService = watchedGamesService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserLadderEntry>> getAllUserLadderEntries(@PathVariable int userId) {
        List<UserLadderEntry> userLadderEntries = userLadderEntryDao.getAllUserLadderEntries(userId);
        return ResponseEntity.ok(userLadderEntries);
    }

    @PostMapping("/{userId/reset")
    public ResponseEntity<?> resetLadderAndMarkAllGamesUnwatched(@PathVariable int userId) {
        try {
            watchedGamesService.resetUserLadderAndMarkAllGamesUnwatched(userId);
            return new ResponseEntity<>("Ladder reset and all games marked as unwatched successfully for user " + userId, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred while attempting to reset the ladder and mark " +
                    "all games as unwatched for user " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
