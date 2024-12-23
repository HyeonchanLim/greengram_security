package com.green.greengram.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.swing.border.TitledBorder;

@Getter
@Setter
@ToString
public class UserSignInReq {

    @Size(min = 3 , max = 30 , message = "아이디는 3~30자 사이만 가능합니다.")
    @NotNull(message = "아이디를 입력하셔야 합니다.")
    @Schema(title = "아이디" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String uid;

    @Size(min = 4 , max = 50 , message = "비밀번호는 4~50자 사이만 가능합니다.")
    @NotNull(message = "비밀번호를 입력하셔야 합니다.")
    @Schema(title = "패스워드" , requiredMode = Schema.RequiredMode.REQUIRED)
    private String upw;


}
