package com.adamsimon.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.adamsimon.api.assembler.EventAssembler;
import com.adamsimon.api.interfaces.ApiController;
import com.adamsimon.api.interfaces.ApiService;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.EventDataResponse;
import com.adamsimon.commons.dto.EventsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiControllerImpl implements ApiController {

    ApiService apiService;
    EventAssembler eventAssembler;

    @Autowired
    public ApiControllerImpl(ApiService apiService,
                             EventAssembler eventAssembler) {
        this.apiService = apiService;
        this.eventAssembler = eventAssembler;
    }

    @Override
    @GetMapping("/getEvents")
    public ResponseEntity<EntityModel<EventsResponse>> getEvents() {
        EventsResponse eventsResponse = this.apiService.getEvents();
        return ResponseEntity.ok().body(new EntityModel<>(eventsResponse,
                linkTo(methodOn(ApiControllerImpl.class).getEvents()).withSelfRel()
        ));
    }

    @Override
    @GetMapping("/getEvent/{eventId}")
    public ResponseEntity<EntityModel<EventDataResponse>> getEvent(@PathVariable("eventId") Long eventId) {
        EventDataResponse eventsResponse = this.apiService.getEvent(eventId);
        return ResponseEntity.ok().body(eventAssembler.toModel(eventsResponse));
    }

    @Override
    @PostMapping("/pay")
    public ResponseEntity<EntityModel<AbstractPartnerResponse>> pay(@RequestParam Long eventId,
                                                                    @RequestParam Long seatId,
                                                                    @RequestParam Long cardId) {
        AbstractPartnerResponse abstractPartnerResponse = this.apiService.pay(eventId, seatId, cardId);
        if (abstractPartnerResponse.getSuccess()) {
            return ResponseEntity.ok().body(new EntityModel<>(abstractPartnerResponse,
                    linkTo(methodOn(ApiControllerImpl.class).pay(eventId, seatId, cardId)).withSelfRel(),
                    linkTo(methodOn(ApiControllerImpl.class).getEvents()).withRel("getEvents"),
                    linkTo(methodOn(ApiControllerImpl.class).getEvent(eventId)).withRel("getEvent")
        ));
        } else  {
            return ResponseEntity.badRequest().body(new EntityModel<>(abstractPartnerResponse,
                    linkTo(methodOn(ApiControllerImpl.class).pay(eventId, seatId, cardId)).withSelfRel(),
                    linkTo(methodOn(ApiControllerImpl.class).getEvents()).withRel("getEvents"),
                    linkTo(methodOn(ApiControllerImpl.class).getEvent(eventId)).withRel("getEvent")
            ));
        }
    }
}
