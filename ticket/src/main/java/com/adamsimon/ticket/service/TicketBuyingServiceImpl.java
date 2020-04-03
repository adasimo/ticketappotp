package com.adamsimon.ticket.service;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.commons.dto.helperDto.EventDetails;
import com.adamsimon.commons.dto.helperDto.Seat;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
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
    final PartnerCallerService partnerCallerService;

    public TicketBuyingServiceImpl(PartnerCallerService partnerCallerService) {
        this.partnerCallerService = partnerCallerService;
    }
    @Override
    public AbstractPartnerResponse getEvents() {
        return this.partnerCallerService.getEvents();
    }

    @Override
    public AbstractPartnerResponse getEvent(final Long eventId) {

        final AbstractPartnerResponse partnerResponse = this.partnerCallerService.getEvent(eventId);
        logger.info("partnerResponse getevent: ", partnerResponse);
        if (!partnerResponse.getSuccess()) {
            logger.info("got into mappinggetevent");
            return mapPartnerErrorsToCoreErrors(partnerResponse);
        }
        return partnerResponse;
    }

    public EventDetails getEventDetails(final AbstractPartnerResponse allResponse, final Long eventId) {
        for (EventDetails eventDetails : ((EventsResponse) allResponse).getData()) {
            if (eventDetails.getEventId().equals(eventId)) {
                logger.info("gotevent");
                return eventDetails;
            }
        }
        return null;
    }

    @Override
    public AbstractPartnerResponse pay(final Long eventId, final Long seatId, final BigDecimal amount) {
        final AbstractPartnerResponse allResponse = getEvents();
        if (!allResponse.getSuccess()) {
            return allResponse;
        }

        final EventDetails eventDetails = getEventDetails(allResponse, eventId);
        logger.info("pay eventdetails: ", eventDetails);
//        if (eventDetails == null) {
//            logger.info("eventdetails null");
//            return this.partnerCallerService.returnError(ERROR_NO_SUCH_EVENT_TICKET, ERROR_NO_SUCH_EVENT_TICKET_STR);
//        }
        if (checkStartTimeStamp(eventDetails.getStartTimeStamp())) {
            logger.info("event has started");
            return this.partnerCallerService.returnError(ERROR_EVENT_HAS_STARTED_TICKET, ERROR_EVENT_HAS_STARTED_TICKET_STR);
        }

        final AbstractPartnerResponse partnerResponse = getEvent(eventId);

        if(!partnerResponse.getSuccess()) {
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
            return finalResponse;
        } else {
            return mapPartnerErrorsToCoreErrors(finalResponse);
        }
    }

    private boolean checkStartTimeStamp(String startTimeStamp) {
        String currentTimeStampString = new Timestamp(System.currentTimeMillis()).getTime() + "";
        logger.info("current time: " + currentTimeStampString);
        logger.info("event time: " + currentTimeStampString);
        return new BigInteger(startTimeStamp).compareTo(new BigInteger(currentTimeStampString.substring(9))) < 0;
    }

    private AbstractPartnerResponse mapPartnerErrorsToCoreErrors(AbstractPartnerResponse partnerResponse) {
        switch (((ReservationFailedResponse) partnerResponse).getErrorCode()) {
            case ERROR_NO_SUCH_EVENT:
                return this.partnerCallerService.returnError(ERROR_NO_SUCH_EVENT_TICKET, ((ReservationFailedResponse) partnerResponse).getErrorMessage());
            case ERROR_NO_SUCH_SEAT:
                return this.partnerCallerService.returnError(ERROR_NO_SUCH_SEAT_TICKET, ((ReservationFailedResponse) partnerResponse).getErrorMessage());
            case ERROR_SEAT_IS_RESERVED:
                return this.partnerCallerService.returnError(ERROR_SEAT_IS_RESERVED_TICKET, ((ReservationFailedResponse) partnerResponse).getErrorMessage());
            case NO_PARTNER_TOKEN_CODE:
                return this.partnerCallerService.returnError(NO_PARTNER_TOKEN_CODE, ((ReservationFailedResponse) partnerResponse).getErrorMessage());
            case INVALID_PARTNERTOKEN_CODE:
                return this.partnerCallerService.returnError(INVALID_PARTNERTOKEN_CODE,((ReservationFailedResponse) partnerResponse).getErrorMessage());
            default:
                return this.partnerCallerService.returnError(ERROR_PARTNER_NOT_FOUND_CODE, ERROR_PARTNER_NOT_FOUND_STR);
         }
    }
}
