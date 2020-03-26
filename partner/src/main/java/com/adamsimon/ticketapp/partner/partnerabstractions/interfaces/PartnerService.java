package com.adamsimon.ticketapp.partner.partnerabstractions.interfaces;

import com.adamsimon.ticketapp.partner.dto.EventDataResponse;
import com.adamsimon.ticketapp.partner.dto.EventsResponse;
import com.adamsimon.ticketapp.partner.enums.FilesEnum;
import com.adamsimon.ticketapp.partner.partnerabstractions.abstracts.AbstractPartnerResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface PartnerService {
    JSONObject getEvents() throws IOException, ParseException;
    JSONObject getEvent(Long eventId) throws IOException, ParseException;
    AbstractPartnerResponse makeReservation(Long eventId, Long seatId) throws IOException, ParseException;
//    JSONObject fetchDataFromFiles(FilesEnum filesEnum) throws IOException, ParseException;
}
