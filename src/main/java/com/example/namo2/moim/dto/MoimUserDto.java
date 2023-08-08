package com.example.namo2.moim.dto;

import com.example.namo2.entity.moimschedule.MoimScheduleAndUser;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MoimUserDto {
    private Long userId;
    private String userName;

    public MoimUserDto(MoimScheduleAndUser moimScheduleAndUser) {
        this.userId = moimScheduleAndUser.getUser().getId();
        this.userName = moimScheduleAndUser.getUser().getName();
    }
}
