package com.sparta.backoffice.global.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BaseResponse<T> {
    private final String msg;
    private final Integer statusCode;
    private final T data;

    public static <T> BaseResponse<T> of(String msg, Integer statusCode, T data) {
        return new BaseResponse<>(msg, statusCode, data);
    }
}
