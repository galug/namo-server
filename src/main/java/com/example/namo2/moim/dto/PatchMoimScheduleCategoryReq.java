package com.example.namo2.moim.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PatchMoimScheduleCategoryReq {
    @NotNull
    private Long moimScheduleId;

    @NotNull
    private Long categoryId;
}
