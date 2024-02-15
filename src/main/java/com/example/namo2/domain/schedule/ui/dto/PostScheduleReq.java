package com.example.namo2.domain.schedule.ui.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
