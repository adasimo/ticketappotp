package com.adamsimon.ticket.service;

import com.adamsimon.ticket.domain.UserToPartner;
import com.adamsimon.ticket.interfaces.TicketDatabaseCallerService;
import com.adamsimon.ticket.repository.UserRepositoryToPartner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketDatabaseCallerServiceImpl implements TicketDatabaseCallerService {

    @Autowired
    private final UserRepositoryToPartner userRepositoryToPartner;

    public TicketDatabaseCallerServiceImpl(UserRepositoryToPartner userRepositoryToPartner) {
        this.userRepositoryToPartner = userRepositoryToPartner;
    }

    @Override
    public String getToken() {
        Optional<UserToPartner> optionalUserToPartner = this.userRepositoryToPartner.findById(1L);
        return optionalUserToPartner.map(UserToPartner::getToken).orElse(null);
    }
}
