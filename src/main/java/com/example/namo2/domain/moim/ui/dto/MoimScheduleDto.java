package com.example.namo2.domain.moim.ui.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MoimScheduleDto {
    private String name;

    private Long startDate;
    private Long endDate;
    private Integer interval;

    private List<MoimScheduleUserDto> users = new ArrayList<>();
    private Long moimId;
    private Long moimScheduleId;
    private boolean isCurMoimSchedule = false;
    private Double x;
    private Double y;
    private String locationName;
    private boolean hasDiaryPlace;


    @QueryProjection
    public MoimScheduleDto(String name, LocalDateTime startDate, LocalDateTime endDate, Integer interval,
                           Long moimId, Long moimScheduleId, Double x, Double y, String locationName) {
        this.name = name;
        this.startDate = startDate.atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.endDate = endDate.atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.interval = interval;
        this.moimId = moimId;
        this.moimScheduleId = moimScheduleId;
        this.x = x;
        this.y = y;
        this.locationName = locationName;
    }

    @QueryProjection
    public MoimScheduleDto(String name, LocalDateTime startDate, LocalDateTime endDate, Integer interval, Long userId, String userName, Integer color) {
        this.name = name;
        this.startDate = startDate.atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.endDate = endDate.atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.interval = interval;
        this.users.add(new MoimScheduleUserDto(userId, userName, color));
        this.hasDiaryPlace = false;
    }

    public void setUsers(List<MoimScheduleUserDto> users, boolean isCurMoimSchedule, boolean hasDiaryPlace) {
        this.users = users;
        this.isCurMoimSchedule = isCurMoimSchedule;
        this.hasDiaryPlace = hasDiaryPlace;
    }
}
