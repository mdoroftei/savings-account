package com.marius.savingsaccount.rest.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class SimpleApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;
}
