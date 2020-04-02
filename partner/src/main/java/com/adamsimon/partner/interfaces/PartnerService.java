package com.adamsimon.partner.interfaces;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.exceptions.NoSuchEventException;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface PartnerService {
    AbstractPartnerResponse getEvents() throws IOException, ParseException;
    AbstractPartnerResponse getEvent(final Long eventId) throws IOException, ParseException, NoSuchEventException;
    AbstractPartnerResponse makeReservation(final Long eventId, final Long seatId) throws IOException, ParseException;
//    JSONObject fetchDataFromFiles(FilesEnum filesEnum) throws IOException, ParseException;
}
