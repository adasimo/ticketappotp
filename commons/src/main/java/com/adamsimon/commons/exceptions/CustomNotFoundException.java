package com.adamsimon.commons.exceptions;

public class CustomNotFoundException extends InterruptedException {
    public CustomNotFoundException(String error) {
        super(error);
    }
}
