package com.adamsimon.commons.errorhandling;

import com.adamsimon.commons.exceptions.NoSuchEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.text.ParseException;

@ControllerAdvice
public class ErrorHandler {

    Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object processValidationError(IOException ie) {
        String result = ie.getMessage();
        logger.error("IOException", ie);
        return result;
    }

    @ExceptionHandler(ParseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object processValidationError(ParseException pe) {
        String result = pe.getMessage();
        logger.error("ParseException", pe);
        return result;
    }

    @ExceptionHandler(NoSuchEventException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object processValidationError(NoSuchEventException nse) {
        String result = nse.getMessage();
        logger.error("NoSuchEventException", nse);
        return result;
    }
}
