//package com.adamsimon.ticket.service;
//
//import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
//import com.adamsimon.commons.dto.responseDto.EventsResponse;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Matchers;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.core.env.Environment;
//import org.springframework.http.*;
//import org.springframework.web.client.RestTemplate;
//
//import static com.adamsimon.commons.constants.Constants.TOKEN_HEADER_PARTNER;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class TestPartnerCallerService {
//    @InjectMocks
//    private PartnerCallerServiceImpl partnerCallerService;
//    @Mock
//    private RestTemplate restTemplate;
//    @Mock
//    private Environment environment;
//    @Mock
//    private TicketDatabaseCallerServiceImpl ticketDatabaseCallerService;
//
//    private static final String GETEVENTS_STRING_RESPONSE= "{ \"success\": true, \"data\": [{}] }";
//    private static final String GETEVENT_URI_TEST= "getEvent/1";
//    private static final String RESERVE_URI_TEST= "reserve?eventId=1&seatId=1";
//
//    @Before
//    public void setup() {
//        when(environment.getProperty("local.server.port")).thenReturn("8080");
//        when(ticketDatabaseCallerService.getToken()).thenReturn("token");
//    }
//    @Test
//    public void shouldInit() {
//        assertNotNull(partnerCallerService);
//        assertNotNull(restTemplate);
//        assertNotNull(environment);
//        assertNotNull(ticketDatabaseCallerService);
//    }
//
//    @Test
//    public void getEventsShouldSuccess() {
//        when(partnerCallerService.restTemplate.exchange(Matchers.anyString(),
//                Matchers.any(HttpMethod.class),
//                Matchers.<HttpEntity<?>> any(),
//                Matchers.<Class<String>> any())).thenReturn(ResponseEntity.ok(GETEVENTS_STRING_RESPONSE));
//        AbstractPartnerResponse partnerResponse = partnerCallerService.getEvents();
//        assertNotNull(partnerResponse);
//        assertTrue(partnerResponse.getSuccess());
//        assertNotNull(((EventsResponse)partnerResponse).getData());
//
//    }
//    private HttpEntity getHeadersEntity() {
//        final HttpHeaders headers = new HttpHeaders();
//        final String token = "token";
//        headers.set(TOKEN_HEADER_PARTNER, token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        return new HttpEntity(headers);
//    }
//
//}
