package com.adamsimon.ticket.service;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
import com.adamsimon.ticket.interfaces.PartnerCallerService;
import com.adamsimon.ticket.interfaces.TicketBuyingResolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.adamsimon.commons.constants.Constants.*;

@Service
public class TicketBuyingServiceImpl implements TicketBuyingResolverService {

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
        return this.partnerCallerService.getEvent(eventId);
    }

    @Override
    public AbstractPartnerResponse pay(final Long eventId, final Long seatId, final BigDecimal amount) {
        return null;
    }

        private AbstractPartnerResponse mapPartnerErrorsToCoreErrors(AbstractPartnerResponse partnerResponse) {
         switch (((ReservationFailedResponse) partnerResponse).getErrorCode()) {
             case ERROR_NO_SUCH_EVENT:
                 return new ReservationBuilder.ReservationResponseBuilder()
                         .getFailedBuilder()
                         .withErrorCodeToFail(ERROR_NO_SUCH_EVENT_TICKET)
                         .withErrorMessageToFail(ERROR_NO_SUCH_EVENT_TICKET_STR)
                         .build();
             case ERROR_NO_SUCH_SEAT:
                 return new ReservationBuilder.ReservationResponseBuilder()
                         .getFailedBuilder()
                         .withErrorCodeToFail(ERROR_NO_SUCH_SEAT_TICKET)
                         .withErrorMessageToFail(ERROR_NO_SUCH_SEAT_TICKET_STR)
                         .build();
                 case ERROR_SEAT_IS_RESERVED:
                     return new ReservationBuilder.ReservationResponseBuilder()
                             .getFailedBuilder()
                             .withErrorCodeToFail(ERROR_SEAT_IS_RESERVED_TICKET)
                             .withErrorMessageToFail(ERROR_SEAT_IS_RESERVED_TICKET_STR)
                             .build();
             default:
                 return new ReservationBuilder.ReservationResponseBuilder()
                         .getFailedBuilder()
                         .withErrorCodeToFail(ERROR_PARTNER_NOT_FOUND_CODE)
                         .withErrorMessageToFail(ERROR_PARTNER_NOT_FOUND_STR)
                         .build();
         }
    }
}
