package com.edu.issuermicroservice.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class BookObjectJsonException extends JsonProcessingException {
    private final String message;

    public BookObjectJsonException(String msg) {
        super(msg);
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }
}
