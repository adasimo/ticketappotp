package com.adamsimon.api.service;

import static com.adamsimon.commons.constants.Constants.*;

import com.adamsimon.api.interfaces.ApiService;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.core.interfaces.IncomingRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    private final IncomingRequestService incomingRequestService;
    private final Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);

    public ApiServiceImpl(final IncomingRequestService incomingRequestService) {
        this.incomingRequestService = incomingRequestService;
    }

    @Override
    public AbstractPartnerResponse getEvents() {
        logger.info("ApiService " + GET_EVENTS_NAME);
        return this.incomingRequestService.getEvents();
    }

    @Override
    public AbstractPartnerResponse getEvent(final Long eventId) {
        logger.info("ApiService " + GET_EVENT_NAME + eventId);
        return this.incomingRequestService.getEvent(eventId);
    }

    @Override
    public AbstractPartnerResponse pay(final Long eventId, final Long seatId, final String cardId, final String token) {
        logger.info("ApiService " + GET_PAY_NAME + eventId + " " + seatId + " " + cardId + "token ***");
        return this.incomingRequestService.pay(eventId, seatId, cardId, token);
    }

    @Override
    public void evictCacheOnSchedule() {
        this.incomingRequestService.evictCacheOnSchedule();
    }
}
