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

    @Override
    public EventDetails getEventDetails(final Long eventId) {
        final AbstractPartnerResponse allResponse = getEvents();
        logger.info("allResponse getEventDetails: ", allResponse);
        if (allResponse.getSuccess()) {
            for (EventDetails eventDetails : ((EventsResponse) allResponse).getData()) {
                if (eventDetails.getEventId().equals(eventId)) {
                    logger.info("gotevent");
                    return eventDetails;
                }
            }
        }
        logger.info("notgotevent");
        return null;
    }

    @Override
    public AbstractPartnerResponse pay(final Long eventId, final Long seatId, final BigDecimal amount) {
        final EventDetails eventDetails = getEventDetails(eventId);
        logger.info("pay eventdetails: ", eventDetails);
        if (eventDetails == null) {
            logger.info("eventdetails null");
            return returnNoSuchEventError();
        }
        if (checkStartTimeStamp(eventDetails.getStartTimeStamp())) {
            logger.info("event has started");
            return returnEventHasStartedError();
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
                return returnAmountIsNotEnoughError();
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
                return returnNoSuchEventError();
            case ERROR_NO_SUCH_SEAT:
                return returnNoSuchSeatError();
            case ERROR_SEAT_IS_RESERVED:
                return returnSeatIsReservedError();
            default:
                return this.partnerCallerService.returnNotFoundError();
         }
    }

    private AbstractPartnerResponse returnEventHasStartedError() {
        return new ReservationBuilder.ReservationResponseBuilder()
                .getFailedBuilder()
                .withErrorCodeToFail(ERROR_EVENT_HAS_STARTED_TICKET)
                .withErrorMessageToFail(ERROR_EVENT_HAS_STARTED_TICKET_STR)
                .build();
    }

    private AbstractPartnerResponse returnNoSuchEventError() {
        return new ReservationBuilder.ReservationResponseBuilder()
                .getFailedBuilder()
                .withErrorCodeToFail(ERROR_NO_SUCH_EVENT_TICKET)
                .withErrorMessageToFail(ERROR_NO_SUCH_EVENT_TICKET_STR)
                .build();
    }

    private AbstractPartnerResponse returnNoSuchSeatError() {
        return new ReservationBuilder.ReservationResponseBuilder()
                .getFailedBuilder()
                .withErrorCodeToFail(ERROR_NO_SUCH_SEAT_TICKET)
                .withErrorMessageToFail(ERROR_NO_SUCH_SEAT_TICKET_STR)
                .build();
    }

    private AbstractPartnerResponse returnSeatIsReservedError() {
        return new ReservationBuilder.ReservationResponseBuilder()
                .getFailedBuilder()
                .withErrorCodeToFail(ERROR_SEAT_IS_RESERVED_TICKET)
                .withErrorMessageToFail(ERROR_SEAT_IS_RESERVED_TICKET_STR)
                .build();
    }

    private AbstractPartnerResponse returnAmountIsNotEnoughError() {
        return new ReservationBuilder.ReservationResponseBuilder()
                .getFailedBuilder()
                .withErrorCodeToFail(ERROR_AMOUNT_NOT_ENOUGH_CODE)
                .withErrorMessageToFail(ERROR_AMOUNT_NOT_ENOUGH_STR)
                .build();
    }

}
