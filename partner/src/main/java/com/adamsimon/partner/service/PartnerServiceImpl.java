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

    final Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

    @Override
    public JSONObject getEvents() throws IOException, ParseException {
        return fetchDataFromFiles(FilesEnum.GETEVENTS);
    }

    @Override
    public JSONObject getEvent(final Long eventId) throws IOException, ParseException, NoSuchEventException {
        return fetchEvent(eventId);
    }

    @Override
    public AbstractPartnerResponse makeReservation(final Long eventId, final Long seatId)
            throws IOException, ParseException {
        try {
            logger.info("Trying to make reservation with eventId: " + eventId + " for seatId: " + seatId);
            final JSONObject jsonObject = fetchEvent(eventId);
            final Boolean isReserved = fetchSeatIsReserved(jsonObject, seatId);
            if (isReserved == null) {
                logger.warn("ERROR_NO_SUCH_SEAT for eventId: " + eventId + " for seatId: " + seatId);
                return new ReservationBuilder.ReservationResponseBuilder()
                        .getFailedBuilder()
                        .withErrorCodeToFail(ERROR_NO_SUCH_SEAT)
                        .withErrorMessageToFail(ERROR_NO_SUCH_SEAT_STR)
                        .build();
                //new ReservationFailed(false, ERROR_NO_SUCH_SEAT);
            } else if (!isReserved) {
                logger.warn("ERROR_SEAT_IS_RESERVED for eventId: " + eventId + " for seatId: " + seatId);
                return new ReservationBuilder.ReservationResponseBuilder()
                        .getFailedBuilder()
                        .withErrorCodeToFail(ERROR_SEAT_IS_RESERVED)
                        .withErrorMessageToFail(ERROR_SEAT_IS_RESERVED_STR)
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
        } catch (final NoSuchEventException nse) {
            return new ReservationBuilder.ReservationResponseBuilder()
                    .getFailedBuilder()
                    .withErrorCodeToFail(ERROR_NO_SUCH_EVENT)
                    .withErrorMessageToFail(ERROR_NO_SUCH_EVENT_TEXT)
                    .build();
        }

    }

    private Long getReservationNumber() {
        final long range = 1234567L;
        final Random r = new Random();
        final long number = (long)(r.nextDouble()*range);
        logger.info("RESERVATION NUMBER: " + number);
        return number;
    }

    private JSONObject fetchDataFromFiles(final FilesEnum filesEnum) throws IOException, ParseException{
        // át lehetne mappelni objectté, mint a fetchSeatIsReserved metódusban,
        // ha végig iterálok a propertyken és besetelem az objectbe,
        // de fölöslegesnek tartottam, mivel úgyis csak továbbküldésre kerül egy JSON responsebodyként

//        private static final Type REVIEW_TYPE = new TypeToken<List<Review>>() {
//        }.getType();
//        Gson gson = new Gson();
//        JsonReader reader = new JsonReader(new FileReader(filename));
//        List<Review> data = gson.fromJson(reader, REVIEW_TYPE);

        final JSONParser parser = new JSONParser();
        final StringBuilder st = new StringBuilder(new File("").getAbsolutePath());
        st.append(RELATIVE_PATH_TO_JSONS);
        st.append(filesEnum);
        logger.info("Filepath: " + st.toString());
        final Reader reader = new FileReader(st.toString());
        final JSONObject jsonObject = (JSONObject) parser.parse(reader);
        logger.info("FileContents: " + jsonObject);
        return jsonObject;
    }

    private JSONObject fetchEvent(final Long eventId) throws IOException, ParseException, NoSuchEventException {
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
//                return new ReservationBuilder.ReservationResponseBuilder()
//                        .getFailedBuilder()
//                        .withErrorMessageToFail(ERROR_NO_SUCH_EVENT_STR)
//                        .withErrorCodeToFail(ERROR_NO_SUCH_EVENT)
//                        .build();
        }
    }

    private Boolean fetchSeatIsReserved(final JSONObject jsonObject, final Long seatId) {
        final JSONObject jsonObjFromData = (JSONObject) jsonObject.get(PROP_DATA);
        final JSONArray jsonArray = (JSONArray) jsonObjFromData.get(PROP_SEATS);
        final Iterator iterator = jsonArray.iterator();
        Boolean isReserved = null;

        while (iterator.hasNext()) {
            final JSONObject seatObj = (JSONObject) iterator.next();
            final String seatIdString = seatObj.get(PROP_SEAT_ID).toString().substring(1);
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
