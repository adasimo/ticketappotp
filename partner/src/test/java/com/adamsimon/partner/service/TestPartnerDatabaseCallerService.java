package com.adamsimon.partner.service;

import com.adamsimon.partner.domain.TicketModuleUser;
import com.adamsimon.partner.repository.TicketModuleUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestPartnerDatabaseCallerService {

    @InjectMocks
    private PartnerDatabaseCallerServiceImpl partnerDatabaseCallerService;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private TicketModuleUserRepository ticketModuleUserRepository;

    @Before
    public void setup() {
        when(ticketModuleUserRepository.findByToken("shouldReturn")).thenReturn(Optional.of(new TicketModuleUser()));
        when(ticketModuleUserRepository.findByToken("shouldBeNull")).thenReturn(Optional.empty());
    }

    @Test
    public void propertiesShouldBeInitialized() {
        assertNotNull(partnerDatabaseCallerService);
        assertNotNull(cacheManager);
        assertNotNull(ticketModuleUserRepository);
    }

    @Test
    public void getUserByTokenShouldNotBeNull() {
        assertNotNull(partnerDatabaseCallerService.getUserByToken("shouldReturn"));
    }

    @Test
    public void getUserByTokenShouldBeNull() {
        assertNull(partnerDatabaseCallerService.getUserByToken("shouldBeNull"));
    }
}
