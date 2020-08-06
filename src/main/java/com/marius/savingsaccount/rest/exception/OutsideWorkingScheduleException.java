package com.marius.savingsaccount.rest.exception;

public class OutsideWorkingScheduleException extends RuntimeException {
    public OutsideWorkingScheduleException(String message) {
        super(message);
    }
}
