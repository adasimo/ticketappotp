package com.adamsimon.partner.service;

import static com.adamsimon.commons.constants.Constants.*;

import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.commons.enums.FilesEnum;
import com.adamsimon.commons.exceptions.NoSuchEventException;
import com.adamsimon.partner.interfaces.PartnerService;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Random;

@Service
public class PartnerServiceImpl implements PartnerService {

    Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

    @Override
    public JSONObject getEvents() throws IOException, ParseException {
        return fetchDataFromFiles(FilesEnum.GETEVENTS);
    }

    @Override
    public JSONObject getEvent(Long eventId) throws IOException, ParseException, NoSuchEventException {
        return fetchEvent(eventId);
    }

    @Override
    public AbstractPartnerResponse makeReservation(Long eventId, Long seatId) throws IOException, ParseException {
        logger.info("Trying to make reservation with eventId: " + eventId + " for seatId: " + seatId);
        JSONObject jsonObject = null;
        try {
           jsonObject = fetchEvent(eventId);
        } catch (NoSuchEventException nse) {
            return new ReservationBuilder.ReservationResponseBuilder()
                    .getFailedBuilder()
                    .withErrorCodeToFail(ERROR_NO_SUCH_EVENT)
                    .build();
        }
        Boolean isReserved = fetchSeatIsReserved(jsonObject, seatId);
        if (isReserved == null) {
            logger.warn("ERROR_NO_SUCH_SEAT for eventId: " + eventId + " for seatId: " + seatId);
            return new ReservationBuilder.ReservationResponseBuilder()
                    .getFailedBuilder()
                    .withErrorCodeToFail(ERROR_NO_SUCH_SEAT)
                    .build();
            //new ReservationFailed(false, ERROR_NO_SUCH_SEAT);
        } else if (!isReserved) {
            logger.warn("ERROR_SEAT_IS_RESERVED for eventId: " + eventId + " for seatId: " + seatId);
            return new ReservationBuilder.ReservationResponseBuilder()
                    .getFailedBuilder()
                    .withErrorCodeToFail(ERROR_SEAT_IS_RESERVED)
                    .build();
//            return new ReservationFailed(false, ERROR_SEAT_IS_RESERVED);
        } else {
            logger.info("SEAT_IS_RESERVED for eventId: " + eventId + " for seatId: " + seatId);
            return new ReservationBuilder.ReservationResponseBuilder()
                    .getSuccessBuilder()
                    .withReservationIdToSuccess(getReservationNumber())
                    .build();
//            return new ReservationSuccess(true, getReservationNumber());
        }
    }

    private Long getReservationNumber() {
        long range = 1234567L;
        Random r = new Random();
        long number = (long)(r.nextDouble()*range);
        logger.info("RESERVATION NUMBER: " + number);
        return number;
    }

    private JSONObject fetchDataFromFiles(FilesEnum filesEnum) throws IOException, ParseException{
        // át lehetne mappelni objectté, mint a fetchSeatIsReserved metódusban,
        // ha végig iterálok a propertyken és besetelem az objectbe,
        // de fölöslegesnek tartottam, mivel úgyis csak továbbküldésre kerül egy JSON responsebodyként
        JSONParser parser = new JSONParser();
        StringBuilder st = new StringBuilder(new File("").getAbsolutePath());
        st.append(RELATIVE_PATH_TO_JSONS);
        st.append(filesEnum);
        logger.info("Filepath: " + st.toString());
        Reader reader = new FileReader(st.toString());
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        logger.info("FileContents: " + jsonObject);
        return jsonObject;
    }

    private JSONObject fetchEvent(Long eventId) throws IOException, ParseException, NoSuchEventException {
      // megoldható lenne hogy átnézzem a fájlokat az eventId-t keresve és utána azt betöltsem, de a jelen helyzetben
      // ez egyszerűbb megoldás
        switch (eventId.intValue()) {
            case 1:
                return fetchDataFromFiles(FilesEnum.GETEVENT1);
            case 2:
                return fetchDataFromFiles(FilesEnum.GETEVENT2);
            case 3:
                return fetchDataFromFiles(FilesEnum.GETEVENT3);
            default:
                throw new NoSuchEventException();
        }
    }

    private Boolean fetchSeatIsReserved(JSONObject jsonObject, Long seatId) {
        JSONObject jsonObjFromData = (JSONObject) jsonObject.get(PROP_DATA);
        JSONArray jsonArray = (JSONArray) jsonObjFromData.get(PROP_SEATS);
        Iterator iterator = jsonArray.iterator();
        Boolean isReserved = null;

        while (iterator.hasNext()) {
            JSONObject seatObj = (JSONObject) iterator.next();
            String seatIdString = seatObj.get(PROP_SEAT_ID).toString().substring(1);
            if (seatId.equals(Long.parseLong(seatIdString))) {
                isReserved = Boolean.parseBoolean(seatObj.get(PROP_SEAT_RESERVED).toString());
                break;
            }
        }
        return isReserved;
    }

//    private EventsResponse fillEventsResponse(JSONObject jsonObject) {
//        // reflectionos megoldás szebb lenne
//        EventsResponse eventsResponse = new EventsResponse();
//        eventsResponse.setSuccess((Boolean) jsonObject.get("success"));
//
//    }
//
//    private EventDetails[] fillEventDetails()

}
