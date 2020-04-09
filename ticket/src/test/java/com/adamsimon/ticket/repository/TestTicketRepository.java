package com.adamsimon.ticket.repository;

import com.adamsimon.ticket.TestConfigurationTicketBoot;
import com.adamsimon.ticket.service.PartnerCallerServiceImpl;
import com.adamsimon.ticket.service.TicketDatabaseCallerServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfigurationTicketBoot.class)
public class TestTicketRepository {

    @Autowired
    private UserRepositoryToPartner userRepositoryToPartner;
    @MockBean
    private PartnerCallerServiceImpl partnerCallerService;
    @MockBean
    private TicketDatabaseCallerServiceImpl ticketDatabaseCallerService;

    @Test
    public void beansShouldNotBeNull() {
        assertNotNull(userRepositoryToPartner);
        assertNotNull(partnerCallerService);
        assertNotNull(ticketDatabaseCallerService);
    }

    @Test
    public void findByTokenTestShouldWork() {
        assertNotNull(userRepositoryToPartner.findByToken("a5eA4E8F7fgu5hk6af3fsaGgfAteAe46FL3aI67EfN"));
    }
}
