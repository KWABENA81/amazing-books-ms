package com.edu.bookmicroservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BookNotFoundExceptionResponseStatus extends ResponseStatusException {
    public BookNotFoundExceptionResponseStatus(HttpStatus status) {
        super(status);
    }
}
