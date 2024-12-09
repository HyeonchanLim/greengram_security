package com.green.greengram.user;

import com.green.greengram.common.model.ResultResponse;
import com.green.greengram.user.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResultResponse<UserSignInRes> selUserForSignIn (@RequestBody UserSignInReq p) {
        UserSignInRes result = service.selUserForSignIn(p);
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
