package com.adamsimon.ticket.service;

import com.adamsimon.ticket.TestConfigurationTicketBoot;
import com.adamsimon.ticket.interfaces.PartnerCallerService;
import com.adamsimon.ticket.interfaces.TicketBuyingResolverService;
import com.adamsimon.ticket.interfaces.TicketDatabaseCallerService;
import com.adamsimon.ticket.repository.UserRepositoryToPartner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfigurationTicketBoot.class)
public class TestTicketDataBaseCallerService {
    @MockBean
    PartnerCallerService partnerCallerService;
    @MockBean
    TicketBuyingResolverService ticketBuyingService;
    @MockBean
    TicketDatabaseCallerService ticketDatabaseCallerService;
    @MockBean
    UserRepositoryToPartner userRepositoryToPartner;

    @Test
    public void beansShouldNotBeNull() {
        assertNotNull(partnerCallerService);
        assertNotNull(ticketBuyingService);
        assertNotNull(ticketDatabaseCallerService);
        assertNotNull(userRepositoryToPartner);
    }

    @Test
    public void findByTokenTestShouldWork() {
        assertNotNull(userRepositoryToPartner.findByToken("a5eA4E8F7fgu5hk6af3fsaGgfAteAe46FL3aI67EfN"));
    }
}
