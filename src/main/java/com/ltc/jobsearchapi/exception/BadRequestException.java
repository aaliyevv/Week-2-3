package com.ltc.jobsearchapi.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {

        super(message);
    }
}
