package com.example.namo2.config.exception;

import com.example.namo2.config.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends Exception{
    private BaseResponseStatus status;
}
