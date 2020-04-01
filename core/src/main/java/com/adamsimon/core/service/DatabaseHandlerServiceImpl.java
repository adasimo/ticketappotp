package com.adamsimon.core.service;

import com.adamsimon.core.domain.User;
import com.adamsimon.core.domain.UserBankCard;
import com.adamsimon.core.interfaces.DatabaseHandlerService;
import com.adamsimon.core.repository.UserCardRepository;
import com.adamsimon.core.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class DatabaseHandlerServiceImpl implements DatabaseHandlerService {

    @Autowired
    final UsersRepository usersRepository;
    @Autowired
    final UserCardRepository userCardRepository;

    public DatabaseHandlerServiceImpl(final UsersRepository usersRepository, final UserCardRepository userCardRepository) {
        this.usersRepository = usersRepository;
        this.userCardRepository = userCardRepository;
    }

    @Override
    public User getUserFromAuthToken(final String token) {
        final Optional<User> user = this.usersRepository.findByToken(token);
        return user.orElse(null);
    }

    @Override
    public Boolean getIfUserIdOwnsCardId(final Long userId, final String cardId) {
        return this.userCardRepository.findByUserIdAndCardId(userId, cardId).isPresent();
    }

    @Override
    public BigDecimal getAmountFromCardId(final String cardId) {
        Optional<UserBankCard> userBankCardOptional = this.userCardRepository.findByCardId(cardId);

        return userBankCardOptional.map(UserBankCard::getAmount).orElse(null);
    }


}
