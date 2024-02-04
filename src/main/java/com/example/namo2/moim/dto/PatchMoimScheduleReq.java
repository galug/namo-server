package com.example.namo2.moim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PatchMoimScheduleReq {
    @NotNull
    private Long moimScheduleId;
    @NotBlank
    private String name;

    @NotNull
    private Long startDate;
    @NotNull
    private Long endDate;
    @NotNull
    private Integer interval;

    private Double x;
    private Double y;
    private String locationName;

    @NotNull
    private List<Long> users;
}
