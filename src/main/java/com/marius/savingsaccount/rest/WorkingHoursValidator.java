package com.marius.savingsaccount.rest;

import com.marius.savingsaccount.rest.exception.OutsideWorkingDaysException;
import com.marius.savingsaccount.rest.exception.OutsideWorkingHoursException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Setter
public class WorkingHoursValidator {

    @Value("${business.hours.start:9}")
    private int businessHoursStart;

    @Value("${business.hours.end:17}")
    private int businessHoursEnd;

    @Value("${business.days.start:1}")
    private int businessDaysStart;

    @Value("${business.days.end:5}")
    private int businessDaysEnd;

    private LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    public void validate() {
        LocalDateTime localDateTime = getCurrentLocalDateTime();
        validateInsideBusinessDays(localDateTime);
        validateInsideBusinessHours(localDateTime);
    }

    private void validateInsideBusinessDays(LocalDateTime localDateTime) {
        Optional.of(localDateTime)
                .filter(ldt -> ldt.getDayOfWeek() != DayOfWeek.of(businessDaysStart))
                .filter(ldt -> ldt.getDayOfWeek() != DayOfWeek.of(businessDaysEnd))
                .orElseThrow(() -> new OutsideWorkingDaysException("Outside of working days"));
    }

    private void validateInsideBusinessHours(LocalDateTime localDateTime) {
        Optional.of(localDateTime)
                .filter(ldt -> ldt.getHour() >= businessHoursStart)
                .filter(ldt -> ldt.getHour() <= businessHoursEnd)
                .orElseThrow(() -> new OutsideWorkingHoursException("Outside of business hours"));
    }
}
