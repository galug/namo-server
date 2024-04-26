package com.example.namo2.global.annotation.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.namo2.global.common.response.BaseResponseStatus;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiErrorCode(BaseResponseStatus.INTERNET_SERVER_ERROR)
public @interface ApiErrorCodes {

	BaseResponseStatus[] value();
}
