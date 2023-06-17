package com.example.namo2.config.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 200 : 요청 성공
     */
    SUCCESS(200, "요청 성공"),

    /**
     * 401 : 소셜 로그인 오류
     */

    SOCIAL_LOGIN_FAILURE(401, "소셜 로그인에 실패하였습니다."),

    /**
     * 403 : local Access Token 오류
     */

    EMPTY_ACCESS_KEY(403, "AccessToken 이 비어있습니다."),
    INVALID_TOKEN(403, "토큰이 인증이 안됩니다."),
    EXPIRATION_REFRESH_TOKEN(403, "RefreshToken 이 만료되었습니다."),

    /**
     * NOT FOUND 오류
     * 404 번오류
     */
    NOT_FOUND_USER_FAILURE( 404, "유저를 찾을 수 없습니다."),
    NOT_FOUND_SCHEDULE_FAILURE(404, "스케줄을 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY_FAILURE(404, "카테고리를 찾을 수 없습니다."),
    NOT_FOUND_PALETTE_FAILURE(404, "팔레트를 찾을 수 없습니다."),
    NOT_FOUND_DIARY_FAILURE(404, "다이어리를 찾을 수 없습니다."),
    DIARY_EXISTS_FAILURE(404, "이미 존재하는 다이어리 입니다."),
    FILE_NAME_EXCEPTION(404, "파일 확장자가 잘못되었습니다."),
    S3_FAILURE(404, "파일 업로드 과정에서 오류가 발생하였습니다."),


    /**
     * 500 : 서버 에러
     */
    INTERNET_SERVER_ERROR(500, "서버 오류"),
    JPA_FAILURE( 500, "jpa, sql 상에서 오류가 발생했습니다.");

    private final int code;
    private final String message;

    private BaseResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
