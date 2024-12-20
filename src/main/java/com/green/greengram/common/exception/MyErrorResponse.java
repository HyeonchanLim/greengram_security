package com.green.greengram.common.exception;

import com.green.greengram.common.model.ResultResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@SuperBuilder
public class MyErrorResponse extends ResultResponse<String> {
    // Validation 에러메시지 전달
    private final List<ValidationError> valids;
    // resultresponse 에서 valids 까지 추가해서 3가지로 빌더 빌드 생성 가능

    // Validation 에러가 발생 시 해당 에러의 메시지
    // 어떤 필드였고 , 에러 메세지를 묶어서 담을 객체를 만들 때 사용
    @Getter
    @Builder
    public static class ValidationError {
        private final String field;
        private final String message;


        // ValidationError 빌더 객체화 하면서 fieldError 을 담을꺼임
        // null 일 경우 아닐 경우 상황을 만들꺼임
        public static ValidationError of(final FieldError fieldError){
            return ValidationError.builder()
                    .field(fieldError.getDefaultMessage())
                    .build();
        }
    }
}
