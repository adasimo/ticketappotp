package com.adamsimon.ticket.service;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.helperDto.EventDetails;
import com.adamsimon.commons.dto.helperDto.Seat;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.ticket.interfaces.PartnerCallerService;
import com.adamsimon.ticket.interfaces.TicketBuyingResolverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;


import static com.adamsimon.commons.constants.Constants.*;

@Service
public class TicketBuyingServiceImpl implements TicketBuyingResolverService {

    private final Logger logger = LoggerFactory.getLogger(TicketBuyingServiceImpl.class);
    @Autowired
    private final PartnerCallerService partnerCallerService;

    public TicketBuyingServiceImpl(PartnerCallerService partnerCallerService) {
        this.partnerCallerService = partnerCallerService;
    }
    @Override
    public AbstractPartnerResponse getEvents() {

        final AbstractPartnerResponse partnerResponse = this.partnerCallerService.getEvents();
        if (!partnerResponse.getSuccess()) {
            return mapPartnerErrorsToCoreErrors(partnerResponse);
        }

        logger.info("partnerResponse getevents success");
        return partnerResponse;
    }

    @Override
    public AbstractPartnerResponse getEvent(final Long eventId) {

        final AbstractPartnerResponse partnerResponse = this.partnerCallerService.getEvent(eventId);
        if (!partnerResponse.getSuccess()) {
            return mapPartnerErrorsToCoreErrors(partnerResponse);
        }
        logger.info("partnerResponse getevent success");
        return partnerResponse;
    }

    public EventDetails getEventDetails(final AbstractPartnerResponse allResponse, final Long eventId) {
        for (EventDetails eventDetails : ((EventsResponse) allResponse).getData()) {
            if (eventDetails.getEventId().equals(eventId)) {
                logger.info("gotevent: " + eventId);
                return eventDetails;
            }
        }
        return null;
    }

    @Override
    public AbstractPartnerResponse pay(final Long eventId, final Long seatId, final BigDecimal amount) {
        final AbstractPartnerResponse allResponse = getEvents();
        if (!allResponse.getSuccess()) {
            logger.info("Payment is failed");
            return allResponse;
        }

        final EventDetails eventDetails = getEventDetails(allResponse, eventId);
        logger.info("pay eventdetails: " + eventDetails);
        if (eventDetails == null) {
            return this.partnerCallerService.returnError(ERROR_NO_SUCH_EVENT_TICKET, ERROR_NO_SUCH_EVENT_TICKET_STR);
        }

        if (checkStartTimeStamp(eventDetails.getStartTimeStamp())) {
            return this.partnerCallerService.returnError(ERROR_EVENT_HAS_STARTED_TICKET, ERROR_EVENT_HAS_STARTED_TICKET_STR);
        }

        final AbstractPartnerResponse partnerResponse = getEvent(eventId);

        if(!partnerResponse.getSuccess()) {
            logger.info("Payment is failed");
            return partnerResponse;
        }

        final EventDataResponse eventDataResponse = (EventDataResponse) partnerResponse;

        for (Seat s : eventDataResponse.getData().getSeats()) {
            // itt lehetne egy átváltó logikát valuták között, ha nem HUF a valuta, átadnám paraméterként, és itt check
            // majd lehetne egy RestTemplates hívást egy external API-ra intézni, ami valutaátváltási értékeket ad vissza
            if (amount.compareTo(BigDecimal.valueOf(s.getPrice())) < 0) {
                return this.partnerCallerService.returnError(ERROR_AMOUNT_NOT_ENOUGH_CODE, ERROR_AMOUNT_NOT_ENOUGH_STR);
            }
        }

        final AbstractPartnerResponse finalResponse = this.partnerCallerService.pay(eventId, seatId);
        if (finalResponse.getSuccess()) {
            logger.info("Payment is succeed");
            return finalResponse;
        } else {
            return mapPartnerErrorsToCoreErrors(finalResponse);
        }
    }

    private boolean checkStartTimeStamp(final String startTimeStamp) {
        final String currentTimeStampString = new Timestamp(System.currentTimeMillis()).getTime() + "";
        logger.info("current time: " + currentTimeStampString.substring(0, 10));
        logger.info("event time: " + startTimeStamp);
        return new BigInteger(startTimeStamp).compareTo(new BigInteger(currentTimeStampString.substring(0, 10))) < 0;
    }

    private AbstractPartnerResponse mapPartnerErrorsToCoreErrors(AbstractPartnerResponse partnerResponse) {
        switch (((ReservationFailedResponse) partnerResponse).getErrorCode()) {
            case ERROR_NO_SUCH_EVENT:
                return this.partnerCallerService.returnError(ERROR_NO_SUCH_EVENT_TICKET, ERROR_NO_SUCH_EVENT_TICKET_STR);
            case ERROR_NO_SUCH_SEAT:
                return this.partnerCallerService.returnError(ERROR_NO_SUCH_SEAT_TICKET, ERROR_NO_SUCH_SEAT_TICKET_STR);
            case ERROR_SEAT_IS_RESERVED:
                return this.partnerCallerService.returnError(ERROR_SEAT_IS_RESERVED_TICKET, ERROR_SEAT_IS_RESERVED_TICKET_STR);
            case NO_PARTNER_TOKEN_CODE:
                return this.partnerCallerService.returnError(NO_PARTNER_TOKEN_CODE, NO_PARTNER_TOKEN_STR);
            case INVALID_PARTNERTOKEN_CODE:
                return this.partnerCallerService.returnError(INVALID_PARTNERTOKEN_CODE, INVALID_PARTNER_TOKEN_STR);
            case ERROR_NO_JSON:
                return this.partnerCallerService.returnError(ERROR_NO_JSON, ERROR_NO_JSON_STR);
            default:
                return this.partnerCallerService.returnError(ERROR_PARTNER_NOT_FOUND_CODE, ERROR_PARTNER_NOT_FOUND_STR);
         }
    }
}
