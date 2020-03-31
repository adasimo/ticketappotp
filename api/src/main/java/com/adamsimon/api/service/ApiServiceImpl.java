package com.adamsimon.api.service;

import com.adamsimon.api.interfaces.ApiService;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.EventData;
import com.adamsimon.commons.dto.EventDataResponse;
import com.adamsimon.commons.dto.EventsResponse;
import org.springframework.stereotype.Service;

@Service
public class ApiServiceImpl implements ApiService {
    @Override
    public AbstractPartnerResponse getEvents() {
        EventsResponse eventsResponse = new EventsResponse();
        return eventsResponse;
    }

    @Override
    public AbstractPartnerResponse getEvent(Long eventId) {
        EventDataResponse eventDataResponse = new EventDataResponse();
        EventData eventData = new EventData();
        eventData.setEventId(new Long(2222));
        eventDataResponse.setData(eventData);
        return eventDataResponse;
    }

    @Override
    public AbstractPartnerResponse pay(Long eventId, Long seatId, Long cardId) {
        return null;
    }
}
