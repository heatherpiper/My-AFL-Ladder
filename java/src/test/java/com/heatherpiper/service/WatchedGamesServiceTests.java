package com.heatherpiper.service;

import com.heatherpiper.dao.*;
import com.heatherpiper.model.Game;
import com.heatherpiper.model.UserLadderEntry;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WatchedGamesServiceTests {

    @Mock
    private JdbcWatchedGamesDao watchedGamesDao;

    @Mock
    private JdbcUserLadderEntryDao userLadderEntryDao;

    @Mock
    private JdbcUserDao userDao;

    @Mock
    private JdbcGameDao gameDao;

    @Mock
    private JdbcTeamDao teamDao;

    @InjectMocks
    private WatchedGamesService watchedGamesService;

    @Test
    void whenGameMarkedAsWatched_withValidGame_ThenLadderIsUpdated() {
        // Arrange
        int userId = 1;
        int gameId = 1;
        int teamAId = 1;
        int teamBId = 2;
        Game mockGame = new Game (1, 1, 2023, 1703277000, "Team A", "Team B", 100, 90, "Team A", 100);
        UserLadderEntry mockEntryTeamA = new UserLadderEntry(1, 1, 4, 110, 1, "Team A", 1, 0, 0, 100, 90);
        UserLadderEntry mockEntryTeamB = new UserLadderEntry(1, 2, 0, 90, 18, "Team B", 0, 1, 0, 90, 100);

        when(userDao.userExists(userId)).thenReturn(true);
        when(gameDao.findGameById(gameId)).thenReturn(mockGame);
        when(watchedGamesDao.isGameWatched(userId, gameId)).thenReturn(false);

        // Mocking teamDao to return valid IDs for team names
        when(teamDao.findTeamIdByName("Team A")).thenReturn(teamAId);
        when(teamDao.findTeamIdByName("Team B")).thenReturn(teamBId);

        // Mocking userLadderEntryDao to return a mock UserLadderEntry for each team
        when(userLadderEntryDao.getUserLadderEntry(userId, teamAId)).thenReturn(mockEntryTeamA);
        when(userLadderEntryDao.getUserLadderEntry(userId, teamBId)).thenReturn(mockEntryTeamB);

        // Act
        watchedGamesService.markGameAsWatchedAndUpdateLadder(userId, gameId);

        // Assert
        verify(watchedGamesDao).addWatchedGame(userId, gameId);
        // Verify that updateUserLadderEntry is called for both teams involved in the game
        verify(userLadderEntryDao, times(2)).updateUserLadderEntry(argThat(entry ->
                entry.getUserId() == userId && (entry.getTeamId() == teamAId || entry.getTeamId() == teamBId)));
    }

    @Test
    void whenGameMarkedAsUnwatched_withValidGame_ThenLadderIsUpdated() {
        // Arrange
        int userId = 1;
        int gameId = 1;
        int teamAId = 1;
        int teamBId = 2;
        Game mockGame = new Game(1, 1, 2023, 1703277000, "Team A", "Team B", 100, 90, "Team A", 100);
        UserLadderEntry mockEntryTeamA = new UserLadderEntry(1, 1, 4, 110, 1, "Team A", 1, 0, 0, 100, 90);
        UserLadderEntry mockEntryTeamB = new UserLadderEntry(1, 2, 0, 90, 18, "Team B", 0, 1, 0, 90, 100);

        when(userDao.userExists(userId)).thenReturn(true);
        when(gameDao.findGameById(gameId)).thenReturn(mockGame);
        when(watchedGamesDao.isGameWatched(userId, gameId)).thenReturn(true); // The game is initially marked as watched

        // Mocking teamDao to return valid IDs for team names
        when(teamDao.findTeamIdByName("Team A")).thenReturn(teamAId);
        when(teamDao.findTeamIdByName("Team B")).thenReturn(teamBId);

        // Mocking userLadderEntryDao to return mock UserLadderEntry objects for each team
        when(userLadderEntryDao.getUserLadderEntry(userId, teamAId)).thenReturn(mockEntryTeamA);
        when(userLadderEntryDao.getUserLadderEntry(userId, teamBId)).thenReturn(mockEntryTeamB);

        // Act
        watchedGamesService.markGameAsUnwatchedAndUpdateLadder(userId, gameId);

        // Assert
        verify(watchedGamesDao).removeWatchedGame(userId, gameId);
        // Verify that updateUserLadderEntry is called for both teams involved in the game
        verify(userLadderEntryDao, times(2)).updateUserLadderEntry(argThat(entry ->
                entry.getUserId() == userId && (entry.getTeamId() == teamAId || entry.getTeamId() == teamBId)));
    }

    @Test
    void whenGameMarkedAsWatched_isAlreadyMarked_ThrowsException() {
        // Arrange
        int userId = 1;
        int gameId = 1;
        when(userDao.userExists(userId)).thenReturn(true);
        when(gameDao.findGameById(gameId)).thenReturn(new Game());
        when(watchedGamesDao.isGameWatched(userId, gameId)).thenReturn(true); // Game already marked as watched

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            watchedGamesService.markGameAsWatchedAndUpdateLadder(userId, gameId);
        });

        // Verify the exception message
        String expectedMessage = "Game is already marked as watched";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        // Verify no attempt made to mark game as watched again or update ladder
        verify(watchedGamesDao, never()).addWatchedGame(anyInt(), anyInt());
        verify(userLadderEntryDao, never()).updateUserLadderEntry(any(UserLadderEntry.class));
    }

    @Test
    void whenGameMarkedAsWatchedSequentially_withValidGames_ThenLadderIsUpdatedForAll() {
        // Arrange
        int userId = 1;
        List<Integer> gameIds = Arrays.asList(1, 2);
        Game mockGame1 = new Game(1, 1, 2023, 1703277000, "Team A", "Team B", 100, 90, "Team A", 100);
        Game mockGame2 = new Game(2, 1, 2023, 1703277000, "Team C", "Team D", 110, 100, "Team C", 100);
        int teamAId = 1, teamBId = 2, teamCId = 3, teamDId = 4;

        when(userDao.userExists(userId)).thenReturn(true);
        when(gameDao.findGameById(1)).thenReturn(mockGame1);
        when(gameDao.findGameById(2)).thenReturn(mockGame2);
        when(watchedGamesDao.isGameWatched(userId, 1)).thenReturn(false);
        when(watchedGamesDao.isGameWatched(userId, 2)).thenReturn(false);

        // Mocking teamDao to return valid IDs for team names
        when(teamDao.findTeamIdByName("Team A")).thenReturn(teamAId);
        when(teamDao.findTeamIdByName("Team B")).thenReturn(teamBId);
        when(teamDao.findTeamIdByName("Team C")).thenReturn(teamCId);
        when(teamDao.findTeamIdByName("Team D")).thenReturn(teamDId);

        // Mocking userLadderEntryDao to return mock UserLadderEntry objects for each team
        when(userLadderEntryDao.getUserLadderEntry(userId, teamAId)).thenReturn(new UserLadderEntry(1, 1, 4, 110, 1, "Team A", 1, 0, 0, 100, 90));
        when(userLadderEntryDao.getUserLadderEntry(userId, teamBId)).thenReturn(new UserLadderEntry(1, 2, 0, 90, 18,
                "Team B", 0, 1, 0, 90, 100));
        when(userLadderEntryDao.getUserLadderEntry(userId, teamCId)).thenReturn(new UserLadderEntry(1, 3, 4, 105, 2,
                "Team C", 1, 0, 0, 105, 100));
        when(userLadderEntryDao.getUserLadderEntry(userId, teamDId)).thenReturn(new UserLadderEntry(1, 4, 0, 100, 17,
                "Team D", 0, 1, 0, 100, 105));

        // Act
        watchedGamesService.markGamesAsWatchedSequentially(userId, gameIds);

        // Assert
        // Verify that each game was marked as watched
        verify(watchedGamesDao).addWatchedGame(userId, 1);
        verify(watchedGamesDao).addWatchedGame(userId, 2);
        // Verify that updateUserLadderEntry is called for each team involved in the games
        verify(userLadderEntryDao, times(4)).updateUserLadderEntry(any(UserLadderEntry.class));
    }

}
