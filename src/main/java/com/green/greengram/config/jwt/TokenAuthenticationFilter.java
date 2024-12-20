package com.green.greengram.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// security 와 연결하기 위해서 빈등록 해줘야 함
@Slf4j
@Component
@RequiredArgsConstructor
// OncePerRequestFilter 상속 받아야 필터 끼워줄 수 있음
// once 필터는 호출 시 한번만 실행함 -> 중복 없어짐
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    // filterchain 으로 다음 필터에 패스함
    protected void doFilterInternal(HttpServletRequest request // request 에 리턴 -> 다음 필터 절차에서 또 호출 가능 -> 재활용 가능
                                    , HttpServletResponse response
                                    , FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION); // bearer 토큰값
        log.info("ip address: {}", request.getRemoteAddr());
        // header 키값인 Authorization 확인 -> 아니면 null
        log.info("authorizationHeader: {}" , authorizationHeader);

        String token = getAccessToken(authorizationHeader);
        log.info("token: {}" , token);
        if (tokenProvider.validToken(token)){
            Authentication auth = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } // token 유효하면 auth 담을꺼고 아니면 request , response 반환
        // 요청 들어오는 token(bearer 제외) 값만 확인 절차 걸침 -> 요청 때만 실행

        filterChain.doFilter(request,response); // request - response 필터로 연결
    }
    private String getAccessToken(String authorizationHeader){
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)){
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
