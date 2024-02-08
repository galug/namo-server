package com.example.namo2.domain.moim.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetMoimRes {
    private Long groupId;
    private String groupName;
    private String groupImgUrl;
    private String groupCode;
    private List<GetMoimUserRes> moimUsers;
}
