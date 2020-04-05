package com.adamsimon.core.service;

import static com.adamsimon.commons.constants.Constants.*;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.core.interfaces.DatabaseHandlerService;
import com.adamsimon.core.interfaces.HelperService;
import com.adamsimon.core.interfaces.OutgoingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class HelperServiceImpl implements HelperService {

     @Autowired
     private final OutgoingService outgoingService;
     @Autowired
     private final DatabaseHandlerService databaseHandlerService;
     private final Logger logger = LoggerFactory.getLogger(HelperServiceImpl.class);

     public HelperServiceImpl(final OutgoingService outgoingService, final DatabaseHandlerService databaseHandlerService) {
         this.outgoingService = outgoingService;
         this.databaseHandlerService = databaseHandlerService;
     }

    @Override
    public AbstractPartnerResponse getEvents() {
        logger.info("HelperService: " + GET_EVENTS_NAME);
        return this.outgoingService.getEvents();
    }

    @Override
    public AbstractPartnerResponse getEvent(final Long eventId) {
        logger.info("HelperService: " + GET_EVENT_NAME + eventId);
         return this.outgoingService.getEvent(eventId);
    }

    @Override
    public AbstractPartnerResponse pay(final Long eventId, final Long seatId, final String cardId, final String token) {
        // a security miatt biztos, hogy van ilyen user, így nem ellenőrzöm
        final Long userId = this.databaseHandlerService.getUserFromAuthToken(token).getUserId();
        logger.info("UserId for payment got: " + userId);
        if (this.databaseHandlerService.getIfUserIdOwnsCardId(userId, cardId)) {
            logger.info("User owns card");
            BigDecimal amount = this.databaseHandlerService.getAmountFromCardId(cardId);
            amount = amount == null ? new BigDecimal(0) : amount;
            logger.info("Amount for payment got: " + amount);
            final AbstractPartnerResponse response = this.outgoingService.pay(eventId, seatId, amount);
            if (response.getSuccess()) {
                //itt levonni az egyenleget egy saveAndFlush-sal
                logger.info("Payment success");
                this.databaseHandlerService.evictCacheOnAmountWithCardId(cardId);
            } else {
                logger.info("Payment failed");
            }
            return response;
        } else {
            logger.info("Error: " + INVALID_USER_TO_CARD_CODE + " " + INVALID_USER_TO_CARD_STR);
            return new ReservationBuilder.ReservationResponseBuilder()
                    .getFailedBuilder()
                    .withErrorMessageToFail(INVALID_USER_TO_CARD_STR)
                    .withErrorCodeToFail(INVALID_USER_TO_CARD_CODE)
                    .build();
        }
    }

    @Override
    public void evictCacheOnSchedule() {
        this.databaseHandlerService.evictCacheOnSchedule();
    }
}
