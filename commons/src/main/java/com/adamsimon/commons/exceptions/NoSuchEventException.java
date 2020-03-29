package com.adamsimon.commons.exceptions;

import static com.adamsimon.commons.constants.Constants.ERROR_NO_SUCH_EVENT_TEXT;

public class NoSuchEventException extends IllegalArgumentException {
    public NoSuchEventException() {
        super(ERROR_NO_SUCH_EVENT_TEXT);
    }
}
