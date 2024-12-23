package com.green.greengram.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    // enum 은 변수 , 상수 등 값을 제대로 입력하지 못하는 경우를 대비해서 쓸 때 유용함
    // enum 은 errorcode 의 name 을 자동 생성해줘서 빼도 됨
    INTERNEL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 내부에서 에러가 발생하였습니다.")
    ,INVALID_PARAMETER(HttpStatus.BAD_REQUEST ,"잘못된 파라미터입니다.")
    ;
//    , AAAA("DDD") // 에러메시지가 더 필요하면 , 찍고 추가
    private final HttpStatus httpStatus;
    private final String message;



}
