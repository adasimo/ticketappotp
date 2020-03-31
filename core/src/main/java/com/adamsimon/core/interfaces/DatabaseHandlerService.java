package com.adamsimon.core.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.core.domain.Users;

public interface DatabaseHandlerService {
    AbstractPartnerResponse getEvents();
    AbstractPartnerResponse getEvent(Long eventId);
    AbstractPartnerResponse pay(Long eventId, Long seatId, Long cardId);
    Users getUserFromAuthToken(String token);
}
