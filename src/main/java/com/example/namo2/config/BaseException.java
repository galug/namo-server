package com.example.namo2.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
@AllArgsConstructor
public class BaseException extends Exception {
    private BaseResponseStatus status;
}
