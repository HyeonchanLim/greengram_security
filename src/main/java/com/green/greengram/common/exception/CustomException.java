package com.green.greengram.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class CustomException extends RuntimeException {
    // 담고 싶은 에러를 담아서 throw 할꺼임
    public final ErrorCode errorCode;
    // commom , user errorcode 2가지의 enum 을 담을 수 있음
}
