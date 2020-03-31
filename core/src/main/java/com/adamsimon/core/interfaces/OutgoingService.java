package com.adamsimon.core.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

public interface OutgoingService {
    AbstractPartnerResponse getEvents();
    AbstractPartnerResponse getEvent(Long eventId);
    AbstractPartnerResponse pay(Long eventId, Long seatId);
}
