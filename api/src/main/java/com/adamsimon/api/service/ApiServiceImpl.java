package com.adamsimon.api.service;

import com.adamsimon.api.interfaces.ApiService;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.exceptions.CustomNotFoundException;
import com.adamsimon.core.interfaces.IncomingRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    final IncomingRequestService incomingRequestService;

    public ApiServiceImpl(final IncomingRequestService incomingRequestService) {
        this.incomingRequestService = incomingRequestService;
    }

    @Override
    public AbstractPartnerResponse getEvents() {
        return this.incomingRequestService.getEvents();
    }

    @Override
    public AbstractPartnerResponse getEvent(final Long eventId) {
        return this.incomingRequestService.getEvent(eventId);
    }

    @Override
    public AbstractPartnerResponse pay(final Long eventId, final Long seatId, final String cardId, final String token)
            throws CustomNotFoundException {
        return this.incomingRequestService.pay(eventId, seatId, cardId, token);
    }
}
