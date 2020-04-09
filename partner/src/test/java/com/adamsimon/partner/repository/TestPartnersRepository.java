package com.adamsimon.partner.repository;

import com.adamsimon.partner.TestConfigurationPartnerBoot;
import com.adamsimon.partner.controller.PartnerControllerImpl;
import com.adamsimon.partner.service.PartnerDatabaseCallerServiceImpl;
import com.adamsimon.partner.service.PartnerServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestConfigurationPartnerBoot.class)
public class TestPartnersRepository {

    @Autowired
    private TicketModuleUserRepository ticketModuleUserRepository;
    @MockBean
    private CacheManager cacheManager;
    @MockBean
    private PartnerDatabaseCallerServiceImpl partnerDatabaseCallerService;
    @MockBean
    private PartnerServiceImpl partnerService;
    @MockBean
    private PartnerControllerImpl partnerController;
    @Test
    public void beansShouldNotBeNull() {
        assertNotNull(ticketModuleUserRepository);
        assertNotNull(cacheManager);
        assertNotNull(partnerDatabaseCallerService);
        assertNotNull(partnerService);
        assertNotNull(partnerController);
    }

    @Test
    public void findByTokenTestShouldWork() {
        assertNotNull(ticketModuleUserRepository.findByToken("a5eA4E8F7fgu5hk6af3fsaGgfAteAe46FL3aI67EfN"));
    }
}
