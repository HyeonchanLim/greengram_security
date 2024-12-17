package com.green.greengram.config.jwt;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode // 어노테이션 사용해줘야 Hash 통해서 userid - roles 연결 가능 
public class JwtUser {
    private long signedUserId;
    private List<String> roles; // 인가(권한)처리 때 사용
    // 인증 절차 체크 -> 권한 레벨 어디까지인지 확인
    // ROLE_이름 , ROLE_USER , ROLE_ADMIN 방식으로 작성
    // 아이디 마다 USER 권한 OR USER + ADMIN 권한이 있으니 LIST 로 묶어서 반복문을 통해 권한 부여
}
