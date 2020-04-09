package com.adamsimon.core.service;

import com.adamsimon.core.domain.User;
import com.adamsimon.core.domain.UserBankCard;
import com.adamsimon.core.repository.UserCardRepository;
import com.adamsimon.core.repository.UsersRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cache.CacheManager;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestDatabaseHandlerService {

    @InjectMocks
    private DatabaseHandlerServiceImpl databaseHandlerService;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private UserCardRepository userCardRepository;

    @Before
    public void setup() {
        when(usersRepository.findByToken("true")).thenReturn(Optional.of(new User()));
        when(usersRepository.findByToken("false")).thenReturn(Optional.empty());
        final UserBankCard userBankCard = new UserBankCard();
        userBankCard.setAmount(new BigDecimal(1000));
        when(userCardRepository.findByCardId("true")).thenReturn(Optional.of(userBankCard));
        when(userCardRepository.findByCardId("false")).thenReturn(Optional.empty());
        when(userCardRepository.findByUserIdAndCardId(1L, "true")).thenReturn(Optional.of(new UserBankCard()));
        when(userCardRepository.findByUserIdAndCardId(1L, "false")).thenReturn(Optional.empty());
    }

    @Test
    public void shouldInit() {
        assertNotNull(databaseHandlerService);
        assertNotNull(cacheManager);
        assertNotNull(usersRepository);
        assertNotNull(userCardRepository);
    }

    @Test
    public void getUserFromAuthTokenShouldNotBeNull() {
        assertNotNull(databaseHandlerService.getUserFromAuthToken("true"));
    }

    @Test
    public void getUserFromAuthTokenShouldBeNull() {
        assertNull(databaseHandlerService.getUserFromAuthToken("false"));
    }

    @Test
    public void getIfUserIdOwnsCardIdShouldNotBeNull() {
        assertNotNull(databaseHandlerService.getIfUserIdOwnsCardId(1L,"true"));
    }

    @Test
    public void getIfUserIdOwnsCardIdShouldBeNull() {
        assertNotNull(databaseHandlerService.getIfUserIdOwnsCardId(1L,"false"));
    }

    @Test
    public void getAmountFromCardIdShouldNotBeNull() {
        final BigDecimal amount = databaseHandlerService.getAmountFromCardId("true");
        assertNotNull(amount);
        assertTrue(amount.compareTo(new BigDecimal(0)) > 0);

    }

    @Test
    public void getAmountFromCardIdShouldBeNull() {
        assertNull(databaseHandlerService.getAmountFromCardId("false"));
    }
}
