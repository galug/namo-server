package com.example.namo2.moim.dto;

import com.example.namo2.entity.moimmemo.MoimMemo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class MoimMemoDto {
    private Long startDate;
    private String locationName;
    private List<MoimUserDto> users;
    private List<MoimMemoLocationDto> locationDtos;

    public MoimMemoDto(MoimMemo moimMemo) {
        this.startDate = moimMemo.getMoimSchedule().getPeriod().getStartDate().atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        this.locationName = moimMemo.getMoimSchedule().getLocation().getLocationName();
        this.users = moimMemo.getMoimSchedule().getMoimScheduleAndUsers().stream()
                .map(MoimUserDto::new)
                .collect(Collectors.toList());
    }

    public void addMoimMemoLocationDto(List<MoimMemoLocationDto> moimMemoLocationDto) {
        this.locationDtos = moimMemoLocationDto;
    }
}
