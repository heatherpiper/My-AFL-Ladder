package com.techelevator.service;

import com.techelevator.dao.GameDao;
import com.techelevator.dao.TeamDao;
import com.techelevator.dao.UserLadderEntryDao;
import com.techelevator.dao.WatchedGamesDao;
import com.techelevator.model.Game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.argThat;


@ExtendWith(MockitoExtension.class)
class WatchedGamesServiceTests {

    @Mock
    private WatchedGamesDao watchedGamesDao;

    @Mock
    private GameDao gameDao;

    @Mock
    private TeamDao teamDao;

    @Mock
    private UserLadderEntryDao userLadderEntryDao;

    @InjectMocks
    private WatchedGamesService watchedGamesService;

    @Test
    void markGameAsWatched_AddsGameAndUpdatesUserLadder() {
        int userId = 1;
        int gameId = 1;
        Game game = new Game(1, 1, 2024, "Home Team", "Away Team", 100, 50, "Home Team", 100);
        int hteamId = 1;
        int ateamId = 2;

        when(gameDao.findGameById(gameId)).thenReturn(game);
        when(teamDao.findTeamIdByName("Home Team")).thenReturn(hteamId);
        when(teamDao.findTeamIdByName("Away Team")).thenReturn(ateamId);

        watchedGamesService.markGameAsWatched(userId, gameId);

        verify(watchedGamesDao, times(1)).addWatchedGame(userId, gameId);
        verify(userLadderEntryDao, times(1)).updateUserLadderEntry(argThat(entry -> 
            entry.getUserId() == userId && 
            entry.getTeamId() == hteamId && 
            entry.getPoints() == 4
        ));
        verify(userLadderEntryDao, times(1)).updateUserLadderEntry(argThat(entry -> 
            entry.getUserId() == userId && 
            entry.getTeamId() == ateamId && 
            entry.getPoints() == 0
        ));
    }
}