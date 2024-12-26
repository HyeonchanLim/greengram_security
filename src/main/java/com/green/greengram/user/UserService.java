package com.green.greengram.user;

import com.green.greengram.common.CookieUtils;
import com.green.greengram.common.MyFileUtils;
import com.green.greengram.common.exception.CustomException;
import com.green.greengram.common.exception.UserErrorCode;
import com.green.greengram.config.jwt.JwtUser;
import com.green.greengram.config.jwt.TokenProvider;
import com.green.greengram.config.security.AuthenticationFacade;
import com.green.greengram.user.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final MyFileUtils myFileUtils;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;

    public int postSignUp (MultipartFile pic ,UserSignUpReq p){
        String savedPicName = (pic != null ? myFileUtils.makeRandomFileName(pic) : null);
//        String hashedPassword = BCrypt.hashpw(p.getUpw(),BCrypt.gensalt());
        String hashedPassword = passwordEncoder.encode(p.getUpw());
        p.setPic(savedPicName);
        p.setUpw(hashedPassword);

        int result = mapper.insUser(p);
        if (pic == null){
            return result;
        }
        long userId = p.getUserId();
        String middlePath = String.format("user/%d",userId);
        String filePath = String.format("%s/%s",middlePath,savedPicName);
        myFileUtils.makeFolders(middlePath);
        try {
            myFileUtils.transferTo(pic , filePath);
            // 임시파일에 있는 pic 을 filepath (저장하고 싶은 경로) 위치로
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public UserSignInRes selUserForSignIn (UserSignInReq p
                                            , HttpServletResponse response){
        UserSignInRes res = mapper.selUserByUid(p.getUid());

        // 보안 강화로 아이디 , 비밀번호 확인 따로 분류하지 않겟음
        // 오류가 터지면 전부 throw 일괄 처리
        if (res == null || !passwordEncoder.matches(p.getUpw(),res.getUpw())){
            throw new CustomException(UserErrorCode.INCORRECT_ID_PW);
        }

//        if (res == null){
//            res = new UserSignInRes();
//            res.setMessage("아이디를 확인해 주세요.");
//            return res;
//        } else if (!passwordEncoder.matches(p.getUpw() , res.getUpw())){ // 비밀번호가 다를 경우
//            res = new UserSignInRes();
//            res.setMessage("비밀번호를 확인해 주세요");
//            return res;
//        }

        // jwt 토큰 - 액세스토큰 , 리프레쉬 토큰 2개 생성
        // 액세스 (20분) , 리프레쉬 (15일) 유효기간
        JwtUser jwtUser = new JwtUser();
        jwtUser.setSignedUserId(res.getUserId());
        jwtUser.setRoles(new ArrayList<>(2));
        jwtUser.getRoles().add("ROLE_USER");
        jwtUser.getRoles().add("ROLE_ADMIN");
        System.out.println(jwtUser.getRoles());
        String accessToken = tokenProvider.generateToken(jwtUser , Duration.ofMinutes(30));
        String refreshToken = tokenProvider.generateToken(jwtUser , Duration.ofDays(15));

        // refreshToken 은 쿠키에 담아서 보낼꺼임
        int maxAge = 1_296_000; // 15일*24시간*60분*60초 = 초(second) 계산
        cookieUtils.setCookie(response,"refreshToken" , refreshToken , maxAge);

        res.setMessage("로그인 성공~");
        res.setAccessToken(accessToken);
        return res;
    }

    public UserInfoGetRes getUserInfo (UserInfoGetReq p) {
        p.setProfileUserId(authenticationFacade.getSignedUserId());
        return mapper.selUserInfo2(p);
    }

    public String getAccessToken(HttpServletRequest req) {
        Cookie cookie = cookieUtils.getCookie(req, "refreshToken");
        String refreshToken = cookie.getValue();
        log.info("refreshToken: {}", refreshToken);

        JwtUser jwtUser = tokenProvider.getJwtUserFromToken(refreshToken);
        String accessToken = tokenProvider.generateToken(jwtUser, Duration.ofSeconds(30));
        return accessToken;
    }

    public String patchUserPic(UserPicPatchReq p){
        p.setSignedUserId(authenticationFacade.getSignedUserId());
        // 1. 저장할 파일명 생성 -> 확장자는 오리지날 파일명과 일치하게 한다. uuid + getExt 사용
        String savedPicName = (p.getPic() != null ? myFileUtils.makeRandomFileName(p.getPic()) : null);
        // 2. 기존 파일 삭제 (1. 폴더를 지운다 2.select 해서 기존 파일명을 얻어온다. 3.기존 파일명을 FrontEnd 에서 받는다.)
        String folderpath = String.format("user/%d", p.getSignedUserId());
        myFileUtils.makeFolders(folderpath);

        //File currentPic = new File(); // pk값 통해서 경로 가져오기
        // 3. 원하는 위치에 저장할 파일명으로 파일을 이동한다 transferTo 사용
        String deletePath = String.format("%s/user/%d" ,myFileUtils.getUploadPath() ,  p.getSignedUserId());
        myFileUtils.deleteFolder(deletePath,false);


        // 4. db 에 튜플을 수정(update)한다.
        p.setPicName(savedPicName);
        int result = mapper.updUserPic(p); // 튜플값

        if (p.getPic() == null) {
            return null;
        }

        String filePath = String.format("user/%d/%s" , p.getSignedUserId(),savedPicName);
        try {
            myFileUtils.transferTo(p.getPic(),filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedPicName;
    }
}
