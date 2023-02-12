package com.example.namo2.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Component
@RequiredArgsConstructor
public class Converter {
    public LocalDateTime convertLongToLocalDateTime(Long timeStamp) {
        if (timeStamp == 0)
            return null;
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), TimeZone
                .getDefault().toZoneId());
    }
}
