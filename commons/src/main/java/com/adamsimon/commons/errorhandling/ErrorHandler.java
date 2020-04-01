package com.adamsimon.commons.errorhandling;

import com.adamsimon.commons.exceptions.CustomNotFoundException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.text.ParseException;

@ControllerAdvice
public class ErrorHandler {

    Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler({ IOException.class, ParseException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object processValidationError(Exception e) {
        String result = e.getMessage();
        logger.error("Exception", e);
        return result;
    }

    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public Object processValidationError(CustomNotFoundException ce) {
        String result = ce.getMessage();
        logger.error("CustomNotFoundException", result);
        return result;
    }
}
