package com.marius.savingsaccount.rest.exception;

public class OutsideWorkingHoursException extends OutsideWorkingScheduleException {
    public OutsideWorkingHoursException(String message) {
        super(message);
    }
}
