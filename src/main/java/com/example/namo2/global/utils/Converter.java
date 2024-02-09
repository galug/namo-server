package com.example.namo2.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Converter {
    public List<LocalDateTime> convertLongToLocalDateTime(String year_and_month) {
        List<Integer> ym = Arrays.stream(year_and_month.split(",")).map((s) -> Integer.valueOf(s))
                .collect(Collectors.toList());
        LocalDateTime startMonth = LocalDate.of(ym.get(0), ym.get(1), 1).atStartOfDay();
        LocalDateTime localDateTime = startMonth.plusMonths(1L);
        return List.of(startMonth, localDateTime);
    }

}
