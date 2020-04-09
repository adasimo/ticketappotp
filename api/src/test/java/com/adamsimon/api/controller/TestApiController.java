package com.adamsimon.api.controller;

import com.adamsimon.api.service.ApiServiceImpl;
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
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestApiController {

    @InjectMocks
    private ApiControllerImpl apiController;
    @Mock
    private ApiServiceImpl apiService;

    private static final long GOOD_EVENT_ID = 1;
    private static final long RANDOM_SEAT_ID = 1;
    private static final String RANDOM_CARD_ID = "C0001";
    private static final String RANDOM_TOKEN = "token";
    private static final long WRONG_EVENT_ID = 999;
    private static final int RANDOM_ERR_CODE = 123;
    private static final String RANDOM_ERR_MSG = "randomMSG";
    private static final long RANDOM_RES_NUM = 1234567;

    @Before
    public void setup() {
        when(apiService.getEvents()).thenReturn(new EventsResponse(new ArrayList<EventDetails>(), true));
        when(apiService.getEvent(GOOD_EVENT_ID)).thenReturn(new EventDataResponse(new EventData(), true));
        when(apiService.getEvent(WRONG_EVENT_ID)).thenReturn(new ReservationFailedResponse(false, RANDOM_ERR_CODE, RANDOM_ERR_MSG));
        when(apiService.pay(GOOD_EVENT_ID, RANDOM_SEAT_ID, RANDOM_CARD_ID, RANDOM_TOKEN)).thenReturn(new ReservationSuccessResponse(true, RANDOM_RES_NUM));
        when(apiService.pay(WRONG_EVENT_ID, RANDOM_SEAT_ID, RANDOM_CARD_ID, RANDOM_TOKEN)).thenReturn(new ReservationFailedResponse(false, RANDOM_ERR_CODE, RANDOM_ERR_MSG));
    }

    @Test
    public void shouldInitTest() {
        assertNotNull(apiController);
        assertNotNull(apiService);
    }

    @Test
    public void getEventsShouldSuccessTest() {
        final EntityModel<AbstractPartnerResponse> partnerResponse = apiController.getEvents().getBody();
        assertNotNull(partnerResponse);
        assertNotNull(partnerResponse.getContent());
        assertTrue(partnerResponse.getContent().getSuccess());
        assertNotNull(((EventsResponse)partnerResponse.getContent()).getData());
        assertTrue(partnerResponse.getLink("self").isPresent());
        assertTrue(partnerResponse.getLink("self").get().getHref().contains("getEvents"));
    }

    @Test
    public void getEventsShouldGiveErrorTest() {
        when(apiService.getEvents()).thenReturn(new ReservationFailedResponse(false, RANDOM_ERR_CODE, RANDOM_ERR_MSG));
        final EntityModel<AbstractPartnerResponse> partnerResponse = apiController.getEvents().getBody();
        assertNotNull(partnerResponse);
        assertNotNull(partnerResponse.getContent());
        assertFalse(partnerResponse.getContent().getSuccess());
        assertEquals(RANDOM_ERR_CODE, ((ReservationFailedResponse)partnerResponse.getContent()).getErrorCode().intValue());
        assertEquals(RANDOM_ERR_MSG, ((ReservationFailedResponse)partnerResponse.getContent()).getErrorMessage());
        assertTrue(partnerResponse.getLink("self").isPresent());
        assertTrue(partnerResponse.getLink("self").get().getHref().contains("getEvents"));
    }

    @Test
    public void getEventShouldSucceedTest() {
        final EntityModel<AbstractPartnerResponse> partnerResponse = apiController.getEvent(GOOD_EVENT_ID).getBody();
        assertNotNull(partnerResponse);
        assertNotNull(partnerResponse.getContent());
        assertTrue(partnerResponse.getContent().getSuccess());
        assertNotNull(((EventDataResponse)partnerResponse.getContent()).getData());
        assertTrue(partnerResponse.getLink("self").isPresent());
        assertTrue(partnerResponse.getLink("self").get().getHref().contains("getEvent"));
        assertTrue(partnerResponse.getLink("getEvents").isPresent());
        assertTrue(partnerResponse.getLink("getEvents").get().getHref().contains("getEvents"));
    }

    @Test
    public void getEventShouldGiveErrorTest() {
        final EntityModel<AbstractPartnerResponse> partnerResponse = apiController.getEvent(WRONG_EVENT_ID).getBody();
        assertNotNull(partnerResponse);
        assertNotNull(partnerResponse.getContent());
        assertFalse(partnerResponse.getContent().getSuccess());
        assertEquals(RANDOM_ERR_CODE, ((ReservationFailedResponse)partnerResponse.getContent()).getErrorCode().intValue());
        assertEquals(RANDOM_ERR_MSG, ((ReservationFailedResponse)partnerResponse.getContent()).getErrorMessage());
        assertTrue(partnerResponse.getLink("self").isPresent());
        assertTrue(partnerResponse.getLink("self").get().getHref().contains("getEvent"));
        assertTrue(partnerResponse.getLink("getEvents").isPresent());
        assertTrue(partnerResponse.getLink("getEvents").get().getHref().contains("getEvents"));
    }

    @Test
    public void makeReserveShouldSucceedTest() {
        final EntityModel<AbstractPartnerResponse> partnerResponse = apiController.pay(GOOD_EVENT_ID, RANDOM_SEAT_ID, RANDOM_CARD_ID, RANDOM_TOKEN).getBody();
        assertNotNull(partnerResponse);
        assertNotNull(partnerResponse.getContent());
        assertTrue(partnerResponse.getContent().getSuccess());
        assertEquals(RANDOM_RES_NUM, ((ReservationSuccessResponse)partnerResponse.getContent()).getReservationId().intValue());
        assertTrue(partnerResponse.getLink("self").isPresent());
        assertTrue(partnerResponse.getLink("self").get().getHref().contains("pay"));
        assertTrue(partnerResponse.getLink("getEvents").isPresent());
        assertTrue(partnerResponse.getLink("getEvents").get().getHref().contains("getEvents"));
        assertTrue(partnerResponse.getLink("getEvent").isPresent());
        assertTrue(partnerResponse.getLink("getEvent").get().getHref().contains("getEvent"));
    }

    @Test
    public void makeReserveShouldGiveErrorTest() {
        final EntityModel<AbstractPartnerResponse> partnerResponse = apiController.pay(WRONG_EVENT_ID, RANDOM_SEAT_ID, RANDOM_CARD_ID, RANDOM_TOKEN).getBody();
        assertNotNull(partnerResponse);
        assertNotNull(partnerResponse.getContent());
        assertFalse(partnerResponse.getContent().getSuccess());
        assertEquals(RANDOM_ERR_CODE, ((ReservationFailedResponse)partnerResponse.getContent()).getErrorCode().intValue());
        assertEquals(RANDOM_ERR_MSG, ((ReservationFailedResponse)partnerResponse.getContent()).getErrorMessage());
        assertTrue(partnerResponse.getLink("self").isPresent());
        assertTrue(partnerResponse.getLink("self").get().getHref().contains("pay"));
        assertTrue(partnerResponse.getLink("getEvents").isPresent());
        assertTrue(partnerResponse.getLink("getEvents").get().getHref().contains("getEvents"));
        assertTrue(partnerResponse.getLink("getEvent").isPresent());
        assertTrue(partnerResponse.getLink("getEvent").get().getHref().contains("getEvent"));
    }
}
