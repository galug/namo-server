package com.example.namo2.moim.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class MoimScheduleAlarmDto {
    private Long moimScheduleId;
    private List<Integer> alarmDates;
}
