package com.example.namo2.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

import static com.example.namo2.config.BaseResponseStatus.JPA_FAILURE;

@Getter
@AllArgsConstructor
public class BaseException extends Exception{
    private BaseResponseStatus status;
}
