package com.adamsimon.core.service;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.helperDto.EventData;
import com.adamsimon.commons.dto.helperDto.EventDetails;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
import com.adamsimon.core.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.adamsimon.commons.constants.Constants.INVALID_USER_TO_CARD_CODE;
import static com.adamsimon.commons.constants.Constants.INVALID_USER_TO_CARD_STR;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestHelperService {

    @InjectMocks
    private HelperServiceImpl helperService;
    @Mock
    private DatabaseHandlerServiceImpl databaseHandlerService;
    @Mock
    private OutgoingServiceImpl outgoingService;

    private static final long EVENT_ID = 1;
    private static final long SEAT_ID = 1;
    private static final long WRONG_USER_CODE = 456;
    private static final long GOOD_USER_CODE = 123;
    private static final BigDecimal AMOUNT = new BigDecimal(1000);
    private static final String CARD_ID = "C0001";

    @Before
    public void setup() {
        when(databaseHandlerService.getAmountFromCardId(any())).thenReturn(AMOUNT);
        when(databaseHandlerService.getIfUserIdOwnsCardId(GOOD_USER_CODE, CARD_ID)).thenReturn(true);
        when(databaseHandlerService.getIfUserIdOwnsCardId(WRONG_USER_CODE, CARD_ID)).thenReturn(false);

        when(outgoingService.getEvents()).thenReturn(new EventsResponse(new ArrayList<EventDetails>(), true));
        when(outgoingService.getEvent(any())).thenReturn(new EventDataResponse(new EventData(), true));
    }

    @Test
    public void beansShouldNotBeNull() {
        assertNotNull(helperService);
        assertNotNull(databaseHandlerService);
        assertNotNull(outgoingService);
    }

    @Test
    public void getEventsShouldSuccess() {
        AbstractPartnerResponse partnerResponse = this.outgoingService.getEvents();
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((EventsResponse)partnerResponse).getData());
    }

    @Test
    public void getEventsShouldGiveError() {
        when(outgoingService.getEvents()).thenReturn(new ReservationFailedResponse(false, 123, "MSG"));
        AbstractPartnerResponse partnerResponse = this.outgoingService.getEvents();
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorCode());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }

    @Test
    public void getEventShouldSuccess() {
        AbstractPartnerResponse partnerResponse = this.outgoingService.getEvent(1L);
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((EventDataResponse)partnerResponse).getData());
    }

    @Test
    public void getEventShouldGiveError() {
        when(outgoingService.getEvent(any())).thenReturn(new ReservationFailedResponse(false, 123, "MSG"));
        AbstractPartnerResponse partnerResponse = this.outgoingService.getEvent(1L);
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorCode());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }

    @Test
    public void payShouldGiveCardIsNotUsersError() {
        final User user = new User();
        user.setUserId(WRONG_USER_CODE);
        when(databaseHandlerService.getUserFromAuthToken("tokenTrue")).thenReturn(user);
        final AbstractPartnerResponse partnerResponse = helperService.pay(EVENT_ID, SEAT_ID, CARD_ID, "tokenTrue");
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertEquals(INVALID_USER_TO_CARD_CODE, ((ReservationFailedResponse)partnerResponse).getErrorCode().intValue());
        assertEquals(INVALID_USER_TO_CARD_STR, ((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }

    @Test
    public void payShouldGiveErrorFromOutGoingService() {
        //given
        final User user = new User();
        user.setUserId(GOOD_USER_CODE);
        when(databaseHandlerService.getUserFromAuthToken("tokenTrue")).thenReturn(user);
        when(outgoingService.pay(EVENT_ID, SEAT_ID, AMOUNT)).thenReturn(new ReservationFailedResponse(false, 123, "MSG"));
        final AbstractPartnerResponse partnerResponse = helperService.pay(EVENT_ID, SEAT_ID, CARD_ID, "tokenTrue");
        //then
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertEquals(123, ((ReservationFailedResponse)partnerResponse).getErrorCode().intValue());
        assertEquals("MSG", ((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }

    @Test
    public void payShouldGiveSuccess() {
        //given
        final User user = new User();
        user.setUserId(GOOD_USER_CODE);
        when(databaseHandlerService.getUserFromAuthToken("tokenTrue")).thenReturn(user);
        when(outgoingService.pay(EVENT_ID, SEAT_ID, AMOUNT)).thenReturn(new ReservationSuccessResponse(true,1234567L));
        final AbstractPartnerResponse partnerResponse = helperService.pay(EVENT_ID, SEAT_ID, CARD_ID, "tokenTrue");
        //then
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertEquals(1234567, ((ReservationSuccessResponse)partnerResponse).getReservationId().intValue());
    }
}
