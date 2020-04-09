package com.adamsimon.api.service;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.helperDto.EventData;
import com.adamsimon.commons.dto.helperDto.EventDetails;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
import com.adamsimon.core.service.IncomingRequestServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestApiService {

    @InjectMocks
    private ApiServiceImpl apiService;
    @Mock
    private IncomingRequestServiceImpl incomingRequestService;
    @Autowired

    @Before
    public void setup() {
        when(incomingRequestService.getEvents()).thenReturn(new EventsResponse(new ArrayList<EventDetails>(), true));
        when(incomingRequestService.getEvent(any())).thenReturn(new EventDataResponse(new EventData(), true));
        when(incomingRequestService.pay(any(), any(), any(), any())).thenReturn(new ReservationSuccessResponse(true,1234567L));
    }
    @Test
    public void propertiesShouldNotBeNull() {
        assertNotNull(apiService);
        assertNotNull(incomingRequestService);
    }

    @Test
    public void getEventsShouldSuccess() {
        AbstractPartnerResponse partnerResponse = this.apiService.getEvents();
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((EventsResponse)partnerResponse).getData());
    }

    @Test
    public void getEventsShouldGiveError() {
        when(incomingRequestService.getEvents()).thenReturn(new ReservationFailedResponse(false, 123, "MSG"));
        AbstractPartnerResponse partnerResponse = this.apiService.getEvents();
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorCode());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }

    @Test
    public void getEventShouldSuccess() {
        AbstractPartnerResponse partnerResponse = this.apiService.getEvent(1L);
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((EventDataResponse)partnerResponse).getData());
    }

    @Test
    public void getEventShouldGiveError() {
        when(incomingRequestService.getEvent(any())).thenReturn(new ReservationFailedResponse(false, 123, "MSG"));
        AbstractPartnerResponse partnerResponse = this.apiService.getEvent(1L);
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorCode());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }

    @Test
    public void payShouldSuccess() {
        AbstractPartnerResponse partnerResponse = this.apiService.pay(1L, 1L,"1", "token");
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((ReservationSuccessResponse)partnerResponse).getReservationId());
    }

    @Test
    public void payShouldGiveError() {
        when(incomingRequestService.pay(any(), any(), any(), any())).thenReturn(new ReservationFailedResponse(false, 123, "MSG"));
        AbstractPartnerResponse partnerResponse = this.apiService.pay(1L, 1L, "1", "token");
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorCode());
        assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }
}
