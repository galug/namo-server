package com.example.namo2.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostScheduleReq {
    private String name;
    private Long startDate;
    private Long endDate;
    private Long alarm;
    private double x;
    private double y;
    private Long categoryId;
}
