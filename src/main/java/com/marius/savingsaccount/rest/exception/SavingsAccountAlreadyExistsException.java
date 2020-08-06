package com.marius.savingsaccount.rest.exception;

public class SavingsAccountAlreadyExistsException extends RuntimeException {
    public SavingsAccountAlreadyExistsException(String message) {
        super(message);
    }
}
