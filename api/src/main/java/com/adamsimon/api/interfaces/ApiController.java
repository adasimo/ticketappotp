package com.adamsimon.api.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.exceptions.CustomNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface ApiController {
    ResponseEntity<EntityModel<AbstractPartnerResponse>> getEvents();
    ResponseEntity<EntityModel<AbstractPartnerResponse>> getEvent (final Long eventId);
    ResponseEntity<EntityModel<AbstractPartnerResponse>> pay(final Long eventId,
                                                             final Long seatId,
                                                             final String cardId,
                                                             final String token) throws CustomNotFoundException;
}
