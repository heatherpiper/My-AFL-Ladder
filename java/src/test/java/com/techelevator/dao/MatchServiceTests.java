package com.techelevator.dao;

import com.techelevator.dao.MatchDao;
import com.techelevator.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

class MatchServiceTest {

    @Mock
    private MatchDao matchDao;

    @InjectMocks
    private MatchService matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMatches_ShouldUseMatchDao() {

        when(matchDao.getAllMatches()).thenReturn(new ArrayList<>());

        matchService.getAllMatches();

        verify(matchDao).getAllMatches();
    }
}
