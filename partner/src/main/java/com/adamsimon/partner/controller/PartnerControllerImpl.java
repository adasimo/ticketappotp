package com.adamsimon.partner.controller;

import static com.adamsimon.commons.constants.Constants.AUTO_CACHE_EVICT_INTERVAL;

import com.adamsimon.partner.interfaces.PartnerService;
import com.adamsimon.partner.interfaces.PartnerController;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/partner", produces = "application/json;charset=UTF-8")
public class PartnerControllerImpl implements PartnerController {

    @Autowired
    private final PartnerService partnerService;

    public PartnerControllerImpl(final PartnerService partnerService) {
        this.partnerService = partnerService;
    }


    @Override
    @GetMapping("/getEvents")
    public ResponseEntity<AbstractPartnerResponse> getEvents() throws IOException, ParseException {
        return new ResponseEntity<>(this.partnerService.getEvents(), HttpStatus.OK);
    }

    @Override
    @GetMapping("/getEvent/{id}")
    public ResponseEntity<AbstractPartnerResponse> getEvent(@PathVariable("id") final Long eventId) throws IOException, ParseException  {
        return new ResponseEntity<>(this.partnerService.getEvent(eventId), HttpStatus.OK);
    }

    @Override
    @PostMapping("/reserve")
    public ResponseEntity<AbstractPartnerResponse> makeReserve(@RequestParam final Long eventId,
                                                               @RequestParam final Long seatId)
            throws IOException, ParseException {
        return new ResponseEntity<>(this.partnerService.makeReservation(eventId, seatId), HttpStatus.OK);
    }

    @Override
    @Scheduled(fixedDelay = AUTO_CACHE_EVICT_INTERVAL)
    public void evictCachesOnSchedule() {
        this.partnerService.evictCacheOnSchedule();
    }
}
