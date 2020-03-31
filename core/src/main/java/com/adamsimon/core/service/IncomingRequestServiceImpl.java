package com.adamsimon.core.service;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.core.interfaces.IncomingRequestService;

public class IncomingRequestServiceImpl implements IncomingRequestService {
    @Override
    public AbstractPartnerResponse getEvents() {
        return null;
    }

    @Override
    public AbstractPartnerResponse getEvent(Long eventId) {
        return null;
    }

    @Override
    public AbstractPartnerResponse pay(Long eventId, Long seatId, Long cardId) {
        return null;
    }
}
