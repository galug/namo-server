package com.example.namo2.domain.memo.ui.dto;

import com.example.namo2.domain.memo.domain.MoimMemo;
import com.example.namo2.domain.memo.domain.MoimMemoLocationAndUser;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class MoimMemoResponse {

    @NoArgsConstructor
    @Getter
    public static class MoimMemoDto {
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

    @Getter
    @NoArgsConstructor
    public static class MoimUserDto {
        private Long userId;
        private String userName;

        public MoimUserDto(MoimScheduleAndUser moimScheduleAndUser) {
            this.userId = moimScheduleAndUser.getUser().getId();
            this.userName = moimScheduleAndUser.getUser().getName();
        }
    }

    @NoArgsConstructor
    @Getter
    public class MoimMemoLocationDto {
        private Long moimMemoLocationId;
        private String name;
        private Integer money;
        private List<Long> participants;
        private List<String> urls;


        public MoimMemoLocationDto(Long moimMemoLocationId, String name, Integer money, List<String> urls) {
            this.moimMemoLocationId = moimMemoLocationId;
            this.name = name;
            this.money = money;
            this.urls = urls;
        }

        public void addLocationParticipants(List<MoimMemoLocationAndUser> participants) {
            this.participants = participants.stream()
                    .map(moimMemoLocationAndUser -> moimMemoLocationAndUser.getUser().getId())
                    .collect(Collectors.toList());
        }
    }


}
