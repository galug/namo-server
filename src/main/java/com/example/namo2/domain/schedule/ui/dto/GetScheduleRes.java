package com.example.namo2.domain.schedule.ui.dto;


import com.example.namo2.domain.moim.domain.MoimScheduleAlarm;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.schedule.domain.Alarm;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

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
    private boolean isMoimSchedule;

    public GetScheduleRes(Schedule schedule) {
        this.scheduleId = schedule.getId();
        this.name = schedule.getName();
        this.startDate = schedule.getPeriod().getStartDate().atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.endDate = schedule.getPeriod().getEndDate().atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.x = schedule.getLocation().getX();
        this.y = schedule.getLocation().getY();
        this.locationName = schedule.getLocation().getLocationName();
        this.categoryId = schedule.getCategory().getId();
        this.interval = schedule.getPeriod().getDayInterval();
        this.hasDiary = schedule.getHasDiary();
        this.alarmDate = schedule.getAlarms().stream().map(Alarm::getAlarmDate).collect(Collectors.toList());
        this.isMoimSchedule = false;
    }

    @QueryProjection
    public GetScheduleRes(MoimScheduleAndUser moimScheduleAndUser) {
        this.scheduleId = moimScheduleAndUser.getMoimSchedule().getId();
        this.name = moimScheduleAndUser.getMoimSchedule().getName();
        this.startDate = moimScheduleAndUser.getMoimSchedule().getPeriod().getStartDate()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.endDate = moimScheduleAndUser.getMoimSchedule().getPeriod().getEndDate()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.x = moimScheduleAndUser.getMoimSchedule().getLocation().getX();
        this.y = moimScheduleAndUser.getMoimSchedule().getLocation().getY();
        this.locationName = moimScheduleAndUser.getMoimSchedule().getLocation().getLocationName();
        this.categoryId = moimScheduleAndUser.getCategory().getId();
        this.interval = moimScheduleAndUser.getMoimSchedule().getPeriod().getDayInterval();
        this.alarmDate = moimScheduleAndUser.getMoimSchedule().getMoimScheduleAlarms()
                .stream().map(MoimScheduleAlarm::getAlarmDate).collect(Collectors.toList());
        this.isMoimSchedule = true;
        this.hasDiary = moimScheduleAndUser.getMoimSchedule().getMoimMemo() != null;
    }
}
