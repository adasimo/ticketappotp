package com.adamsimon.api.controller;

import static com.adamsimon.commons.constants.Constants.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.adamsimon.api.interfaces.ApiController;
import com.adamsimon.api.interfaces.ApiService;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", produces = "application/json;charset=UTF-8")
public class ApiControllerImpl implements ApiController {

    private final ApiService apiService;
    private final Logger logger = LoggerFactory.getLogger(ApiControllerImpl.class);
    @Autowired
    public ApiControllerImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    @GetMapping("/getEvents")
    public ResponseEntity<EntityModel<AbstractPartnerResponse>> getEvents() {
        logger.info(GET_EVENTS_NAME);
        final AbstractPartnerResponse eventsResponse = this.apiService.getEvents();
        logger.info("api returns" + GET_EVENTS_NAME + ": " + eventsResponse.toString());
        if (eventsResponse.getSuccess()) {
            return ResponseEntity.ok().body(new EntityModel<>(eventsResponse,
                    linkTo(methodOn(ApiControllerImpl.class).getEvents()).withSelfRel()));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new EntityModel<>(eventsResponse,
                    linkTo(methodOn(ApiControllerImpl.class).getEvents()).withSelfRel()));
        }
    }

    @Override
    @GetMapping("getEvent/{eventId}")
    public ResponseEntity<EntityModel<AbstractPartnerResponse>> getEvent(@PathVariable("eventId") final Long eventId) {
        logger.info(GET_EVENT_NAME);
        final AbstractPartnerResponse eventsResponse = this.apiService.getEvent(eventId);
        logger.info("api returns" + GET_EVENT_NAME + ": " + eventsResponse.toString());
        if (eventsResponse.getSuccess()) {
            return ResponseEntity.ok().body(new EntityModel<>(eventsResponse,
                    linkTo(methodOn(ApiControllerImpl.class).getEvent(eventId)).withSelfRel(),
                    linkTo(methodOn(ApiControllerImpl.class).getEvents()).withRel("getEvents")
            ));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new EntityModel<>(eventsResponse,
                    linkTo(methodOn(ApiControllerImpl.class).getEvent(eventId)).withSelfRel(),
                    linkTo(methodOn(ApiControllerImpl.class).getEvents()).withRel("getEvents")
            ));
        }
    }

    @Override
    @PostMapping(GET_PAY_NAME)
    public ResponseEntity<EntityModel<AbstractPartnerResponse>> pay(@RequestParam final Long eventId,
                                                                    @RequestParam final Long seatId,
                                                                    @RequestParam final String cardId,
                                                                    @RequestHeader(TOKEN_HEADER) final String token) {
        logger.info(GET_PAY_NAME);
        final AbstractPartnerResponse abstractPartnerResponse = this.apiService.pay(eventId, seatId, cardId, token);
        logger.info("api returns" + GET_PAY_NAME + ": " + abstractPartnerResponse.toString());
        if (abstractPartnerResponse.getSuccess()) {
            return ResponseEntity.ok().body(new EntityModel<>(abstractPartnerResponse,
                    linkTo(methodOn(ApiControllerImpl.class).pay(eventId, seatId, cardId, PLACEHOLDER_TOKEN_HEADER))
                            .withSelfRel(),
                    linkTo(methodOn(ApiControllerImpl.class).getEvents()).withRel("getEvents"),
                    linkTo(methodOn(ApiControllerImpl.class).getEvent(eventId)).withRel("getEvent")
        ));
        } else  {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new EntityModel<>(abstractPartnerResponse,
                    linkTo(methodOn(ApiControllerImpl.class).pay(eventId, seatId, cardId, PLACEHOLDER_TOKEN_HEADER))
                            .withSelfRel(),
                    linkTo(methodOn(ApiControllerImpl.class).getEvents()).withRel("getEvents"),
                    linkTo(methodOn(ApiControllerImpl.class).getEvent(eventId)).withRel("getEvent")
            ));
        }
    }

    @Override
    @Scheduled(fixedDelay = AUTO_CACHE_EVICT_INTERVAL)
    public void evictCacheOnSchedule() {
        this.apiService.evictCacheOnSchedule();
    }
}
