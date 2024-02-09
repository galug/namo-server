package com.example.namo2.domain.moim.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MoimScheduleUserDto {
    private Long userId;
    private String userName;
    private Integer color;

    public MoimScheduleUserDto(Long userId, String userName, Integer color) {
        this.userId = userId;
        this.userName = userName;
        this.color = color;
    }
}
