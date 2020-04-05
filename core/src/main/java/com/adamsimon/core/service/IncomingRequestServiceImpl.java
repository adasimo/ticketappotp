package com.adamsimon.core.service;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.core.interfaces.HelperService;
import com.adamsimon.core.interfaces.IncomingRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncomingRequestServiceImpl implements IncomingRequestService {

    @Autowired
    private final HelperService helperService;

    public IncomingRequestServiceImpl(final HelperService helperService) {
        this.helperService = helperService;
    }

    @Override
    public AbstractPartnerResponse getEvents() {
        return this.helperService.getEvents();
    }

    @Override
    public AbstractPartnerResponse getEvent(final Long eventId) {
        return this.helperService.getEvent(eventId);
    }

    @Override
    public AbstractPartnerResponse pay(final Long eventId, final Long seatId, final String cardId, final String token) {
        return this.helperService.pay(eventId, seatId, cardId, token);
    }

    @Override
    public void evictCacheOnSchedule() {
        this.helperService.evictCacheOnSchedule();
    }
}
