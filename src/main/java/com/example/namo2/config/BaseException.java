package com.example.namo2.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends Exception {
    private BaseResponseStatus status;
}
