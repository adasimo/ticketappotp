package com.adamsimon.partner.service;

import com.adamsimon.partner.domain.TicketModuleUser;
import com.adamsimon.partner.interfaces.PartnerDatabaseCallerService;
import com.adamsimon.partner.repository.TicketModuleUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PartnerDatabaseCallerServiceImpl implements PartnerDatabaseCallerService {


    @Autowired
    private final CacheManager cacheManager;
    @Autowired
    private final TicketModuleUserRepository ticketModuleUserRepository;
    private final Logger logger = LoggerFactory.getLogger(PartnerDatabaseCallerServiceImpl.class);

    public PartnerDatabaseCallerServiceImpl(TicketModuleUserRepository ticketModuleUserRepository,
                                            CacheManager cacheManager) {
        this.ticketModuleUserRepository = ticketModuleUserRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    @Cacheable("partnerUser")
    public TicketModuleUser getUserByToken(String token) {
        final Optional<TicketModuleUser> optionalTicketModuleUser = this.ticketModuleUserRepository.findByToken(token);
        logger.info("Partner User Got: " + optionalTicketModuleUser.isPresent());
        return optionalTicketModuleUser.orElse(null);
    }

    @Override
    public void evictCacheOnSchedule() {
        logger.info("Evicting all caches on schedule...");
        this.cacheManager.getCacheNames().stream()
                .forEach(cacheName -> {
                    logger.info("Evicting cache " + cacheName + " on schedule...");
                    cacheManager.getCache(cacheName).clear();
                });
    }
}
