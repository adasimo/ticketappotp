package com.adamsimon.partner.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.exceptions.NoSuchEventException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface PartnerService {
    JSONObject getEvents() throws IOException, ParseException;
    JSONObject getEvent(Long eventId) throws IOException, ParseException, NoSuchEventException;
    AbstractPartnerResponse makeReservation(Long eventId, Long seatId) throws IOException, ParseException;
//    JSONObject fetchDataFromFiles(FilesEnum filesEnum) throws IOException, ParseException;
}