package com.example.namo2.schedule.dto;

import com.example.namo2.entity.Period;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.geo.Point;

@AllArgsConstructor
@Getter
public class ScheduleDto {
    private String name;

    Period period;

    private Point point;

    private Long categoryId;

    public ScheduleDto(PostScheduleReq postScheduleReq) {
        Period period = new Period(postScheduleReq.getStartDate(), postScheduleReq.getEndDate(), postScheduleReq.getAlarm());
        Point point = new Point(postScheduleReq.getX(), postScheduleReq.getY());
        this.name = postScheduleReq.getName();
        this.period = period;
        this.point = point;
        this.categoryId = postScheduleReq.getCategoryId();
    }
}
