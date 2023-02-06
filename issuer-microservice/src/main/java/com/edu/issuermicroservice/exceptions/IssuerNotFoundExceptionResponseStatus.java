package com.edu.issuermicroservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class IssuerNotFoundExceptionResponseStatus extends ResponseStatusException {
    public IssuerNotFoundExceptionResponseStatus(HttpStatus msg) {
        super(msg);
    }
}
