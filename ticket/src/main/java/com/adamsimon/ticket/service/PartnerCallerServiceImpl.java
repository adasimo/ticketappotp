package com.adamsimon.ticket.service;

import static com.adamsimon.commons.constants.Constants.*;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.dto.responseDto.ReservationFailedResponse;
import com.adamsimon.commons.dto.responseDto.ReservationSuccessResponse;
import com.adamsimon.ticket.interfaces.PartnerCallerService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class PartnerCallerServiceImpl implements PartnerCallerService {

    private RestTemplate restTemplate;

    public PartnerCallerServiceImpl() {
        setRestTemplate();
    }

    @Override
    public AbstractPartnerResponse getEvents() {
        final String response = restTemplate.exchange(
                buildPartnerCall(GET_EVENTS_NAME, null, null),
                HttpMethod.GET,
                getHeadersEntity(),
                String.class
        ).getBody();
        if (response == null) {
            return returnNotFoundError();
        }
        return new Gson().fromJson(response, EventsResponse.class);
    }

    @Override
    public AbstractPartnerResponse getEvent(final Long eventId) {
        final String response = restTemplate.exchange(
                buildPartnerCall(GET_EVENT_NAME, eventId.toString(), null),
                HttpMethod.GET,
                getHeadersEntity(),
                String.class
        ).getBody();
        if (response == null) {
            return returnNotFoundError();
        }
        if (response.contains("\"success\":true")) {
            return new Gson().fromJson(response, EventDataResponse.class);
        } else {
            return new Gson().fromJson(response, ReservationFailedResponse.class);
        }
    }

    @Override
    public AbstractPartnerResponse pay(final Long eventId, final Long seatId) {
        final String response = restTemplate.exchange(
                buildPartnerCall(RESERVE, null, getMapForQueryParams(eventId, seatId)),
                HttpMethod.POST,
                getHeadersEntity(),
                String.class
        ).getBody();
        if (response == null) {
            return returnNotFoundError();
        }
        if (response.contains("\"success\":true")) {
            return new Gson().fromJson(response, ReservationSuccessResponse.class);
        } else {
            return new Gson().fromJson(response, ReservationFailedResponse.class);
        }
    }

    @Override
    public AbstractPartnerResponse returnNotFoundError() {
        return new ReservationBuilder.ReservationResponseBuilder()
                .getFailedBuilder()
                .withErrorCodeToFail(ERROR_PARTNER_NOT_FOUND_CODE)
                .withErrorMessageToFail(ERROR_PARTNER_NOT_FOUND_STR)
                .build();
    }

    private void setRestTemplate() {
        if (restTemplate == null) {
            this.restTemplate = new RestTemplate();
        }
    }

    private HttpEntity getHeadersEntity() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(TOKEN_HEADER, "999999999999999999");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(headers);
    }

    private String buildPartnerCall(final String methodName, final String pathParam, final Map<String, String> queryParams) {
        StringBuilder st = new StringBuilder(LOCAL_URL_PREFIX);
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
        return st.toString();
    }

    private Map<String, String> getMapForQueryParams(final Long eventId, final Long seatId) {
        final Map <String, String> map = new HashMap<>();
        map.put("eventId", eventId.toString());
        map.put("seatId", seatId.toString());
        return map;
    }
}
