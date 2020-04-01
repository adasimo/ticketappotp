package com.adamsimon.api.controller;

import static com.adamsimon.commons.constants.Constants.TOKEN_HEADER;
import static com.adamsimon.commons.constants.Constants.PLACEHOLDER_TOKEN_HEADER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.adamsimon.api.assembler.EventAssembler;
import com.adamsimon.api.interfaces.ApiController;
import com.adamsimon.api.interfaces.ApiService;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.exceptions.CustomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiControllerImpl implements ApiController {

    final ApiService apiService;
    final EventAssembler eventAssembler;

    @Autowired
    public ApiControllerImpl(ApiService apiService,
                             EventAssembler eventAssembler) {
        this.apiService = apiService;
        this.eventAssembler = eventAssembler;
    }

    @Override
    @GetMapping("/getEvents")
    public ResponseEntity<EntityModel<AbstractPartnerResponse>> getEvents() {
        final AbstractPartnerResponse eventsResponse = this.apiService.getEvents();
        return ResponseEntity.ok().body(new EntityModel<>(eventsResponse,
                linkTo(methodOn(ApiControllerImpl.class).getEvents()).withSelfRel()
        ));
    }

    @Override
    @GetMapping("/getEvent/{eventId}")
    public ResponseEntity<EntityModel<AbstractPartnerResponse>> getEvent(@PathVariable("eventId") final Long eventId) {
        final AbstractPartnerResponse eventsResponse = this.apiService.getEvent(eventId);
        return ResponseEntity.ok().body(eventAssembler.toModel(eventsResponse));
    }

    @Override
    @PostMapping("/pay")
    public ResponseEntity<EntityModel<AbstractPartnerResponse>> pay(@RequestParam final Long eventId,
                                                                    @RequestParam final Long seatId,
                                                                    @RequestParam final String cardId,
                                                                    @RequestHeader(TOKEN_HEADER) final String token)
            throws CustomNotFoundException {
        final AbstractPartnerResponse abstractPartnerResponse = this.apiService.pay(eventId, seatId, cardId, token);
        if (abstractPartnerResponse.getSuccess()) {
            return ResponseEntity.ok().body(new EntityModel<>(abstractPartnerResponse,
                    linkTo(methodOn(ApiControllerImpl.class).pay(eventId, seatId, cardId, PLACEHOLDER_TOKEN_HEADER))
                            .withSelfRel(),
                    linkTo(methodOn(ApiControllerImpl.class).getEvents()).withRel("getEvents"),
                    linkTo(methodOn(ApiControllerImpl.class).getEvent(eventId)).withRel("getEvent")
        ));
        } else  {
            return ResponseEntity.badRequest().body(new EntityModel<>(abstractPartnerResponse,
                    linkTo(methodOn(ApiControllerImpl.class).pay(eventId, seatId, cardId, PLACEHOLDER_TOKEN_HEADER))
                            .withSelfRel(),
                    linkTo(methodOn(ApiControllerImpl.class).getEvents()).withRel("getEvents"),
                    linkTo(methodOn(ApiControllerImpl.class).getEvent(eventId)).withRel("getEvent")
            ));
        }
    }
}
