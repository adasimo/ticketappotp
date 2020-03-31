package com.adamsimon.core.service;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.core.domain.Users;
import com.adamsimon.core.interfaces.DatabaseHandlerService;
import com.adamsimon.core.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class DatabaseHandlerServiceImpl implements DatabaseHandlerService {

    @Autowired
    UsersRepository usersRepository;

    @Override
    public AbstractPartnerResponse getEvents() {
        return null;
    }

    @Override
    public AbstractPartnerResponse getEvent(Long eventId) {
        return null;
    }

    @Override
    public AbstractPartnerResponse pay(Long eventId, Long seatId, Long cardId) {
        return null;
    }

    @Override
    public Users getUserFromAuthToken(String token) {
        Optional<Users> user = this.usersRepository.findByToken(token);
        return user.orElse(null);
    }
}
