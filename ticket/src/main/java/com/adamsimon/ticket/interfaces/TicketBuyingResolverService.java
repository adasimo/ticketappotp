package com.adamsimon.ticket.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

import java.math.BigDecimal;

public interface TicketBuyingResolverService {
    AbstractPartnerResponse getEvents();
    AbstractPartnerResponse getEvent(final Long eventId);
    AbstractPartnerResponse pay(final Long eventId, final Long seatId, final BigDecimal amount);
}
