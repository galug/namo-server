package com.example.namo2.schedule.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class DiaryDto {
    private Long scheduleId;
    private String name;
    private LocalDateTime startDate;
    private String texts;
    private List<String> urls;

    @QueryProjection
    public DiaryDto(Long scheduleId, String name, LocalDateTime startDate, String texts, List<String> urls) {
        this.scheduleId = scheduleId;
        this.name = name;
        this.startDate = startDate;
        this.texts = texts;
        this.urls = urls;
    }
}
