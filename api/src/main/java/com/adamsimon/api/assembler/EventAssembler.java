package com.adamsimon.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.adamsimon.api.controller.ApiControllerImpl;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EventAssembler implements RepresentationModelAssembler<AbstractPartnerResponse, EntityModel<AbstractPartnerResponse>> {

    @Override
    public EntityModel<AbstractPartnerResponse> toModel(final AbstractPartnerResponse eventDataResponse) {

        return new EntityModel<>(eventDataResponse,
                linkTo(methodOn(ApiControllerImpl.class).getEvent(((EventDataResponse) eventDataResponse).getData().getEventId())).withSelfRel(),
                linkTo(methodOn(ApiControllerImpl.class).getEvents()).withRel("getEvents")
        );
    }
}
