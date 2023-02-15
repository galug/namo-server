package com.example.namo2.schedule.dto;

import com.example.namo2.entity.Period;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class GetScheduleRes {
    private Long scheduleId;
    private String name;

    private Long startDate;

    private Long endDate;

    private Long alarmDate;

    private Point point;

    private Long categoryId;

    private String categoryName;

    private String color;

    @QueryProjection
    public GetScheduleRes(Long scheduleId, String name, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime alarmDate, Point point, Long categoryId, String categoryName, String color) {
        this.scheduleId = scheduleId;
        this.name = name;
        this.startDate = startDate.atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        ;
        this.endDate = endDate.atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.alarmDate = Optional.ofNullable(alarmDate)
                .map((date) -> date.atZone(ZoneId.systemDefault())
                        .toInstant()
                        .getEpochSecond())
                .orElse(0L);
        this.point = point;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.color = color;
    }
}
