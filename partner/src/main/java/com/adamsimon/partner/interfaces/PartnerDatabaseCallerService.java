package com.adamsimon.partner.interfaces;

import com.adamsimon.partner.domain.TicketModuleUser;

public interface PartnerDatabaseCallerService {
    TicketModuleUser getUserByToken(String token);
}
