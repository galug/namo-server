package com.example.namo2.domain.memo.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MoimMemoRequest {
    @NoArgsConstructor
    @Getter
    public static class MoimMemoLocationDtos {
        List<LocationDto> locationDtos;
    }

    @NoArgsConstructor
    @Getter
    public static class LocationDto {
        private String name;
        private Integer money;
        private List<Long> participants;

        public LocationDto(String name, String money, String participants) {
            this.name = name;
            this.money = Integer.valueOf(money);
            this.participants = Arrays.stream(participants.replace(" ", "").split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }
    }
}
