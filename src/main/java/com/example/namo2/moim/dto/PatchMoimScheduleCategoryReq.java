package com.example.namo2.moim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PatchMoimScheduleCategoryReq {
    @NotNull
    private Long moimScheduleId;

    @NotNull
    private Long categoryId;
}
