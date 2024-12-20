package com.green.greengram.config.security;
// spring security 세팅

import com.green.greengram.config.jwt.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration // 메소드 빈등록이 있어야 의미가 있다. 그래야 빈등록을 통해서 싱글톤이 됨
// 그러지 않으면 매번 메소드 호출하면서 사용해야함
@RequiredArgsConstructor
// @EnableWebSecurity 없어도 괜찮음
public class WebSecurityConfig {
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

//    // 스프링 시큐리티 기능 비활성화(스프링 시큐리티가 관여하지 않았으면 하는 부분)
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return web -> web.ignoring()
//                         .requestMatchers(new AntPathRequestMatcher("/static/**"));
//        // /static/ 아래의 모든 파일 match
//
//        // -> 은 lambda 식 : 익명 클래스 , 즉 인터페이스를 객체화하듯이 보임
//        // 1회용으로 사용할꺼니 익명 클래스로 작성하고 메소드는 하나만 있어야함 !
//        // @FunctionalInterface 이 있으면 람다식인지 아닌지 체크 가능함
//    }
    @Bean // 스프링이 메소드 호출을 하고 리턴한 객체의 주소값을 관리한다. (빈등록)

    // REST API 사용할 경우 밑에 Session 4가지는 끄고 시작하면 됨
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
        return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 시큐리티가 세션을 사용하지 않는다
                .httpBasic(h -> h.disable()) // SSR(Server Side Rendering) 이 아니다 화면을 만들지 않을꺼기 때문에 비활성화
                // 시큐리티 로그인 화면이 사라짐
                .formLogin(form -> form.disable()) // 폼로그인도 화면 기능 자체를 off
                .csrf(csrf -> csrf.disable()) // 보안관련 SSR 이 아니면 보안이슈가 없기 때문에 기능을 끈다.
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/api/feed" , "/api/feed/**").authenticated()
                // 위의 api/feed , api/feed/** 주소로 접속 -> 로그인 되어 있어야만 사용 가능 / 인가 처리 부분
                        .requestMatchers(HttpMethod.GET,"/api/user").authenticated()
                        .requestMatchers(HttpMethod.PATCH,"/api/user/pic").authenticated()
                        .anyRequest().permitAll())
                // requestMatchers 는 특정 URL 패턴과 HTTP 메서드를 매칭 -> 요청에 대한 보안 규칙을 적용할지 결정
                // authenticated() 는 요청이 매칭되었을 때 인증된 사용자만 접근을 허용 즉, 로그인이 필요
                // authenticated 같은 접근 제어 메서드, 종결 메서드를 명시하지 않으면 체이닝 구문 오류가 발생
                // 나머지는 모두 허용하겠다는 표시 permitall
                .addFilterBefore(tokenAuthenticationFilter , UsernamePasswordAuthenticationFilter.class)
                // filter 붙은 클래스 /
                .build();
                // builder 는 아니지만 체이닝 기법을 종료하기 위해서 build 로 종료해줌
    }
    @Bean
    // 스프링 시큐리티 자체에 bcrypt 가 있어서 build.gradle 에서 bcrypt 빼도 괜찮음
    // passwordencoder 인터페이스 상속받은 클래스라서 bcrypt 빼서 클래스 정의해도 됨
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
