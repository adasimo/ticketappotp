package com.adamsimon.core.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

public interface IncomingRequestService {
    AbstractPartnerResponse getEvents();
    AbstractPartnerResponse getEvent(final Long eventId);
    AbstractPartnerResponse pay(final Long eventId, final Long seatId, final String cardId, final String token);
}
