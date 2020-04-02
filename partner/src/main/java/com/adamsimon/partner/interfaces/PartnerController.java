package com.adamsimon.partner.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;


public interface PartnerController {

    ResponseEntity<AbstractPartnerResponse> getEvents() throws IOException, ParseException;
    ResponseEntity<AbstractPartnerResponse> getEvent(final Long eventId) throws IOException, ParseException;
    ResponseEntity<AbstractPartnerResponse> makeReserve(final Long eventId, final Long seatId) throws IOException, ParseException;

}
