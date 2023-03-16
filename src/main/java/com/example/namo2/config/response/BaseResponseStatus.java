package com.example.namo2.config.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(200, "요청에 성공하였습니다."),


    /**
     * User, Auth 오류
     * 2000 번대
     */
    NOT_FOUND_USER_FAILURE( 2001, "유저를 찾을 수 없습니다."),
    SOCIAL_LOGIN_FAILURE(2005, "소셜 로그인에 실패하였습니다."),
    EMPTY_ACCESS_KEY(2010, "AccessToken 이 비어있습니다."),
    INVALID_TOKEN(2015, "토큰이 인증이 안됩니다."),
    EXPIRATION_REFRESH_TOKEN(2020, "RefreshToken 이 만료되었습니다."),

    /**
     * schedule 오류
     * 3000 번대
     */
    NOT_FOUND_SCHEDULE_FAILURE(3001, "스케줄을 찾을 수 없습니다."),

    /**
     * 카테고리 오류
     * 4000번 대
     */
    NOT_FOUND_CATEGORY_FAILURE(4001, "카테고리를 찾을 수 없습니다."),
    NOT_FOUND_PALETTE_FAILURE(4011, "팔레트를 찾을 수 없습니다."),

    /**
     * 다이어리 오류
     * 5000 번 대
     */
    NOT_FOUND_DIARY_FAILURE(5001, "다이어리를 찾을 수 없습니다."),
    DIARY_EXISTS_FAILURE(5010, "이미 존재하는 다이어리 입니다."),

    /**
     * utils 관련 오류
     * 7000 번 대
     */

    FILE_NAME_EXCEPTION(7010, "파일 확장자가 잘못되었습니다."),
    S3_FAILURE(7020, "파일 업로드 과정에서 오류가 발생하였습니다."),
    /**
     * 알수 없는 오류
     * 8000
     */
    JPA_FAILURE( 8000, "jpa 오류가 발생했습니다.");

    private final int code;
    private final String message;

    private BaseResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
