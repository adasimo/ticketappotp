package com.adamsimon.partner.service;

import com.adamsimon.partner.interfaces.PartnerController;
import com.adamsimon.partner.interfaces.PartnerDatabaseCallerService;
import com.adamsimon.partner.interfaces.PartnerService;
import com.adamsimon.partner.repository.TicketModuleUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestPartnerDatabaseCallerService {

    @MockBean
    PartnerDatabaseCallerService partnerDatabaseCallerService;
    @MockBean
    PartnerService partnerService;
    @MockBean
    PartnerController partnerController;
    @MockBean
    TicketModuleUserRepository ticketModuleUserRepository;

    @Test
    public void beansShouldNotBeNull() {
        assertNotNull(partnerDatabaseCallerService);
        assertNotNull(partnerService);
        assertNotNull(partnerController);
        assertNotNull(ticketModuleUserRepository);
    }

    @Test
    public void findByTokenTestShouldWork() {
        assertNotNull(ticketModuleUserRepository.findByToken("a5eA4E8F7fgu5hk6af3fsaGgfAteAe46FL3aI67EfN"));
    }
}
