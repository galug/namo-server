package com.example.namo2.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class PostScheduleReq {
    @NotBlank
    private String name;
    @NotNull
    private Long startDate;
    @NotNull
    private Long endDate;
    private Long alarmDate;
    private Double x;
    private Double y;
    @NotNull
    private Long categoryId;
}
