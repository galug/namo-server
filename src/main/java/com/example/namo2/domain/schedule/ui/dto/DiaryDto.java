package com.example.namo2.domain.schedule.ui.dto;

import com.example.namo2.domain.memo.domain.MoimMemoLocationImg;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.schedule.domain.Image;
import com.example.namo2.domain.schedule.domain.Schedule;
import lombok.Getter;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DiaryDto {
    private Long scheduleId;
    private String name;
    private Long startDate;
    private String contents;
    private List<String> urls;
    private Long categoryId;
    private Long color;
    private String placeName;

    public DiaryDto(Schedule schedule) {
        this.scheduleId = schedule.getId();
        this.name = schedule.getName();
        this.startDate = schedule.getPeriod().getStartDate().atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.contents = schedule.getContents();
        this.categoryId = schedule.getCategory().getId();
        this.color = schedule.getCategory().getPalette().getId();
        this.placeName = schedule.getLocation().getLocationName();
        this.urls = schedule.getImages().stream()
                .map(Image::getImgUrl)
                .collect(Collectors.toList());
    }

    public DiaryDto(MoimScheduleAndUser moimScheduleAndUser) {
        this.scheduleId = moimScheduleAndUser.getMoimSchedule().getId();
        this.name = moimScheduleAndUser.getMoimSchedule().getName();
        this.startDate = moimScheduleAndUser.getMoimSchedule().getPeriod().getStartDate().atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.contents = moimScheduleAndUser.getMemo();
        this.categoryId = moimScheduleAndUser.getCategory().getId();
        this.color = moimScheduleAndUser.getCategory().getPalette().getId();
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
