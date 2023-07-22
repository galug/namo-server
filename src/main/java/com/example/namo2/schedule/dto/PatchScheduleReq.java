package com.example.namo2.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull
    private Integer eventId;
    private Double x;
    private Double y;
    private String locationName;
    @NotNull
    private Long categoryId;
}
