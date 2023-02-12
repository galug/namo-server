package com.example.namo2;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateTest {
    @Test
    public void LocalDateToLong() {
        LocalDateTime now = LocalDateTime.of(2023,2,11,3,9,0);
        long epochSecond = now
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        System.out.println("epochSecond = " + epochSecond);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.systemDefault());
        assertThat(localDateTime).isEqualTo(now);
    }
}
