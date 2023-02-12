package com.example.namo2.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime alarmDate;

    public Period(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime alarmDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.alarmDate = alarmDate;
    }

    public Period(Long startDate, Long endDate, Long alarmDate) {

        this.startDate = convertLongToLocalDateTime(startDate);
        this.endDate = convertLongToLocalDateTime(endDate);
        this.alarmDate = convertLongToLocalDateTime(alarmDate);
    }

    public LocalDateTime convertLongToLocalDateTime(Long timeStamp) {
        if (timeStamp == 0)
            return null;
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), ZoneId.systemDefault());
    }
}
