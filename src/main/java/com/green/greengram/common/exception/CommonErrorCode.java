package com.green.greengram.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    // enum 은 errorcode 의 name 을 자동 생성해줘서 빼도 됨
    INTERNEL_SERVER_ERROR("서버 내부에서 에러가 발생하였습니다.")
    ,INVALID_PARAMETER("잘못된 파라미터입니다.")
    , AAAA("DDD") // 에러메시지가 더 필요하면 , 찍고 추가
    ;
    private final String message;




}
