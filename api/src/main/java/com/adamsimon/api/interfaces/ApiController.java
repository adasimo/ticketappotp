package com.adamsimon.api.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.EventDataResponse;
import com.adamsimon.commons.dto.EventsResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface ApiController {
    ResponseEntity<EntityModel<AbstractPartnerResponse>> getEvents();
    ResponseEntity<EntityModel<AbstractPartnerResponse>> getEvent (Long eventId);
    ResponseEntity<EntityModel<AbstractPartnerResponse>> pay(Long eventId, Long seatId, Long cardId);
}
