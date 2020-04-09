package com.adamsimon.core.service;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.helperDto.EventData;
import com.adamsimon.commons.dto.helperDto.EventDetails;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestIncomingRequestService {

    @InjectMocks
    private IncomingRequestServiceImpl incomingRequestService;
    @Mock
    private HelperServiceImpl helperService;

    @Before
    public void setup() {
        when(helperService.getEvents()).thenReturn(new EventsResponse(new ArrayList<EventDetails>(), true));
        when(helperService.getEvent(any())).thenReturn(new EventDataResponse(new EventData(), true));
        when(helperService.pay(any(), any(), any(), any())).thenReturn(new ReservationSuccessResponse(true,1234567L));
    }

    @Test
    public void propertiesShouldNotBeNull() {
        assertNotNull(helperService);
        assertNotNull(incomingRequestService);
    }

    @Test
    public void getEventsShouldSuccess() {
        AbstractPartnerResponse partnerResponse = this.incomingRequestService.getEvents();
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((EventsResponse)partnerResponse).getData());
    }

    @Test
    public void getEventsShouldGiveError() {
        when(incomingRequestService.getEvents()).thenReturn(new ReservationFailedResponse(false, 123, "MSG"));
        AbstractPartnerResponse partnerResponse = this.incomingRequestService.getEvents();
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorCode());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }

    @Test
    public void getEventShouldSuccess() {
        AbstractPartnerResponse partnerResponse = this.incomingRequestService.getEvent(1L);
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((EventDataResponse)partnerResponse).getData());
    }

    @Test
    public void getEventShouldGiveError() {
        when(incomingRequestService.getEvent(any())).thenReturn(new ReservationFailedResponse(false, 123, "MSG"));
        AbstractPartnerResponse partnerResponse = this.incomingRequestService.getEvent(1L);
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorCode());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }

    @Test
    public void payShouldSuccess() {
        AbstractPartnerResponse partnerResponse = this.incomingRequestService.pay(1L, 1L,"1", "token");
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((ReservationSuccessResponse)partnerResponse).getReservationId());
    }

    @Test
    public void payShouldGiveError() {
        when(incomingRequestService.pay(any(), any(), any(), any())).thenReturn(new ReservationFailedResponse(false, 123, "MSG"));
        AbstractPartnerResponse partnerResponse = this.incomingRequestService.pay(1L, 1L, "1", "token");
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorCode());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }
}
