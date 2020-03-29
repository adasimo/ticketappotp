package com.adamsimon.api.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.EventDataResponse;
import com.adamsimon.commons.dto.EventsResponse;

public interface ApiService {
    EventsResponse getEvents();
    EventDataResponse getEvent(Long eventId);
    AbstractPartnerResponse pay(Long eventId, Long seatId, Long cardId);
}
