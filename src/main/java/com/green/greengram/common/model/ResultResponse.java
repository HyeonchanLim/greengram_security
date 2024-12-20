package com.green.greengram.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder // 상속받은 객체에서도 builder 를 써야한다면 SuperBuilder 로 변경해야함
// 만약 나 혼자서 쓰는거라면 Builder 써도 상관 x
@Setter
public class ResultResponse<T> {
    private String resultMessage;
    private T resultData;
}
