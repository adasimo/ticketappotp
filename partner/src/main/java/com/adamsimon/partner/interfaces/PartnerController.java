package com.adamsimon.partner.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;


public interface PartnerController {

    ResponseEntity<JSONObject> getEvents() throws IOException, ParseException;
    ResponseEntity<JSONObject> getEvent(final Long eventId) throws IOException, ParseException;
    ResponseEntity<AbstractPartnerResponse> makeReserve(final Long eventId, final Long seatId) throws IOException, ParseException;

}
