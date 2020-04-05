package com.adamsimon.ticket.service;

import static com.adamsimon.commons.constants.Constants.*;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
import com.adamsimon.ticket.interfaces.PartnerCallerService;
import com.adamsimon.ticket.interfaces.TicketDatabaseCallerService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class PartnerCallerServiceImpl implements PartnerCallerService {

    @Autowired
    private final TicketDatabaseCallerService ticketDatabaseCallerService;
    private RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(PartnerCallerServiceImpl.class);

    public PartnerCallerServiceImpl(TicketDatabaseCallerService ticketDatabaseCallerService) {
        this.ticketDatabaseCallerService = ticketDatabaseCallerService;
        setRestTemplate();
    }

    @Override
    public AbstractPartnerResponse getEvents() {
        try {
            final String response = restTemplate.exchange(
                    buildPartnerCall(GET_EVENTS_NAME, null, null),
                    HttpMethod.GET,
                    getHeadersEntity(),
                    String.class
            ).getBody();
            if (response == null) {
                return returnError(ERROR_PARTNER_NOT_FOUND_CODE, ERROR_PARTNER_NOT_FOUND_STR);
            }
            logger.info("Partner call response: " + response);
            return new Gson().fromJson(response, EventsResponse.class);
        } catch (HttpClientErrorException he) {
            return mapPartnerErrorToErrorObj(Objects.requireNonNull(he.getMessage()));
        }
    }

    @Override
    public AbstractPartnerResponse getEvent(final Long eventId) {
        try {
            final String response = restTemplate.exchange(
                    buildPartnerCall(GET_EVENT_NAME, eventId.toString(), null),
                    HttpMethod.GET,
                    getHeadersEntity(),
                    String.class
            ).getBody();
            if (response == null) {
                return returnError(ERROR_PARTNER_NOT_FOUND_CODE, ERROR_PARTNER_NOT_FOUND_STR);
            }
            if (response.contains("\"success\":true")) {
                logger.info("Partner call response: " + response);
                return new Gson().fromJson(response, EventDataResponse.class);
            } else {
                logger.info("Error on Partner call response: " + response);
                return new Gson().fromJson(response, ReservationFailedResponse.class);
            }
        } catch (HttpClientErrorException he) {
            return mapPartnerErrorToErrorObj(Objects.requireNonNull(he.getMessage()));
        }
    }

    @Override
    public AbstractPartnerResponse pay(final Long eventId, final Long seatId) {
        try {
            final String response = restTemplate.exchange(
                    buildPartnerCall(RESERVE, null, getMapForQueryParams(eventId, seatId)),
                    HttpMethod.POST,
                    getHeadersEntity(),
                    String.class
                ).getBody();
            if (response == null) {
                return returnError(ERROR_PARTNER_NOT_FOUND_CODE, ERROR_PARTNER_NOT_FOUND_STR);
            }
            if (response.contains("\"success\":true")) {
                logger.info("Partner call response: " + response);
                return new Gson().fromJson(response, ReservationSuccessResponse.class);
            } else {
                logger.info("Error on Partner call response: " + response);
                return new Gson().fromJson(response, ReservationFailedResponse.class);
            }
        } catch (HttpClientErrorException he) {
            return mapPartnerErrorToErrorObj(Objects.requireNonNull(he.getMessage()));
        }
    }

    @Override
    public AbstractPartnerResponse returnError(final int errorCode, final String errorMessage) {
        logger.info("Error on Partner call: " + errorCode + " " + errorMessage);
        return new ReservationBuilder.ReservationResponseBuilder()
                .getFailedBuilder()
                .withErrorCodeToFail(errorCode)
                .withErrorMessageToFail(errorMessage)
                .build();
    }

    private void setRestTemplate() {
        if (restTemplate == null) {
            this.restTemplate = new RestTemplate();
        }
    }

    private HttpEntity getHeadersEntity() {
        final HttpHeaders headers = new HttpHeaders();
        final String token = this.ticketDatabaseCallerService.getToken();
        headers.set(TOKEN_HEADER, token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        logger.info("Header has been set to Partner");
        return new HttpEntity(headers);
    }

    private String buildPartnerCall(final String methodName, final String pathParam, final Map<String, String> queryParams) {
        final StringBuilder st = new StringBuilder(LOCAL_URL_PREFIX);
        st.append(":");
        st.append(LOCAL_PORT);
        st.append(PARTNER_PREFIX);
        st.append(methodName);
        if (pathParam != null) {
            st.append(pathParam);
        }
        if (queryParams != null) {
            st.append("?");
            for (String key : queryParams.keySet()) {
                st.append(key);
                st.append("=");
                st.append(queryParams.get(key));
                st.append("&");
            }
            st.deleteCharAt(st.lastIndexOf("&"));
        }
        logger.info("Request URL to Partner has been built: " + st.toString());
        return st.toString();
    }

    private Map<String, String> getMapForQueryParams(final Long eventId, final Long seatId) {
        final Map <String, String> map = new HashMap<>();
        map.put("eventId", eventId.toString());
        map.put("seatId", seatId.toString());
        return map;
    }

    private AbstractPartnerResponse mapPartnerErrorToErrorObj(final String message) {
        logger.info("Partner call response error before mapping: " + message);
        final JsonObject jsonObject = new Gson().fromJson(message.substring(7, message.lastIndexOf("]")), JsonObject.class);
        logger.info("Partner call response error after mapping: " + jsonObject.toString());
        return new ReservationBuilder.ReservationResponseBuilder().getFailedBuilder().withErrorCodeToFail(jsonObject.get("errorCode").getAsInt()).withErrorMessageToFail(jsonObject.get("errorMessage").getAsString()).build();
    }
}
