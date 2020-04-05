package com.adamsimon.ticket.service;

import com.adamsimon.ticket.domain.UserToPartner;
import com.adamsimon.ticket.interfaces.TicketDatabaseCallerService;
import com.adamsimon.ticket.repository.UserRepositoryToPartner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketDatabaseCallerServiceImpl implements TicketDatabaseCallerService {

    @Autowired
    private final UserRepositoryToPartner userRepositoryToPartner;
    @Autowired
    private final CacheManager cacheManager;
    private final Logger logger = LoggerFactory.getLogger(TicketDatabaseCallerServiceImpl.class);

    public TicketDatabaseCallerServiceImpl(UserRepositoryToPartner userRepositoryToPartner,
                                           CacheManager cacheManager) {
        this.userRepositoryToPartner = userRepositoryToPartner;
        this.cacheManager = cacheManager;
    }

    @Override
    @Cacheable("partnerToken")
    public String getToken() {
        Optional<UserToPartner> optionalUserToPartner = this.userRepositoryToPartner.findById(1L);
        logger.info("Token to Partner found: " + optionalUserToPartner.isPresent());
        return optionalUserToPartner.map(UserToPartner::getToken).orElse(null);
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
