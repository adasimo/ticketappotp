package com.adamsimon.core.repository;

import com.adamsimon.core.domain.UserBankCard;
import com.adamsimon.core.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCardRepository extends JpaRepository <UserBankCard, Long> {
    public List<UserBankCard> findAllByUserId(Long userId);

    public Optional<UserBankCard> findById(Long id);

    public Optional<UserBankCard> findByCardId(String cardId);

    public Optional<UserBankCard> findByCardnumber(String cardnumber);

    public void deleteAllByUserId(Long userId);

    public void deleteByCardId(String cardId);

    public void deleteById(Long id);
}
