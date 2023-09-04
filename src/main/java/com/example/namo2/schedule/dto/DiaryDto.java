package com.example.namo2.schedule.dto;

import com.example.namo2.entity.moimmemo.MoimMemoLocationImg;
import com.example.namo2.entity.moimschedule.MoimSchedule;
import com.example.namo2.entity.moimschedule.MoimScheduleAndUser;
import com.example.namo2.entity.schedule.Image;
import com.example.namo2.entity.schedule.Schedule;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DiaryDto {
    private Long scheduleId;
    private String name;
    private LocalDateTime startDate;
    private String contents;
    private List<String> urls;
    private Long categoryId;
    private Integer color;
    private String placeName;

    public DiaryDto(Schedule schedule) {
        this.scheduleId = schedule.getId();
        this.name = schedule.getName();
        this.startDate = schedule.getPeriod().getStartDate();
        this.contents = schedule.getContents();
        this.categoryId = schedule.getCategory().getId();
        this.color = schedule.getCategory().getPalette().getColor();
        this.placeName = schedule.getLocation().getLocationName();
        this.urls = schedule.getImages().stream()
                .map(Image::getImgUrl)
                .collect(Collectors.toList());
    }

    public DiaryDto(MoimScheduleAndUser moimScheduleAndUser) {
        this.scheduleId = moimScheduleAndUser.getMoimSchedule().getId();
        this.name = moimScheduleAndUser.getMoimSchedule().getName();
        this.startDate = moimScheduleAndUser.getMoimSchedule().getPeriod().getStartDate();
        this.contents = moimScheduleAndUser.getMemo();
        this.categoryId = moimScheduleAndUser.getCategory().getId();
        this.color = moimScheduleAndUser.getCategory().getPalette().getColor();
        this.placeName = moimScheduleAndUser.getMoimSchedule().getLocation().getLocationName();
        this.urls = moimScheduleAndUser.getMoimSchedule().getMoimMemo()
                .getMoimMemoLocations()
                .stream()
                .flatMap(location -> location.getMoimMemoLocationImgs().stream())
                .map(MoimMemoLocationImg::getUrl)
                .limit(3)
                .collect(Collectors.toList());
    }
}
