package com.adamsimon.ticket.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

import java.math.BigDecimal;

public interface PartnerCallerService {
    AbstractPartnerResponse getEvents();
    AbstractPartnerResponse getEvent(final Long eventId);
    AbstractPartnerResponse pay(final Long eventId, final Long seatId);
    AbstractPartnerResponse returnError(final int errorCode, final String errorMessage);
}
