package com.adamsimon.commons.constants;

public class Constants {

    public static final String TOKEN_HEADER = "x-auth-token";
    public static final String PLACEHOLDER_TOKEN_HEADER = "***";
    public static final String LOCAL_URL_PREFIX = "http://localhost";
    public static final String LOCAL_PORT = "8080";

    public static final String GET_EVENTS_NAME = "/getEvents/";
    public static final String GET_EVENT_NAME = "/getEvent/";
    public static final String RESERVE = "/reserve";
    public static final String PARTNER_PREFIX = "/partner";

    public static final String RELATIVE_PATH_TO_JSONS = "\\partner\\src\\main\\resources\\static\\";
    public static final String PROP_SEATS = "seats";
    public static final String PROP_DATA = "data";
    public static final String PROP_SEAT_ID = "id";
    public static final String PROP_SEAT_RESERVED = "reserved";

    public static final int ERROR_NO_SUCH_EVENT = 90001;
    public static final String ERROR_NO_SUCH_EVENT_STR = "Nem letezik ilyen esemeny!";

    public static final int ERROR_NO_SUCH_SEAT = 90002;
    public static final String ERROR_NO_SUCH_SEAT_STR = "Nem letezik ilyen szek!";

    public static final int ERROR_SEAT_IS_RESERVED = 90003;
    public static final String ERROR_SEAT_IS_RESERVED_STR = "Mar lefoglalt szekre nem lehet jegyet eladni!";

    public static final String NO_USER_TOKEN_STR = "A felhasznaloi token lejart vagy nem ertelmezheto";
    public static final int NO_USER_TOKEN_CODE = 10051;

    public static final String INVALID_USER_TOKEN_STR = "A felhasznaloi token nem szerepel";
    public static final int INVALID_TOKEN_CODE = 10050;

    public static final String INVALID_USER_TO_CARD_STR = "Ez a bankkartya nem ehhez a felhasznalohoz tartozik";
    public static final int INVALID_USER_TO_CARD_CODE = 10100;

    public static final String AMOUNT_NOT_ENOUGH_STR = "A felhasznalonak nincs elegendo penze hogy megvasarolja a jegyet!";
    public static final int AMOUNT_NOT_ENOUGH_CODE = 10101;

    public static final int ERROR_PARTNER_NOT_FOUND_CODE = 20404;
    public static final String ERROR_PARTNER_NOT_FOUND_STR = "A kulso rendszer nem elerheto!";

    public static final int ERROR_NO_SUCH_EVENT_TICKET = 20001;
    public static final String ERROR_NO_SUCH_EVENT_TICKET_STR = "Nem letezik ilyen szek!";

    public static final int ERROR_NO_SUCH_SEAT_TICKET = 20002;
    public static final String ERROR_NO_SUCH_SEAT_TICKET_STR = "Nem letezik ilyen szek!";

    public static final int ERROR_SEAT_IS_RESERVED_TICKET = 20010;
    public static final String ERROR_SEAT_IS_RESERVED_TICKET_STR = "Mar lefoglalt szekre nem lehet jegyet eladni!";

    public static final int ERROR_EVENT_HAS_STARTED_TICKET = 20011;
    public static final String ERROR_EVENT_HAS_STARTED_TICKET_STR = "Olyan esemenyre, ami mar elkezdodott, nem lehet jegyet eladni!";


    public static final String ERROR_NO_SUCH_EVENT_TEXT = "No such event with id: ";
}
