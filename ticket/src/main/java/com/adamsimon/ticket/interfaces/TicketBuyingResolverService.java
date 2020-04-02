package com.adamsimon.ticket.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.helperDto.EventDetails;

import java.math.BigDecimal;

public interface TicketBuyingResolverService {
    AbstractPartnerResponse getEvents();
    AbstractPartnerResponse getEvent(final Long eventId);
    EventDetails getEventDetails(final Long eventId);
    AbstractPartnerResponse pay(final Long eventId, final Long seatId, final BigDecimal amount);
}
