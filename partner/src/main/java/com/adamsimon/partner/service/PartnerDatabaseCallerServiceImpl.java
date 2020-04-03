package com.adamsimon.partner.service;

import com.adamsimon.partner.domain.TicketModuleUser;
import com.adamsimon.partner.interfaces.PartnerDatabaseCallerService;
import com.adamsimon.partner.repository.TicketModuleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PartnerDatabaseCallerServiceImpl implements PartnerDatabaseCallerService {

    @Autowired
    private final TicketModuleUserRepository ticketModuleUserRepository;

    public PartnerDatabaseCallerServiceImpl(TicketModuleUserRepository ticketModuleUserRepository) {
        this.ticketModuleUserRepository = ticketModuleUserRepository;
    }

    @Override
    public TicketModuleUser getUserByToken(String token) {
        Optional<TicketModuleUser> optionalTicketModuleUser = this.ticketModuleUserRepository.findByToken(token);

        return optionalTicketModuleUser.orElse(null);
    }
}
