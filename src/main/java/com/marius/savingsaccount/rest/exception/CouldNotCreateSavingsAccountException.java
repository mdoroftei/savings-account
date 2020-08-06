package com.marius.savingsaccount.rest.exception;

public class CouldNotCreateSavingsAccountException extends RuntimeException {
    public CouldNotCreateSavingsAccountException(String message) {
        super(message);
    }
}