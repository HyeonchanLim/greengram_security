package com.green.greengram.user;

import com.green.greengram.common.model.ResultResponse;
import com.green.greengram.user.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
@Tag(name = "1. 회원", description = "sign-in / sign-out")
public class UserController {
    private final UserService service;

    @PostMapping("sign-up")
    public ResultResponse<Integer> postSignUp (@RequestPart (required = false) MultipartFile pic
                            , @RequestPart UserSignUpReq p){
        int result = service.postSignUp(pic , p);
        return ResultResponse.<Integer>builder()
                .resultMessage("회원가입완료")
                .resultData(result)
                .build();
    }
    @PostMapping("sign-in")
    // access-token request 실행 후 response 응답해주는 과정 - 여기서 로그인 절차 밟음
    public ResultResponse<UserSignInRes> selUserForSignIn (@RequestBody UserSignInReq p , HttpServletResponse response) {
        UserSignInRes result = service.selUserForSignIn(p , response);
        return ResultResponse.<UserSignInRes>builder()
                .resultMessage(result.getMessage())
                .resultData(result)
                .build();
    }
    @GetMapping
    @Operation(summary = "유저 profile 정보 ")
    public ResultResponse<UserInfoGetRes> getUserInfo (@ParameterObject @ModelAttribute UserInfoGetReq p){
        // log.info("UserController > getUserInfo{}" , p);
        UserInfoGetRes res = service.getUserInfo(p);
        return ResultResponse.<UserInfoGetRes>builder()
                .resultMessage("유저 프로필 정보")
                .resultData(res)
                .build();
    }
    @GetMapping("access-token")
    @Operation(summary = "Access Token 재발행")
    //request -> tomcat 실행 -> filter -> dispatcherservlet -> mapping -> controller
    public ResultResponse<String> getAccessToken(HttpServletRequest req){
        String accessToken = service.getAccessToken(req);

        return ResultResponse.<String>builder()
                .resultMessage("Access Token 재발행")
                .resultData(accessToken)
                .build();
    }

    @PatchMapping("pic")
    public ResultResponse<String> patchProfilePic(@ModelAttribute UserPicPatchReq p){
        log.info("UserController > patchProfilePic > p : {}" , p);
        String pic = service.patchUserPic(p);
        return ResultResponse.<String>builder()
                .resultMessage("프로필 사진 수정 완료")
                .resultData(pic)
                .build();
    }
}
