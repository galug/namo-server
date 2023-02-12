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
}
