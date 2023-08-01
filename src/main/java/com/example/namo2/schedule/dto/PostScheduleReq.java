package com.example.namo2.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class PostScheduleReq {
    @NotBlank
    private String name;
    @NotNull
    private Long startDate;
    @NotNull
    private Long endDate;
    @NotNull
    private Integer interval;
    private Set<Integer> alarmDate;
    private Double x;
    private Double y;
    private String locationName;
    @NotNull
    private Long categoryId;
}
