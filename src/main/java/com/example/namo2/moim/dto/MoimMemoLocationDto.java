package com.example.namo2.moim.dto;

import com.example.namo2.entity.moimmemo.MoimMemoLocationAndUser;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
