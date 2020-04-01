package com.adamsimon.core.service;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.core.interfaces.OutgoingService;
import com.adamsimon.ticket.interfaces.TicketBuyingResolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OutgoingServiceImpl implements OutgoingService {

    @Autowired
    TicketBuyingResolverService ticketBuyingResolverService;

    public OutgoingServiceImpl(TicketBuyingResolverService ticketBuyingResolverService) {
        this.ticketBuyingResolverService = ticketBuyingResolverService;
    }

    @Override
    public AbstractPartnerResponse getEvents() {
        return this.ticketBuyingResolverService.getEvents();
    }

    @Override
    public AbstractPartnerResponse getEvent(final Long eventId) {
        return this.ticketBuyingResolverService.getEvent(eventId);
    }

    @Override
    public AbstractPartnerResponse pay(final Long eventId, final Long seatId, final BigDecimal amount) {
        return this.ticketBuyingResolverService.pay(eventId, seatId, amount);
    }
}
