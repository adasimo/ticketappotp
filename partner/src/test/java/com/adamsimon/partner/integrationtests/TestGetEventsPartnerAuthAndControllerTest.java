package com.adamsimon.partner.integrationtests;

import static com.adamsimon.commons.constants.Constants.*;
import static org.junit.Assert.*;

import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfigurationPartnerBoot.class)
public class TestGetEventsPartnerAuthAndControllerTest {

    @Autowired
    private PartnerController partnerController;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private Environment environment;
    @MockBean
    private PartnerDatabaseCallerService partnerDatabaseCallerService;
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
    public void getEventsShouldSuccess() {
        assertTrue(this.restTemplate.exchange(LOCAL_URL_PREFIX + ":" + serverPort + "partner/getEvents",
                HttpMethod.GET,
                getHeadersEntity(true,true),
                String.class).getStatusCode().is2xxSuccessful());
    }

    @Test
    public void getEventsShouldHavePropertiesWhenSuccess() {
        final EventsResponse respose = this.restTemplate.exchange(LOCAL_URL_PREFIX + ":" + serverPort + "partner/getEvents",
                HttpMethod.GET,
                getHeadersEntity(true,true),
                EventsResponse.class).getBody();
        assertNotNull(respose);
        assertTrue(respose.getSuccess());
        assertTrue(respose.getData().size() > 0);
    }

    @Test
    public void getEventsShouldGiveUnauthorizedWhenInvalidToken() {
        assertTrue(this.restTemplate.exchange(LOCAL_URL_PREFIX + ":" + serverPort + "partner/getEvents",
                HttpMethod.GET,
                getHeadersEntity(true,false),
                String.class).getStatusCode().is4xxClientError());
    }

    @Test
    public void getEventsShouldGiveErrorInvalidPartnerToken() {
        final ReservationFailedResponse response = this.restTemplate.exchange(LOCAL_URL_PREFIX + ":" + serverPort + "partner/getEvents",
                HttpMethod.GET,
                getHeadersEntity(true,false),
                ReservationFailedResponse.class).getBody();
        assertNotNull(response);
        assertFalse(response.getSuccess());
        assertEquals(INVALID_PARTNERTOKEN_CODE, response.getErrorCode().intValue());
        assertEquals(INVALID_PARTNER_TOKEN_STR, response.getErrorMessage());
    }

    @Test
    public void getEventsShouldGiveUnauthorizedWhenNoToken() {
        assertTrue(this.restTemplate.exchange(LOCAL_URL_PREFIX + ":" + serverPort + "partner/getEvents",
                HttpMethod.GET,
                getHeadersEntity(false,false),
                String.class).getStatusCode().is4xxClientError());
    }

    @Test
    public void getEventsShouldGiveErrorNoPartnerToken() {
        final ReservationFailedResponse response = this.restTemplate.exchange(LOCAL_URL_PREFIX + ":" + serverPort + "partner/getEvents",
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
        if (tokenIncluded && isAuthToken) {
            headers.set(TOKEN_HEADER_PARTNER, token);
        } else if (tokenIncluded) {
            headers.set(TOKEN_HEADER_PARTNER, "falseToken");
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(headers);
    }

}
