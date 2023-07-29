package com.example.namo2.moim.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MoimScheduleUserDto {
    private String userName;
    private Integer color;

    @QueryProjection
    public MoimScheduleUserDto(String userName, Integer color) {
        this.userName = userName;
        this.color = color;
    }
}
