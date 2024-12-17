package com.green.greengram.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.config.security.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final SecretKey secretKey;

    //DI(Defendency Injection) - 외부에서 들어오는 데이터를 DI 라고 함
    // 스프링컨테이너 - 빈등록 되어서 가능 / @Service - @Component 최상위 어노테이션 으로 빈등록
    public TokenProvider(ObjectMapper objectMapper , JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.objectMapper = objectMapper;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProperties.getSecretKey()));
        // hamc 대칭 , sha 암호화
        // BASE64URL 을 인코딩 -> decode
    }
    // jackson 라이브러리 - json 객체 형태 직렬화 해주기 위해서 사용
    // di 받아올꺼임

    //jwt 토큰 생성 , Duration (기간 설정)
    public String generateToken(JwtUser jwtUser , Duration expiredAt){
        Date now = new Date();
        return makeToken(jwtUser , new Date(now.getTime() + expiredAt.toMillis()));
        // jwtuser(signeduserid) 접속 유저 아이디(정보) + 토큰 시간 으로 리턴
        // date 에서 현재시간 + 생성했을 당시 유효기간 = 종료 시간 설정
    }
    // 토큰에는 헤더 , 내용 , 서명 이 있어야함
    private String makeToken(JwtUser jwtUser , Date expiry) {
        return Jwts.builder()
                .header().add("typ","JWT")
                         .add("alg","hs256") // 여기까지 헤더 부분
                .and()
                .issuer(jwtProperties.getIssuer()) // issuer - yaml 에 작성한 green@green.kr - 이메일
                .issuedAt(new Date()) // 토큰 생성 시점 시간
                .expiration(expiry) // exp - 토큰 종료 시점 시간
                .claim("signedUser", makeClaimByUserToString(jwtUser))// 여기까지 내용(payload) ,
                .signWith(secretKey)// jwt 암호화
                .compact();// 여기까지 서명(signature) , compact -> 여기까지 정리하고 암호화 jwt 만듦
                // build 대신 jwt 에서는 compact 로 종료
        // builder 를 사용한 체이닝 기법 -> header 작성 끝내고 and 사용 -> payload 내용 부분으로 넘어가면서 jwtbuilder 가 열림
    }
    private String makeClaimByUserToString(JwtUser jwtUser){
        // 객체 자체를 jwt 에 담고 싶어서 객체를 직렬화
        // jwtUser 에 담았는 데이터를 JSON 형태의 문자열로 변환
        // jwt - payload : 사용자 정보 (signeduserid , roles) 를 json 문자열로 담음
        try {
            return objectMapper.writeValueAsString(jwtUser);
            // 객체 -> json 직렬화(writeValueAsString 사용) 문자열
            // json -> 객체 역직렬화 (readValue 사용)
        } catch (JsonProcessingException e) {
            // writeValueAsString 메서드는 내부적으로 i/o 작업을 수행하므로 JsonProcessingException 예외가 발생할 가능성 있음.
            // 객체를 json 문자열로 변환할 때 문제가 생길 경우 발생
            throw new RuntimeException(e);
            // 위에서 발생하는 문제를 런타임 예외로 변환하여 처리함
        }
    }
    public boolean validToken (String token){
        // jwt 복호화
        try {
            // 토큰에서 claims 객체를 추출 (여기서 token 이 signature 부분 담당)
            // 여기서 getclaims 의 signature 검증
            // 이상이 없으면 true 반납하면서 payload 호출
            getClaims(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    // spring - security - Authentication 사용
    public Authentication getAuthentication(String token){
        // getuser - token 을 파싱해서 사용자 정보를 가져옴
        UserDetails userDetails = getUserDetailsFromToken(token);
        // 권한 확인 및 인증 객체 생성 과정
        // Authentication 객체 - spring security 에서 사용자 인증 정보(userdetails)와 권한(role) 정보 담는 객체
        return userDetails == null
                ? null
                : new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        // 리턴 타입이 UsernamePasswordAuthenticationToken 인데 이거는 Authentication 의 자식이라서 담을 수 있음
        // UsernamePasswordAuthenticationToken - security 의 authen 구현체 : userdetails , 비밀번호 , 권한 목록

    }
    public UserDetails getUserDetailsFromToken(String token){
        Claims claims = getClaims(token);
        String json = (String)claims.get("signedUser"); // 직렬화
        JwtUser jwtUser = null;
        try {
            jwtUser = objectMapper.readValue(json , JwtUser.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // 객체화 과정
        // jwtuser 의 클래스 전부 컨버트
        // JwtUser 클래스에 작성한 멤버필드 - signeduserid , roles 필드에 값을 매핑
        // 그거를 문자열로 이루어진 jwtuser 에 객체로 변환
        // json 에 멤버필드 2가지 키가 있어야 한다.
        MyUserDetails userDetails = new MyUserDetails();
        userDetails.setJwtUser(jwtUser);
        // 위에 2가지 키 값을 myuserdetails 로 값 반환
        // details 에서 for 반복문으로 role 입력 - signeduserid 마다 List<roles> 만들어줌
        // 이게 json - header 부분에 적용
        // set 객체 변환하면서 jwtuser 에 매핑
        return userDetails;
    }
    public Claims getClaims(String token){
        // claims 는 jwt 의 payload 를 키 - 값 매핑으로 관리

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        // 1. jwt 파서 생성
        // 2. 서명 검증(secretKey 사용)
        // 3. 파서 빌드
        // 4. 토큰의 payload를 파싱하여 claims 반환
        // 5. payload 를 가져옴
    }
}
