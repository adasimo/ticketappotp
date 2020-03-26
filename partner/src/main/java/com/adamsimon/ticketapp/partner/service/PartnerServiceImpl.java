package com.adamsimon.ticketapp.partner.service;

import com.adamsimon.ticketapp.partner.dto.ReservationFailed;
import com.adamsimon.ticketapp.partner.dto.ReservationSuccess;
import com.adamsimon.ticketapp.partner.enums.FilesEnum;
import com.adamsimon.ticketapp.partner.partnerabstractions.abstracts.AbstractPartnerResponse;
import com.adamsimon.ticketapp.partner.partnerabstractions.interfaces.PartnerService;
import static com.adamsimon.ticketapp.partner.util.PartnerConstants.*;
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
    public JSONObject getEvents() throws IOException, ParseException{
        return fetchDataFromFiles(FilesEnum.GETEVENTS);
    }

    @Override
    public JSONObject getEvent(Long eventId) throws IOException, ParseException {
        return fetchEvent(eventId);
    }

    @Override
    public AbstractPartnerResponse makeReservation(Long eventId, Long seatId) throws IOException, ParseException {
        logger.info("Trying to make reservation with eventId: " + eventId + " for seatId: " + seatId);
        JSONObject jsonObject = fetchEvent(eventId);
        Boolean isReserved = fetchSeatIsReserved(jsonObject, seatId);
        if (isReserved == null) {
            logger.error("ERROR_NO_SUCH_SEAT for eventId: " + eventId + " for seatId: " + seatId);
            return new ReservationFailed(false, ERROR_NO_SUCH_SEAT);
        } else if (!isReserved) {
            logger.error("ERROR_SEAT_IS_RESERVED for eventId: " + eventId + " for seatId: " + seatId);
            return new ReservationFailed(false, ERROR_SEAT_IS_RESERVED);
        } else {
            logger.info("SEAT_IS_RESERVED for eventId: " + eventId + " for seatId: " + seatId);
            return new ReservationSuccess(true, getReservationNumber());
        }
    }

    private Long getReservationNumber() {
        long range = 1234567L;
        Random r = new Random();
        long number = (long)(r.nextDouble()*range);
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

    private JSONObject fetchEvent(Long eventId) throws IOException, ParseException {
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
                throw new IOException("ERROR IN FILELOADING");
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
