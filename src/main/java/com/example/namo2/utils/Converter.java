package com.example.namo2.utils;

import com.example.namo2.config.exception.BaseException;
import com.example.namo2.config.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Template;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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
