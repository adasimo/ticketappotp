package com.adamsimon.partner.controller;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.helperDto.EventData;
import com.adamsimon.commons.dto.helperDto.EventDetails;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
import com.adamsimon.partner.service.PartnerServiceImpl;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;

import static com.adamsimon.commons.constants.Constants.ERROR_NO_SUCH_EVENT;
import static com.adamsimon.commons.constants.Constants.ERROR_NO_SUCH_EVENT_STR;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestPartnerController {
    @InjectMocks
    private PartnerControllerImpl partnerController;
    @Mock
    private PartnerServiceImpl partnerService;

    private static final long GOOD_EVENT_ID = 1;
    private static final long RANDOM_SEAT_ID = 1;
    private static final long WRONG_EVENT_ID = 999;
    private static final int RANDOM_ERR_CODE = 123;
    private static final String RANDOM_ERR_MSG = "randomMSG";
    private static final long RANDOM_RES_NUM = 1234567;

    @Before
    public void setup() throws IOException, ParseException {
        when(partnerService.getEvents()).thenReturn(new EventsResponse(new ArrayList<EventDetails>(), true));
        when(partnerService.getEvent(GOOD_EVENT_ID)).thenReturn(new EventDataResponse(new EventData(), true));
        when(partnerService.getEvent(WRONG_EVENT_ID)).thenReturn(new ReservationFailedResponse(false, RANDOM_ERR_CODE, RANDOM_ERR_MSG));
        when(partnerService.makeReservation(GOOD_EVENT_ID, RANDOM_SEAT_ID)).thenReturn(new ReservationSuccessResponse(true, RANDOM_RES_NUM));
        when(partnerService.makeReservation(WRONG_EVENT_ID, RANDOM_SEAT_ID)).thenReturn(new ReservationFailedResponse(false, RANDOM_ERR_CODE, RANDOM_ERR_MSG));
    }

    @Test
    public void shouldInit() {
        assertNotNull(partnerController);
        assertNotNull(partnerService);
    }

    @Test
    public void getEventsShouldSuccess() throws IOException, ParseException {
        final AbstractPartnerResponse partnerResponse = partnerController.getEvents().getBody();
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((EventsResponse)partnerResponse).getData());
    }

    @Test
    public void getEventsShouldGiveError() throws IOException, ParseException {
        when(partnerService.getEvents()).thenReturn(new ReservationFailedResponse(false, RANDOM_ERR_CODE, RANDOM_ERR_MSG));
        final AbstractPartnerResponse partnerResponse = partnerController.getEvents().getBody();
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertEquals(RANDOM_ERR_CODE, ((ReservationFailedResponse)partnerResponse).getErrorCode().intValue());
        assertEquals(RANDOM_ERR_MSG, ((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }

    @Test
    public void getEventShouldSucceed() throws IOException, ParseException {
        final AbstractPartnerResponse partnerResponse = partnerController.getEvent(GOOD_EVENT_ID).getBody();
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((EventDataResponse)partnerResponse).getData());
    }

    @Test
    public void getEventShouldGiveError() throws IOException, ParseException {
        final AbstractPartnerResponse partnerResponse = partnerController.getEvent(WRONG_EVENT_ID).getBody();
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertEquals(RANDOM_ERR_CODE, ((ReservationFailedResponse)partnerResponse).getErrorCode().intValue());
        assertEquals(RANDOM_ERR_MSG, ((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }

    @Test
    public void makeReserveShouldSucceed() throws IOException, ParseException {
        final AbstractPartnerResponse partnerResponse = partnerController.makeReserve(GOOD_EVENT_ID, RANDOM_SEAT_ID).getBody();
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertEquals(RANDOM_RES_NUM, ((ReservationSuccessResponse)partnerResponse).getReservationId().intValue());
    }

    @Test
    public void makeReserveShouldGiveError() throws IOException, ParseException {
        final AbstractPartnerResponse partnerResponse = partnerController.makeReserve(WRONG_EVENT_ID, RANDOM_SEAT_ID).getBody();
        assertNotNull(partnerResponse);
        assertFalse(partnerResponse.getSuccess());
        assertEquals(RANDOM_ERR_CODE, ((ReservationFailedResponse)partnerResponse).getErrorCode().intValue());
        assertEquals(RANDOM_ERR_MSG, ((ReservationFailedResponse)partnerResponse).getErrorMessage());
    }
}
