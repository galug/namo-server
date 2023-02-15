package com.example.namo2;

import com.example.namo2.utils.Converter;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateTest {
    @Test
    public void LocalDateToLong() {
        LocalDateTime now = LocalDateTime.of(2023, 2, 11, 3, 9, 0);
        long epochSecond = now
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        System.out.println("epochSecond = " + epochSecond);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.systemDefault());
        assertThat(localDateTime).isEqualTo(now);
    }

    @Test
    public void convertMonthStartDayToEndday() {
        Converter converter = new Converter();
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime("2023/2");
        List<Integer> months = List.of(1, 2, 3);
        List<Integer> days = List.of(1,11, 21);
        List<Long> longLists = new ArrayList<>();
        for (Integer month : months) {
            for (Integer day : days) {
                LocalDateTime timestamp = LocalDateTime.of(2023, month, day, 3, 30);
                System.out.println("of = " + timestamp);
                long epochSecond = timestamp.atZone(ZoneId.systemDefault())
                        .toInstant()
                        .getEpochSecond();
                System.out.println("epochSecond = " + epochSecond);
            }
        }
    }
}
