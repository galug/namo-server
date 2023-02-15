package com.example.namo2.config;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * User 오류
     * 2000 번대
     */
    NOT_FOUND_USER_FAILURE(false, 2001, "유저를 찾을 수 없습니다."),

    /**
     * schedule 오류
     * 3000 번대
     */
    NOT_FOUND_SCHEDULE_FAILURE(false, 3001, "스케줄을 찾을 수 없습니다."),

    /**
     * 카테고리 오류
     * 4000번 대
     */
    NOT_FOUND_CATEGORY_FAILURE(false, 4001, "카테고리를 찾을 수 없습니다."),

    /**
     * utils 관련 오류
     * 7000 번 대
     */

    FILE_NAME_EXCEPTION(false, 7010, "파일 확장자가 잘못되었습니다."),
    S3_FAILURE(false, 7020, "파일 업로드 과정에서 오류가 발생하였습니다."),
    /**
     * 알수 없는 오류
     * 8000
     */
    JPA_FAILURE(false, 8000, "jpa 오류가 발생했습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
