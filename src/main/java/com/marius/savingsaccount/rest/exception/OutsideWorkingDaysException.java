package com.marius.savingsaccount.rest.exception;

public class OutsideWorkingDaysException extends OutsideWorkingScheduleException {
    public OutsideWorkingDaysException(String message) {
        super(message);
    }
}
