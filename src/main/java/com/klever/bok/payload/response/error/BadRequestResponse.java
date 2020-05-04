package com.klever.bok.payload.response.error;

import org.springframework.http.HttpStatus;

import java.util.List;

public class BadRequestResponse extends ErrorResponse {
    public BadRequestResponse(List<String> errors) {
        super(HttpStatus.BAD_REQUEST.value(), errors);
    }

    public BadRequestResponse(String error) {
        super(HttpStatus.BAD_REQUEST.value(), error);
    }
}
