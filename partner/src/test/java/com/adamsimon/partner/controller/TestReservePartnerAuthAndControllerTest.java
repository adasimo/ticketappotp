package com.adamsimon.partner.controller;

import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
import com.adamsimon.partner.TestConfigurationPartnerBoot;
import com.adamsimon.partner.interfaces.PartnerController;
import com.adamsimon.partner.interfaces.PartnerDatabaseCallerService;
import com.adamsimon.partner.interfaces.PartnerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.adamsimon.commons.constants.Constants.*;
import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfigurationPartnerBoot.class)
public class TestReservePartnerAuthAndControllerTest {

    @Autowired
    PartnerController partnerController;
    @Autowired
    PartnerService partnerService;
    @Autowired
    Environment environment;
    @MockBean
    PartnerDatabaseCallerService partnerDatabaseCallerService;
    @LocalServerPort
    private String serverPort;
    private TestRestTemplate restTemplate;

    @Before
     public void setup() {
        this.restTemplate = new TestRestTemplate();
    }

    @Test
    public void beansShouldNotBeNull() {
        assertNotNull(partnerController);
        assertNotNull(partnerService);
        assertNotNull(environment);
        assertNotNull(partnerDatabaseCallerService);
        assertNotNull(serverPort);
        assertNotNull(restTemplate);
    }

    @Test
    public void reserveShouldSuccess() {
        assertTrue(this.restTemplate.exchange(LOCAL_URL_PREFIX + ":"
                        + serverPort + "partner/reserve?eventId=2&seatId=3",
                HttpMethod.POST,
                getHeadersEntity(true,true),
                String.class).getStatusCode().is2xxSuccessful());
    }

    @Test
    public void reserveShouldHavePropertiesWhenSuccess() {
        final ReservationSuccessResponse response = this.restTemplate.exchange(LOCAL_URL_PREFIX + ":"
                        + serverPort + "partner/reserve?eventId=2&seatId=3",
                HttpMethod.POST,
                getHeadersEntity(true,true),
                ReservationSuccessResponse.class).getBody();
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNotNull(response.getReservationId());
        assertTrue(response.getReservationId() > 0);
    }

    @Test
    public void reserveShouldGiveNoSuchEvent() {
        final ReservationFailedResponse respose = this.restTemplate.exchange(LOCAL_URL_PREFIX + ":"
                        + serverPort + "partner/reserve?eventId=999&seatId=3",
                HttpMethod.POST,
                getHeadersEntity(true,true),
                ReservationFailedResponse.class).getBody();
        assertNotNull(respose);
        assertFalse(respose.getSuccess());
        assertEquals(ERROR_NO_SUCH_EVENT, respose.getErrorCode().intValue());
        assertEquals(ERROR_NO_SUCH_EVENT_STR, respose.getErrorMessage());
    }

    @Test
    public void reserveShouldGiveNoSuchSeat() {
        final ReservationFailedResponse respose = this.restTemplate.exchange(LOCAL_URL_PREFIX + ":"
                        + serverPort + "partner/reserve?eventId=2&seatId=999",
                HttpMethod.POST,
                getHeadersEntity(true,true),
                ReservationFailedResponse.class).getBody();
        assertNotNull(respose);
        assertFalse(respose.getSuccess());
        assertEquals(ERROR_NO_SUCH_SEAT, respose.getErrorCode().intValue());
        assertEquals(ERROR_NO_SUCH_SEAT_STR, respose.getErrorMessage());
    }


    @Test
    public void reserveShouldGiveSeatIsReserved() {
        final ReservationFailedResponse respose = this.restTemplate.exchange(LOCAL_URL_PREFIX + ":"
                        + serverPort + "partner/reserve?eventId=2&seatId=2",
                HttpMethod.POST,
                getHeadersEntity(true,true),
                ReservationFailedResponse.class).getBody();
        assertNotNull(respose);
        assertFalse(respose.getSuccess());
        assertEquals(ERROR_SEAT_IS_RESERVED, respose.getErrorCode().intValue());
        assertEquals(ERROR_SEAT_IS_RESERVED_STR, respose.getErrorMessage());
    }

    @Test
    public void reserveShouldGiveUnauthorizedWhenInvalidToken() {
        assertTrue(this.restTemplate.exchange(LOCAL_URL_PREFIX + ":"
                        + serverPort + "partner/reserve?eventId=2&seatId=3",
                HttpMethod.GET,
                getHeadersEntity(true,false),
                String.class).getStatusCode().is4xxClientError());
    }

    @Test
    public void reserveShouldGiveErrorInvalidPartnerToken() {
        final ReservationFailedResponse response = this.restTemplate.exchange(LOCAL_URL_PREFIX + ":"
                        + serverPort + "partner/reserve?eventId=2&seatId=3",
                HttpMethod.GET,
                getHeadersEntity(true,false),
                ReservationFailedResponse.class).getBody();
        assertNotNull(response);
        assertFalse(response.getSuccess());
        assertEquals(INVALID_PARTNERTOKEN_CODE, response.getErrorCode().intValue());
        assertEquals(INVALID_PARTNER_TOKEN_STR, response.getErrorMessage());
    }

    @Test
    public void reserveShouldGiveUnauthorizedWhenNoToken() {
        assertTrue(this.restTemplate.exchange(LOCAL_URL_PREFIX + ":"
                        + serverPort + "partner/reserve?eventId=2&seatId=3",
                HttpMethod.GET,
                getHeadersEntity(false,false),
                String.class).getStatusCode().is4xxClientError());
    }

    @Test
    public void reserveShouldGiveErrorNoPartnerToken() {
            final ReservationFailedResponse response = this.restTemplate.exchange(LOCAL_URL_PREFIX + ":"
                            + serverPort + "partner/reserve?eventId=2&seatId=3",
                    HttpMethod.GET,
                    getHeadersEntity(false,false),
                    ReservationFailedResponse.class).getBody();
            assertNotNull(response);
            assertFalse(response.getSuccess());
            assertEquals(NO_PARTNER_TOKEN_CODE, response.getErrorCode().intValue());
            assertEquals(NO_PARTNER_TOKEN_STR, response.getErrorMessage());
    }

    private HttpEntity getHeadersEntity(final boolean tokenIncluded, final boolean isAuthToken) {
        final HttpHeaders headers = new HttpHeaders();
        final String token = "a5eA4E8F7fgu5hk6af3fsaGgfAteAe46FL3aI67EfN";
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (tokenIncluded && isAuthToken) {
            headers.set(TOKEN_HEADER_PARTNER, token);
        } else if (tokenIncluded) {
            headers.set(TOKEN_HEADER_PARTNER, "falseToken");
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(headers);
    }

}
