package com.adamsimon.core.service;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.core.interfaces.OutgoingService;

public class OutgoingServiceImpl implements OutgoingService {
    @Override
    public AbstractPartnerResponse getEvents() {
        return null;
    }

    @Override
    public AbstractPartnerResponse getEvent(Long eventId) {
        return null;
    }

    @Override
    public AbstractPartnerResponse pay(Long eventId, Long seatId) {
        return null;
    }
}
