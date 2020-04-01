package com.adamsimon.core.service;

import static com.adamsimon.commons.constants.Constants.*;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ErrorBuilder;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.commons.dto.responseDto.ErrorResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.exceptions.CustomNotFoundException;
import com.adamsimon.core.interfaces.DatabaseHandlerService;
import com.adamsimon.core.interfaces.HelperService;
import com.adamsimon.core.interfaces.OutgoingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class HelperServiceImpl implements HelperService {

     @Autowired
     final OutgoingService outgoingService;
     @Autowired
     final DatabaseHandlerService databaseHandlerService;

     public HelperServiceImpl(final OutgoingService outgoingService, final DatabaseHandlerService databaseHandlerService) {
         this.outgoingService = outgoingService;
         this.databaseHandlerService = databaseHandlerService;
     }

    @Override
    public AbstractPartnerResponse getEvents() {
        return this.outgoingService.getEvents();
    }

    @Override
    public AbstractPartnerResponse getEvent(final Long eventId) {
        return this.outgoingService.getEvent(eventId);
    }

    @Override
    public AbstractPartnerResponse pay(final Long eventId, final Long seatId, final String cardId, final String token)
        throws CustomNotFoundException {
        // a security miatt biztos, hogy van ilyen user, így nem ellenőrzöm
        final Long userId = this.databaseHandlerService.getUserFromAuthToken(token).getUserId();

        if (this.databaseHandlerService.getIfUserIdOwnsCardId(userId, cardId)) {
            BigDecimal amount = this.databaseHandlerService.getAmountFromCardId(cardId);
            amount = amount == null ? new BigDecimal(0) : amount;

            AbstractPartnerResponse response = this.outgoingService.pay(eventId, seatId, amount);
            if (response.getSuccess()) {
                return response;
            } else {
                throw new CustomNotFoundException(response.toString());
            }
        } else {
            final AbstractPartnerResponse errorResponse = new ReservationBuilder.ReservationResponseBuilder()
                    .getFailedBuilder()
                    .withErrorMessageToFail(INVALID_USER_TO_CARD_STR)
                    .withErrorCodeToFail(INVALID_USER_TO_CARD_CODE)
                    .build();
            throw new CustomNotFoundException(errorResponse.toString());
        }
    }

//    private String mapPartnerErrorsToCoreErrors(AbstractPartnerResponse partnerResponse) {
//         switch (((ReservationFailedResponse) partnerResponse).getErrorCode()) {
//             case ERROR_NO_SUCH_EVENT:
//                 return new ErrorBuilder.ErrorResponseBuilder()
//                         .withCode(ERROR_NO_SUCH_EVENT)
//                         .withText(ERROR_NO_SUCH_EVENT_STR)
//                         .build().toString();
//             case ERROR_NO_SUCH_SEAT:
//                 return new ErrorBuilder.ErrorResponseBuilder()
//                         .withCode(ERROR_NO_SUCH_SEAT)
//                         .withText(ERROR_NO_SUCH_SEAT_STR)
//                         .build().toString();
//             case ERROR_SEAT_IS_RESERVED:
//                 return new ErrorBuilder.ErrorResponseBuilder()
//                         .withCode(ERROR_SEAT_IS_RESERVED)
//                         .withText(ERROR_SEAT_IS_RESERVED_STR)
//                         .build().toString();
//             default:
//                 return new ErrorBuilder.ErrorResponseBuilder()
//                         .withCode(ERROR_DEFAULT)
//                         .withText(ERROR_DEFAULT_STR)
//                         .build().toString();
//         }
//    }
}
