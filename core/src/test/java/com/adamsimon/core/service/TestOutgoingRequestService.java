package com.adamsimon.core.service;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.helperDto.EventData;
import com.adamsimon.commons.dto.helperDto.EventDetails;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
import com.adamsimon.ticket.service.TicketBuyingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestOutgoingRequestService {

    @InjectMocks
    private OutgoingServiceImpl outgoingService;
    @Mock
    private TicketBuyingServiceImpl ticketBuyingService;

    @Before
    public void setup() {
        when(ticketBuyingService.getEvents()).thenReturn(new EventsResponse(new ArrayList<EventDetails>(), true));
        when(ticketBuyingService.getEvent(any())).thenReturn(new EventDataResponse(new EventData(), true));
        when(ticketBuyingService.pay(any(), any(), any())).thenReturn(new ReservationSuccessResponse(true,1234567L));
    }

    @Test
    public void propertiesShouldNotBeNull() {
        assertNotNull(ticketBuyingService);
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
    public void payShouldSuccess() {
        AbstractPartnerResponse partnerResponse = this.outgoingService.pay(1L, 1L, new BigDecimal(12345));
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((ReservationSuccessResponse)partnerResponse).getReservationId());
    }

    @Test
    public void payShouldGiveError() {
        when(outgoingService.pay(any(), any(), any())).thenReturn(new ReservationFailedResponse(false, 123, "MSG"));
        AbstractPartnerResponse partnerResponse = this.outgoingService.pay(1L, 1L, new BigDecimal(12345));
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorCode());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }
}
