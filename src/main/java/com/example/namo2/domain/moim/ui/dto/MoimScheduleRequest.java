package com.example.namo2.domain.moim.ui.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MoimScheduleRequest {
    private MoimScheduleRequest() {
        throw new IllegalStateException("Util class");
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class PostMoimScheduleDto {
        @NotNull
        private Long moimId;
        @NotBlank
        private String name;

        @NotNull
        private Long startDate;
        @NotNull
        private Long endDate;
        @NotNull
        private Integer interval;

        private Double x;
        private Double y;
        private String locationName;

        @NotNull
        private List<Long> users;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class PatchMoimScheduleDto {
        @NotNull
        private Long moimScheduleId;
        @NotBlank
        private String name;

        @NotNull
        private Long startDate;
        @NotNull
        private Long endDate;
        @NotNull
        private Integer interval;

        private Double x;
        private Double y;
        private String locationName;

        @NotNull
        private List<Long> users;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public class PatchMoimScheduleCategoryReq {
        @NotNull
        private Long moimScheduleId;

        @NotNull
        private Long categoryId;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public class PatchMoimScheduleCategoryDto {
        @NotNull
        private Long moimScheduleId;

        @NotNull
        private Long categoryId;
    }

    @NoArgsConstructor
    @Getter
    public class PostMoimScheduleAlarmDto {
        private Long moimScheduleId;
        private List<Integer> alarmDates;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public class PostMoimScheduleTextDto {
        private String text;
    }
}
