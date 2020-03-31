package com.adamsimon.api.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

public interface ApiService {
    AbstractPartnerResponse getEvents();
    AbstractPartnerResponse getEvent(Long eventId);
    AbstractPartnerResponse pay(Long eventId, Long seatId, Long cardId);
}
