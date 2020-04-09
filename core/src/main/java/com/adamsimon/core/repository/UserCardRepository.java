package com.adamsimon.core.repository;

import com.adamsimon.core.domain.UserBankCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCardRepository extends JpaRepository <UserBankCard, Long> {
    public Optional<UserBankCard> findByUserIdAndCardId(final Long userId, final String cardId);

    public Optional<UserBankCard> findByCardId(final String cardId);
}
