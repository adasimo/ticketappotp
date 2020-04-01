package com.adamsimon.core.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.exceptions.CustomNotFoundException;

public interface IncomingRequestService {
    AbstractPartnerResponse getEvents();
    AbstractPartnerResponse getEvent(final Long eventId);
    AbstractPartnerResponse pay(final Long eventId, final Long seatId, final String cardId, final String token)
            throws CustomNotFoundException;
}
