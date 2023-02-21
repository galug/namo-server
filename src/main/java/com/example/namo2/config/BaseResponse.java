package com.example.namo2.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.namo2.config.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "result"})
public class BaseResponse<T> {
    private final String message;
    private final int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 요청에 성공한 경우
    public BaseResponse(T result) {
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.result = result;
    }

    // 요청에 실패한 경우
    public BaseResponse(BaseResponseStatus status) {
        this.message = status.getMessage();
        this.code = status.getCode();
    }
}
