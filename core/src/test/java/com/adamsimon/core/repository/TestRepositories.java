package com.adamsimon.core.repository;

import com.adamsimon.core.TestConfigurationCoreBoot;
import com.adamsimon.core.service.DatabaseHandlerServiceImpl;
import com.adamsimon.core.service.HelperServiceImpl;
import com.adamsimon.core.service.IncomingRequestServiceImpl;
import com.adamsimon.core.service.OutgoingServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfigurationCoreBoot.class)
public class TestRepositories {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserCardRepository userCardRepository;
    @MockBean
    private DatabaseHandlerServiceImpl databaseHandlerService;
    @MockBean
    private CacheManager cacheManager;
    @MockBean
    private HelperServiceImpl helperService;
    @MockBean
    private IncomingRequestServiceImpl incomingRequestService;
    @MockBean
    private OutgoingServiceImpl outgoingService;

    @Test
    public void beansShouldNotBeNull() {
        assertNotNull(userCardRepository);
        assertNotNull(usersRepository);
        assertNotNull(databaseHandlerService);
        assertNotNull(cacheManager);
        assertNotNull(helperService);
        assertNotNull(incomingRequestService);
        assertNotNull(outgoingService);
    }

    @Test
    public void findByTokenTestShouldWork() {
        assertNotNull(usersRepository.findByToken("QGa7r8JL7HKV7gUrKfXyjILjSqQ7VkO8"));
    }

    @Test
    public void findByCardIdShouldWork() {
        assertNotNull(userCardRepository.findByCardId("C0001"));
    }

    @Test
    public void findByUserIdAndCardIdShouldWork() {
        assertNotNull(userCardRepository.findByUserIdAndCardId(1000L, "C0001"));
    }
}
