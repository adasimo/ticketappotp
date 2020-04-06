package com.adamsimon.commons.constants;

public class Constants {
    
    public static final String TOKEN_HEADER = "User-Token";
    public static final String PLACEHOLDER_TOKEN_HEADER = "***";
    public static final String LOCAL_URL_PREFIX = "http://localhost";
    public static final String LOCAL_PORT = "8080";

    public static final String GET_EVENTS_NAME = "/getEvents/";
    public static final String GET_EVENT_NAME = "/getEvent/";
    public static final String GET_PAY_NAME = "/pay";
    public static final String GET_RESERVE_NAME = "/reserve";
    public static final String RESERVE = "/reserve";
    public static final String PARTNER_PREFIX = "/partner";

    public static final String RELATIVE_PATH_TO_JSONS = "\\partner\\src\\main\\resources\\static\\";

    public static final int ERROR_NO_SUCH_EVENT = 90001;
    public static final String ERROR_NO_SUCH_EVENT_STR = "Nem létezik ilyen esemény!";

    public static final int ERROR_NO_SUCH_SEAT = 90002;
    public static final String ERROR_NO_SUCH_SEAT_STR = "Nem létezik ilyen szék!";

    public static final int ERROR_SEAT_IS_RESERVED = 90003;
    public static final String ERROR_SEAT_IS_RESERVED_STR = "Már lefoglalt székre nem lehet jegyet eladni!";

    public static final String NO_USER_TOKEN_STR = "A felhasználói token nem szerepel!";
    public static final int NO_USER_TOKEN_CODE = 10050;

    public static final String INVALID_USER_TOKEN_STR = "A felhasználói token lejárt vagy nem értelmezhető!";
    public static final int INVALID_TOKEN_CODE = 10051;

    public static final String NO_PARTNER_TOKEN_STR = "A partner token nem szerepel!";
    public static final int NO_PARTNER_TOKEN_CODE = 90050;

    public static final String INVALID_PARTNER_TOKEN_STR = "A partner token lejárt vagy nem értelmezhető!";
    public static final int INVALID_PARTNERTOKEN_CODE = 90051;

    public static final String INVALID_USER_TO_CARD_STR = "Ez a bankkártya nem ehhez a felhasználóhoz tartozik!";
    public static final int INVALID_USER_TO_CARD_CODE = 10100;

    public static final String ERROR_AMOUNT_NOT_ENOUGH_STR = "A felhasználonak nincs elegendő pénze hogy megvásarolja a jegyet!";
    public static final int ERROR_AMOUNT_NOT_ENOUGH_CODE = 10101;

    public static final int ERROR_PARTNER_NOT_FOUND_CODE = 20404;
    public static final String ERROR_PARTNER_NOT_FOUND_STR = "A külső rendszer nem elérhető!";

    public static final int ERROR_NO_SUCH_EVENT_TICKET = 20001;
    public static final String ERROR_NO_SUCH_EVENT_TICKET_STR = "Nem létezik ilyen esemény!";

    public static final int ERROR_NO_SUCH_SEAT_TICKET = 20002;
    public static final String ERROR_NO_SUCH_SEAT_TICKET_STR = "Nem létezik ilyen szék!";

    public static final int ERROR_SEAT_IS_RESERVED_TICKET = 20010;
    public static final String ERROR_SEAT_IS_RESERVED_TICKET_STR = "Már lefoglalt székre nem lehet jegyet eladni!";

    public static final int ERROR_EVENT_HAS_STARTED_TICKET = 20011;
    public static final String ERROR_EVENT_HAS_STARTED_TICKET_STR = "Olyan eseményre, ami már elkezdődött, nem lehet jegyet eladni!";

    public static final int ERROR_NO_JSON = 91404;
    public static final String ERROR_NO_JSON_STR = "Nem létezik az események tárolója!";

    public static final int AUTO_CACHE_EVICT_INTERVAL = 60000;
}
