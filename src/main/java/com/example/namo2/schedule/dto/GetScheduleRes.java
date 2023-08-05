package com.example.namo2.schedule.dto;


import com.example.namo2.entity.schedule.Location;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
@NoArgsConstructor
public class GetScheduleRes {
    private Long scheduleId;
    private String name;
    private Long startDate;
    private Long endDate;
    private List<Integer> alarmDate;
    private Integer interval;
    private Double x;
    private Double y;
    private String locationName;
    private Long categoryId;
    private boolean hasDiary;

    @Builder
    @QueryProjection
    public GetScheduleRes(Long scheduleId, String name, LocalDateTime startDate, LocalDateTime endDate, Integer interval
            , Location location, Long categoryId, Boolean hasDiary) {
        this.scheduleId = scheduleId;
        this.name = name;
        this.startDate = startDate.atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.endDate = endDate.atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.x = location.getX();
        this.y = location.getY();
        this.locationName = location.getLocationName();
        this.categoryId = categoryId;
        this.interval = interval;
        this.hasDiary = hasDiary;
    }

    public void setAlarmDate(List<Integer> alarmDate) {
        this.alarmDate = alarmDate;
    }
}
