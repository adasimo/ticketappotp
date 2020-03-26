package com.adamsimon.ticketapp.partner.controller;

import com.adamsimon.ticketapp.partner.partnerabstractions.PartnerController;
import com.adamsimon.ticketapp.partner.partnerabstractions.abstracts.AbstractPartnerResponse;
import com.adamsimon.ticketapp.partner.partnerabstractions.interfaces.PartnerService;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/partner")
public class PartnerEventController implements PartnerController {

    PartnerService partnerService;

    @Autowired
    public PartnerEventController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }


    @Override
    @GetMapping("/getevents")
    public ResponseEntity<JSONObject> getEvents() throws IOException, ParseException {
        return new ResponseEntity<>(this.partnerService.getEvents(), HttpStatus.OK);
    }

    @Override
    @GetMapping("/getevent/{id}")
    public ResponseEntity<JSONObject> getEvent(@PathVariable("id") Long eventId) throws IOException, ParseException {
        return new ResponseEntity<>(this.partnerService.getEvent(eventId), HttpStatus.OK);
    }

    @Override
    @PostMapping("/reserve")
    public ResponseEntity<AbstractPartnerResponse> makeReserve(@RequestParam Long eventId, @RequestParam Long seatId) throws IOException, ParseException {
        return new ResponseEntity<>(this.partnerService.makeReservation(eventId, seatId), HttpStatus.OK);
    }
}
