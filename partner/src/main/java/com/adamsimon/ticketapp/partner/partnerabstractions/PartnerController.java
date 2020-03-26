package com.adamsimon.ticketapp.partner.partnerabstractions;

import com.adamsimon.ticketapp.partner.dto.EventDataResponse;
import com.adamsimon.ticketapp.partner.dto.EventsResponse;
import com.adamsimon.ticketapp.partner.partnerabstractions.abstracts.AbstractPartnerResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;


public interface PartnerController {

    ResponseEntity<JSONObject> getEvents() throws IOException, ParseException;
    ResponseEntity<JSONObject> getEvent(Long eventId) throws IOException, ParseException;
    ResponseEntity<AbstractPartnerResponse> makeReserve(Long eventId, Long seatId) throws IOException, ParseException;

}
