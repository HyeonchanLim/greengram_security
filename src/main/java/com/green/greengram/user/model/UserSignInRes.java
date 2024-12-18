package com.green.greengram.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserSignInRes {
    private long userId;
    private String nickName;
    private String pic;
    private String accessToken;

    // 밑에 2가지는 프론트에서 볼 이유 없음 !
    // upw 는 swagger 표시 안 되지만 응답 때 빼는 역할도 한다.
    @JsonIgnore
    private String upw;
    @JsonIgnore
    private String message;
    // message 를 service 에서 결과값 바로 출력 -> message 쓸 이유 x
}
