package com.adamsimon.core.service;

import com.adamsimon.core.domain.User;
import com.adamsimon.core.domain.UserBankCard;
import com.adamsimon.core.interfaces.DatabaseHandlerService;
import com.adamsimon.core.repository.UserCardRepository;
import com.adamsimon.core.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class DatabaseHandlerServiceImpl implements DatabaseHandlerService {

    @Autowired
    private final CacheManager cacheManager;
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final UserCardRepository userCardRepository;
    private final Logger logger = LoggerFactory.getLogger(DatabaseHandlerServiceImpl.class);

    public DatabaseHandlerServiceImpl(final UsersRepository usersRepository,
                                      final UserCardRepository userCardRepository,
                                      final CacheManager cacheManager) {
        this.usersRepository = usersRepository;
        this.userCardRepository = userCardRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    @Cacheable("user")
    public User getUserFromAuthToken(final String token) {
        final Optional<User> user = this.usersRepository.findByToken(token);
        logger.info("User found: " + user.isPresent());
        return user.orElse(null);
    }

    @Override
    @Cacheable("userOwnsCard")
    public Boolean getIfUserIdOwnsCardId(final Long userId, final String cardId) {
        logger.info("getIfUserIdOwnsCardId: userId " + userId + " cardId " + cardId);
        return this.userCardRepository.findByUserIdAndCardId(userId, cardId).isPresent();
    }

    @Override
    @Cacheable("amount")
    public BigDecimal getAmountFromCardId(final String cardId) {
        Optional<UserBankCard> userBankCardOptional = this.userCardRepository.findByCardId(cardId);
        logger.info("getIfUserIdOwnsCardId: cardId " + cardId + " found: " + userBankCardOptional.isPresent());
        return userBankCardOptional.map(UserBankCard::getAmount).orElse(null);
    }

    @Override
    @CacheEvict(value = "amount", key = "#cardId")
    public void evictCacheOnAmountWithCardId(final String cardId) {
        logger.info("Evicting Cache: amount on reserve/pay...");
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
