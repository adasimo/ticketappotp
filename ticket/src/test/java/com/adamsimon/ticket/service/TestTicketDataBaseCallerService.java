package com.adamsimon.ticket.service;

import com.adamsimon.ticket.domain.UserToPartner;
import com.adamsimon.ticket.repository.UserRepositoryToPartner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestTicketDataBaseCallerService {
    @InjectMocks
    private TicketDatabaseCallerServiceImpl ticketDatabaseCallerService;
    @Mock
    private UserRepositoryToPartner userRepositoryToPartner;
    @Mock
    private CacheManager cacheManager;

    @Before
    public void setup() {
        final UserToPartner userToPartner = new UserToPartner();
        userToPartner.setToken("token");
        when(userRepositoryToPartner.findById(any())).thenReturn(Optional.of(userToPartner));
    }

    @Test
    public void shouldInit() {
        assertNotNull(ticketDatabaseCallerService);
        assertNotNull(userRepositoryToPartner);
        assertNotNull(cacheManager);
    }

    @Test
    public void getTokenShouldGiveToken() {
        assertNotNull(ticketDatabaseCallerService.getToken());
    }

}
