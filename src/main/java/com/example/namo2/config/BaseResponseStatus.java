package com.example.namo2.config;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * Schedule 오류
     * 2000 번대
     */
    SCHEDULE_ILLEGAL_ARGUMENT_FAILURE(false, 2012, "잘못된 유저 id 혹은 category id 를 전달하였습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
