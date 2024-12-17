package com.green.greengram.config.jwt;

import com.green.greengram.config.security.MyUserDetails;
import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 통합 테스트 때 사용
class TokenProviderTest {
    // 테스트는 생성자를 이용한 DI가 불가능
    // DI 방법은 필드 , setter 메소드 , 생성자
    // 테스트에서는 필드 주입 방식을 사용한다.

    @Autowired // 리플렉션 API 를 이용해서 setter 가 없어도 주입 가능
    private TokenProvider tokenProvider;

    @Test
    public void generateToken(){
        //Given (준비단계)
        JwtUser jwtUser = new JwtUser();
        jwtUser.setSignedUserId(10);

        List<String> roles = new ArrayList<>(2);
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        jwtUser.setRoles(roles);

        //When (실행단계)
        String token = tokenProvider.generateToken(jwtUser, Duration.ofHours(3));

        //Then (검증단계)
        assertNotNull(token);

        System.out.println("token: " + token);
    }

    @Test
    void validToken(){
        // 1분 토큰이라 시간 끝남
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJncmVlbkBncmVlbi5rciIsImlhdCI6MTczNDQwMTYxNywiZXhwIjoxNzM0NDAxNjc3LCJzaWduZWRVc2VyIjoie1wic2lnbmVkVXNlcklkXCI6MTAsXCJyb2xlc1wiOltcIlJPTEVfVVNFUlwiLFwiUk9MRV9BRE1JTlwiXX0ifQ.1tFuKoZSEj_aEs6bWtU1A26gnNhejovskZg35f2yMJdLKs2HPD83pb9SnGrVUXE_pm24L0roo9Nkf-DYkb6BIw";
        boolean result = tokenProvider.validToken(token);
        assertFalse(result);
    }
    @Test
    void getAuthentication(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJncmVlbkBncmVlbi5rciIsImlhdCI6MTczNDQwMzMzMiwiZXhwIjoxNzM0NDE0MTMyLCJzaWduZWRVc2VyIjoie1wic2lnbmVkVXNlcklkXCI6MTAsXCJyb2xlc1wiOltcIlJPTEVfVVNFUlwiLFwiUk9MRV9BRE1JTlwiXX0ifQ.KgnY09ETegk9hS3QgUNe7oCGN4BW57JT8Db0xCo9mTKA8cCH0wwU2NUxjvkaLTlXjbimZJUP2tbPtwQd7VO6Jg"; // 3시간 토큰 생성
        Authentication authentication = tokenProvider.getAuthentication(token);
        assertNotNull(authentication);
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        JwtUser jwtUser = myUserDetails.getJwtUser();

        JwtUser expectedJwtUser = new JwtUser();
        expectedJwtUser.setSignedUserId(10);

        List<String> roles = new ArrayList<>(2);
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        expectedJwtUser.setRoles(roles);

        assertEquals(expectedJwtUser , jwtUser);

    }
}