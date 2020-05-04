package com.klever.bok.payload.response.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomGlobalExceptionHandler.class);

    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        logger.warn("Sorting errors");
        errors.sort(Comparator.comparing( String::toString ) );
        logger.warn(errors.toString());

        ErrorResponse errorResponse = new ErrorResponse(status.value(),errors);

        return new ResponseEntity<>(errorResponse, headers, status);

    }

}
