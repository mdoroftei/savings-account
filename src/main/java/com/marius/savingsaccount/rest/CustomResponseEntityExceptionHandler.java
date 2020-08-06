package com.marius.savingsaccount.rest;

import com.marius.savingsaccount.rest.exception.OutsideWorkingHoursException;
import com.marius.savingsaccount.rest.exception.OutsideWorkingScheduleException;
import com.marius.savingsaccount.rest.exception.SavingsAccountAlreadyExistsException;
import com.marius.savingsaccount.rest.exception.SimpleApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String VALIDATION_FAILED = "Validation failed";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        SimpleApiError simpleApiError = SimpleApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(VALIDATION_FAILED)
                .errors(errors)
                .build();

        return handleExceptionInternal(ex, simpleApiError, headers, simpleApiError.getStatus(), request);
    }

    @ExceptionHandler(SavingsAccountAlreadyExistsException.class)
    protected ResponseEntity<SimpleApiError> handleSavingsAccountAlreadyExistsException(Exception ex, WebRequest request) throws Exception {
        SimpleApiError simpleApiError = SimpleApiError.builder()
                .status(HttpStatus.CONFLICT)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(simpleApiError, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OutsideWorkingScheduleException.class)
    protected ResponseEntity<SimpleApiError> handleOutsideWorkingScheduleException(Exception ex, WebRequest request) throws Exception {
        SimpleApiError simpleApiError = SimpleApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(simpleApiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}