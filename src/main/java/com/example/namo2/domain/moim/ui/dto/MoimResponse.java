package com.example.namo2.domain.moim.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MoimResponse {
    private MoimResponse() {
        throw new IllegalStateException("Util Class");
    }

    @AllArgsConstructor
    @Getter
    public static class MoimIdDto {
        private Long moimId;
    }

    @Getter
    @AllArgsConstructor
    public static class MoimDto {
        private Long groupId;
        private String groupName;
        private String groupImgUrl;
        private String groupCode;
        private List<MoimUserDto> moimUsers;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MoimUserDto {
        private Long userId;
        private String userName;
        private Integer color;
    }
}
