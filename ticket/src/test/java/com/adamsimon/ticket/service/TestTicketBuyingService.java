package com.adamsimon.ticket.service;

import static com.adamsimon.commons.constants.Constants.*;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.commons.dto.helperDto.EventData;
import com.adamsimon.commons.dto.helperDto.EventDetails;
import com.adamsimon.commons.dto.helperDto.Seat;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
import com.adamsimon.commons.enums.CurrencyEnum;
import com.adamsimon.ticket.interfaces.PartnerCallerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestTicketBuyingService {
    @Mock//(answer = Answers.RETURNS_MOCKS)
    private PartnerCallerServiceImpl partnerCallerService;
    @InjectMocks
    private TicketBuyingServiceImpl ticketBuyingService;

    @Test
    public void beansShouldNotBeNull() {
        assertNotNull(partnerCallerService);
        assertNotNull(ticketBuyingService);
    }

    @Test
    public void getEventDetailsShouldGetEventDetails() {
        // given
        final AbstractPartnerResponse allResponse = this.helperToGetEventDetails();
        // when
        final EventDetails eventDetails = ticketBuyingService.getEventDetails(allResponse, 1L);
        // then
        assertNotNull(eventDetails);
        assertEquals(1L, eventDetails.getEventId().intValue());
    }

    @Test
    public void getEventDetailsShouldGetEventDetailsNull() {
        // given
        final AbstractPartnerResponse allResponse = this.helperToGetEventDetails();
        // when
        final EventDetails eventDetails = ticketBuyingService.getEventDetails(allResponse, 999L);
        // then
        assertNull(eventDetails);
    }

    @Test
    public void getEventsShouldSuccess() {
        when(this.partnerCallerService.getEvents()).thenReturn(helperToGetEventDetails());
        // given
        final AbstractPartnerResponse partnerResponse = ticketBuyingService.getEvents();
        //then
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((EventsResponse)partnerResponse).getData());
        assertTrue(((EventsResponse)partnerResponse).getData().size() > 0);
    }

    @Test
    public void getEventShouldSuccess() {
        final long eventId = 1;
        when(this.partnerCallerService.getEvent(eventId)).thenReturn(helperToGetEvent(eventId, false));
        // given
        final AbstractPartnerResponse partnerResponse = ticketBuyingService.getEvent(eventId);
        //then
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((EventDataResponse)partnerResponse).getData());
        assertEquals(eventId, ((EventDataResponse)partnerResponse).getData().getEventId().longValue());
    }

    @Test
    public void payShouldSuccess() {
        final long eventId = 2;
        final long seatId = 3;
        final BigDecimal amount = new BigDecimal(1000);

        when(this.partnerCallerService.getEvents()).thenReturn(helperToGetEventDetails());
        when(this.partnerCallerService.getEvent(eventId)).thenReturn(helperToGetEvent(eventId, false));
        when(this.partnerCallerService.pay(eventId, seatId)).thenReturn(helperToGetSuccessResponse());
        // given
        final AbstractPartnerResponse partnerResponse = ticketBuyingService.pay(eventId, seatId, amount);
        //then
        assertNotNull(partnerResponse);
        assertTrue(partnerResponse.getSuccess());
        assertNotNull(((ReservationSuccessResponse)partnerResponse).getReservationId());
    }

//    @Test
//    public void getEventShouldGiveErrors() {
//        final long eventId = 999;
//        MockitoAnnotations.initMocks(this);
//        when(this.partnerCallerService.getEvent(eventId)).thenReturn(helperToGetErrorResponse(ERROR_NO_SUCH_EVENT_TICKET, ERROR_NO_SUCH_EVENT_TICKET_STR));
//        // given
//        final AbstractPartnerResponse partnerResponse = ticketBuyingService.getEvent(eventId);
//        //then
//        System.out.println("TESTTT: " + partnerResponse.toString());
//        assertNotNull(partnerResponse);
//        assertFalse(partnerResponse.getSuccess());
//    }

    private EventsResponse helperToGetEventDetails() {
        final EventsResponse allResponse = new EventsResponse();
        final List<EventDetails> eventDetailsList = new ArrayList<>();
        eventDetailsList.add(new EventDetails(1L, "title1", "location1", "3586353102", "3586353108"));
        eventDetailsList.add(new EventDetails(2L, "title2", "location2", "3586353103", "3586353109"));
        allResponse.setSuccess(true);
        allResponse.setData(eventDetailsList);
        return allResponse;
    }

    private EventDataResponse helperToGetEvent(Long eventId, boolean reserved) {
        final EventDataResponse response = new EventDataResponse();
        final EventData eventData = new EventData();
        final List<Seat> seatList = new ArrayList<>();

        eventData.setEventId(1L);
        seatList.add(new Seat(eventData.toString(), 10, CurrencyEnum.HUF, reserved));
        eventData.setSeats(seatList);

        response.setSuccess(true);
        response.setData(eventData);
        return response;
    }

    private AbstractPartnerResponse helperToGetErrorResponse(final int errorCode, final String errorMessage) {
        return new ReservationBuilder.ReservationResponseBuilder()
                .getFailedBuilder()
                .withErrorCodeToFail(errorCode)
                .withErrorMessageToFail(errorMessage)
                .build();
    }

    private AbstractPartnerResponse helperToGetSuccessResponse() {
        return new ReservationBuilder.ReservationResponseBuilder()
                .getSuccessBuilder()
                .withReservationIdToSuccess(1234567L)
                .build();
    }

    private void helperMockitoGetEvents() {
        when(partnerCallerService.getEvents()).thenReturn(helperToGetEventDetails());
    }
}
