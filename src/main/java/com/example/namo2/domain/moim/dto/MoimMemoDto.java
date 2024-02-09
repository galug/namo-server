package com.example.namo2.domain.moim.dto;

import com.example.namo2.domain.memo.domain.MoimMemo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class MoimMemoDto {
    private String name;
    private Long startDate;
    private String locationName;
    private List<MoimUserDto> users;
    private List<MoimMemoLocationDto> locationDtos;

    public MoimMemoDto(MoimMemo moimMemo) {
        this.name = moimMemo.getMoimSchedule().getName();
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
