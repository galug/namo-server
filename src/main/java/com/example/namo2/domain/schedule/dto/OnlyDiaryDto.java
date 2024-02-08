package com.example.namo2.domain.schedule.dto;

import com.example.namo2.domain.entity.schedule.Image;
import com.example.namo2.domain.entity.schedule.Schedule;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OnlyDiaryDto {
    private Long scheduleId;
    private String contents;
    private List<String> urls;

    @QueryProjection
    public OnlyDiaryDto(Long scheduleId, String contents, List<Image> images) {
        this.scheduleId = scheduleId;
        this.contents = contents;
        this.urls = images
                .stream()
                .map(Image::getImgUrl)
                .collect(Collectors.toList());
    }

    public OnlyDiaryDto(Schedule schedule) {
        this.scheduleId = schedule.getId();
        this.contents = schedule.getContents();
        this.urls = schedule.getImages()
                .stream()
                .map(Image::getImgUrl)
                .collect(Collectors.toList());
    }
}
