package com.klever.bok.payload.response.error;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString

public class ErrorResponse {
    private Date timestamp;
    private int status;
    private List<String> errors;

    public ErrorResponse(int status, List<String> errors) {
        timestamp = new Date();
        this.status = status;
        this.errors = errors;
    }

    public ErrorResponse(int status, String error) {
        timestamp = new Date();
        this.status = status;
        this.errors = new ArrayList<>();
        this.errors.add(error);
    }
}
