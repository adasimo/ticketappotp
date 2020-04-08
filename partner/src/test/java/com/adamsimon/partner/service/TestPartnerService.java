package com.adamsimon.partner.service;

import static com.adamsimon.commons.constants.Constants.*;

import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
import com.adamsimon.partner.TestConfigurationPartnerBoot;
import com.adamsimon.partner.interfaces.PartnerDatabaseCallerService;
import com.adamsimon.partner.interfaces.PartnerService;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfigurationPartnerBoot.class)
public class TestPartnerService {

    @Autowired
    private PartnerService partnerService;
    @MockBean
    private PartnerDatabaseCallerService partnerDatabaseCallerService;

    @Test
    public void beansShouldNotBeNull() {
        assertNotNull(partnerService);
        assertNotNull(partnerDatabaseCallerService);
    }

    @Test
    public void getEventsShouldWork() {
        try {
            // when
            final AbstractPartnerResponse partnerResponse = partnerService.getEvents();
            // then
            assertNotNull(partnerResponse);
            assertTrue(partnerResponse.getSuccess());
            assertNotNull(((EventsResponse)partnerResponse).getData());
            assertTrue(((EventsResponse)partnerResponse).getData().size() > 0);
        } catch (IOException ie) {
            Assert.fail("File not found");
        } catch (ParseException pe) {
            Assert.fail("File cannot be parsed");
        }
    }

    @Test
    public void getEventShouldSuccessWhenValidEventId() {
        try {
            // given
            final long eventId = 2;
            // when
            final AbstractPartnerResponse partnerResponse = partnerService.getEvent(eventId);
            // then
            assertNotNull(partnerResponse);
            assertTrue(partnerResponse.getSuccess());
            assertNotNull(((EventDataResponse)partnerResponse).getData());
            assertEquals(eventId, ((EventDataResponse)partnerResponse).getData().getEventId().longValue());
        } catch (IOException ie) {
            Assert.fail("File not found");
        } catch (ParseException pe) {
            Assert.fail("File cannot be parsed");
        }
    }

    @Test
    public void getEventShouldReturnErrorWhenInvalidEventId() {
        try {
            // given
            final long eventId = 999;
            // when
            final AbstractPartnerResponse partnerResponse = partnerService.getEvent(eventId);
            // then
            assertNotNull(partnerResponse);
            assertFalse(partnerResponse.getSuccess());
            assertNotNull(((ReservationFailedResponse)partnerResponse).getErrorCode());
            assertEquals(ERROR_NO_SUCH_EVENT, ((ReservationFailedResponse)partnerResponse).getErrorCode().longValue());
            assertEquals(ERROR_NO_SUCH_EVENT_STR, ((ReservationFailedResponse)partnerResponse).getErrorMessage());
        } catch (IOException ie) {
            Assert.fail("File not found");
        } catch (ParseException pe) {
            Assert.fail("File cannot be parsed");
        }
    }

    @Test
    public void makeReservationShouldSuccess() {
        try {
            //given
            final long eventId = 2L;
            final long seatId = 3L;
            //when
            final AbstractPartnerResponse checkerResponse = partnerService.getEvent(eventId);
            //then
            assertNotNull(checkerResponse);
            assertTrue(checkerResponse.getSuccess());
            assertNotNull(((EventDataResponse)checkerResponse).getData());
            assertFalse(((EventDataResponse)checkerResponse).getData().getSeats().stream().filter((item) -> item.getId().equals("S" + seatId)).findFirst().get().getReserved());
            //when
            final AbstractPartnerResponse partnerResponse = partnerService.makeReservation(eventId, seatId);
            //then
            assertNotNull(partnerResponse);
            assertTrue(partnerResponse.getSuccess());
            assertTrue(((ReservationSuccessResponse) partnerResponse).getReservationId() > 0);

        } catch (IOException ie) {
            Assert.fail("File not found");
        } catch (ParseException pe) {
            Assert.fail("File cannot be parsed");
        }
    }

    @Test
    public void makeReservationShouldGiveErrorNoSuchEvent() {
        try {
            //given
            final long eventId = 999L;
            final long seatId = 2L;
            //when
            final AbstractPartnerResponse partnerResponse = partnerService.makeReservation(eventId, seatId);
            //then
            assertNotNull(partnerResponse);
            assertFalse(partnerResponse.getSuccess());
            assertEquals(ERROR_NO_SUCH_EVENT, ((ReservationFailedResponse)partnerResponse).getErrorCode().longValue());
            assertEquals(ERROR_NO_SUCH_EVENT_STR, ((ReservationFailedResponse)partnerResponse).getErrorMessage());
        } catch (IOException ie) {
            Assert.fail("File not found");
        } catch (ParseException pe) {
            Assert.fail("File cannot be parsed");
        }
    }

    @Test
    public void makeReservationShouldGiveErrorNoSuchSeat() {
        try {
            //given
            final long eventId = 2L;
            final long seatId = 999L;
            //when
            final AbstractPartnerResponse partnerResponse = partnerService.makeReservation(eventId, seatId);
            //then
            assertNotNull(partnerResponse);
            assertFalse(partnerResponse.getSuccess());
            assertEquals(ERROR_NO_SUCH_SEAT, ((ReservationFailedResponse)partnerResponse).getErrorCode().longValue());
            assertEquals(ERROR_NO_SUCH_SEAT_STR, ((ReservationFailedResponse)partnerResponse).getErrorMessage());
        } catch (IOException ie) {
            Assert.fail("File not found");
        } catch (ParseException pe) {
            Assert.fail("File cannot be parsed");
        }
    }

    @Test
    public void makeReservationShouldGiveErrorSeatIsReserved() {
        try {
            //given
            final long eventId = 2L;
            final long seatId = 2L;
            //when
            final AbstractPartnerResponse partnerResponse = partnerService.makeReservation(eventId, seatId);
            //then
            assertNotNull(partnerResponse);
            assertFalse(partnerResponse.getSuccess());
            assertEquals(ERROR_SEAT_IS_RESERVED, ((ReservationFailedResponse)partnerResponse).getErrorCode().longValue());
            assertEquals(ERROR_SEAT_IS_RESERVED_STR, ((ReservationFailedResponse)partnerResponse).getErrorMessage());
        } catch (IOException ie) {
            Assert.fail("File not found");
        } catch (ParseException pe) {
            Assert.fail("File cannot be parsed");
        }
    }
}
