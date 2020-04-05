package com.adamsimon.partner.service;

import static com.adamsimon.commons.constants.Constants.*;

import com.adamsimon.commons.dto.builders.ReservationBuilder;
import com.adamsimon.commons.dto.helperDto.Seat;
import com.adamsimon.commons.dto.responseDto.EventDataResponse;
import com.adamsimon.commons.dto.responseDto.EventsResponse;
import com.adamsimon.commons.enums.FilesEnum;
import com.adamsimon.partner.interfaces.PartnerDatabaseCallerService;
import com.adamsimon.partner.interfaces.PartnerService;
import com.adamsimon.commons.abstractions.AbstractPartnerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

@Service
public class PartnerServiceImpl implements PartnerService {

    private static final Type EVENTSRESPONSE_TYPE = new TypeToken<EventsResponse>() {}.getType();
    private static final Type EVENTDATARESPONSE_TYPE = new TypeToken<EventDataResponse>() {}.getType();

    private final Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);
    @Autowired
    private final PartnerDatabaseCallerService partnerDatabaseCallerService;

    public PartnerServiceImpl(PartnerDatabaseCallerService partnerDatabaseCallerService) {
        this.partnerDatabaseCallerService = partnerDatabaseCallerService;
    }

    @Override
    public AbstractPartnerResponse getEvents() throws IOException, ParseException {
        return fetchDataFromFiles(FilesEnum.GETEVENTS);
    }

    @Override
    public AbstractPartnerResponse getEvent(final Long eventId) throws IOException, ParseException {
        return fetchEvent(eventId);
    }

    @Override
    public AbstractPartnerResponse makeReservation(final Long eventId, final Long seatId)
            throws IOException, ParseException {

        logger.info("Trying to make reservation with eventId: " + eventId + " for seatId: " + seatId);
        final AbstractPartnerResponse jsonObject = fetchEvent(eventId);
        if (!jsonObject.getSuccess()) {
            logger.info("Reservation failed");
            return jsonObject;
        }
        final Boolean isReserved = fetchSeatIsReserved(jsonObject, seatId);
        if (isReserved == null) {
            logger.info("ERROR_NO_SUCH_SEAT for eventId: " + eventId + " for seatId: " + seatId);
            return new ReservationBuilder.ReservationResponseBuilder()
                    .getFailedBuilder()
                    .withErrorCodeToFail(ERROR_NO_SUCH_SEAT)
                    .withErrorMessageToFail(ERROR_NO_SUCH_SEAT_STR)
                    .build();
        } else if (!isReserved) {
            logger.info("ERROR_SEAT_IS_RESERVED for eventId: " + eventId + " for seatId: " + seatId);
            return new ReservationBuilder.ReservationResponseBuilder()
                    .getFailedBuilder()
                    .withErrorCodeToFail(ERROR_SEAT_IS_RESERVED)
                    .withErrorMessageToFail(ERROR_SEAT_IS_RESERVED_STR)
                    .build();
        } else {
            //itt a beolvasott JSONObjectben a Seat-ben a reservedet true-ra állítani és azt egy FileOutPuttal kiírni a
            //fileba
            logger.info("SEAT_IS_RESERVED for eventId: " + eventId + " for seatId: " + seatId);
            return new ReservationBuilder.ReservationResponseBuilder()
                    .getSuccessBuilder()
                    .withReservationIdToSuccess(getReservationNumber())
                    .build();
        }
    }

    @Override
    public void evictCacheOnSchedule() {
        this.partnerDatabaseCallerService.evictCacheOnSchedule();
    }

    private Long getReservationNumber() {
        final long range = 1234567L;
        final Random r = new Random();
        final long number = (long)(r.nextDouble()*range);
        logger.info("RESERVATION NUMBER: " + number);
        return number;
    }

    private AbstractPartnerResponse fetchDataFromFiles(final FilesEnum filesEnum) throws IOException, ParseException{
        // át lehetne mappelni objectté, mint a fetchSeatIsReserved metódusban,
        // ha végig iterálok a propertyken és besetelem az objectbe,
        // de fölöslegesnek tartottam, mivel úgyis csak továbbküldésre kerül egy JSON responsebodyként
        final StringBuilder st = new StringBuilder(new File("").getAbsolutePath());
        st.append(RELATIVE_PATH_TO_JSONS);
        st.append(filesEnum);
        logger.info("Filepath: " + st.toString());
        final Gson gson = new Gson();
        final JsonReader jsonReader = new JsonReader(new FileReader(st.toString()));
        if (filesEnum.equals(FilesEnum.GETEVENTS)) {
            return gson.fromJson(jsonReader, EVENTSRESPONSE_TYPE);
        } else if (filesEnum.equals(FilesEnum.GETEVENT1)
                || filesEnum.equals(FilesEnum.GETEVENT2)
                || filesEnum.equals(FilesEnum.GETEVENT3)) {
            return gson.fromJson(jsonReader, EVENTDATARESPONSE_TYPE);
        } else {
            return null;
        }
    }

    private AbstractPartnerResponse fetchEvent(final Long eventId) throws IOException, ParseException {
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
                logger.info("ERROR_NO_SUCH_EVENT: " + eventId);
                return new ReservationBuilder.ReservationResponseBuilder()
                        .getFailedBuilder()
                        .withErrorMessageToFail(ERROR_NO_SUCH_EVENT_STR)
                        .withErrorCodeToFail(ERROR_NO_SUCH_EVENT)
                        .build();
        }
    }

    private Boolean fetchSeatIsReserved(final AbstractPartnerResponse jsonObject, final Long seatId) {
        if(jsonObject.getSuccess()) {
            final List<Seat> seatList = ((EventDataResponse) jsonObject).getData().getSeats();
            Boolean isReserved = null;
            for (Seat s : seatList) {
                if (s.getId().substring(1).equals(seatId.toString())) {
                    isReserved = s.getReserved();
                    break;
                }
            }
            logger.info("SeatId is reserved: " + isReserved);
            return isReserved;
        }
        return null;
    }
}
