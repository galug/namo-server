package com.example.namo2.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PatchScheduleReq {
    @NotBlank
    private String name;
    @NotNull
    private Long startDate;
    @NotNull
    private Long endDate;
    @NotNull
    private Integer interval;
    private List<Integer> deleteAlarmDate;
    private List<Integer> addAlarmDate;
    private Double x;
    private Double y;
    private String locationName;
    @NotNull
    private Long categoryId;
}
