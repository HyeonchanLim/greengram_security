package com.green.greengram.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice // AOP (Aspect Orientation Programming , 관점 지향 프로그래밍)
// 시작과 끝을 알려줌 ex - 시작 메세지 , 종료 메시지 등등 일정 부분이 겹치면 어노테이션으로 묶어줌
// 그다음 어노테이션 사용하면 해당 구현분들은 시작 - 종료 과정에서 메세지를 뽑아줌
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //(추가메소드) 우리가 커스텀한 예외가 발생되었을 경우 캐치
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleException(CustomException e){
        return null;
    }
    //Validation 예외가 발생되었을 경우 캐치
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception
                                                                                    , HttpHeaders headers
                                                                                    , HttpStatusCode statusCode
                                                                                    , WebRequest request) {
        return null;
    }




    private List<MyErrorResponse.ValidationError> getValidationError(BindException e){
        //List<FieldError> fieldErrors = e.getFieldErrors();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<MyErrorResponse.ValidationError> errors = new ArrayList<>(fieldErrors.size());
        for (FieldError fieldError : fieldErrors){
            errors.add(MyErrorResponse.ValidationError.of(fieldError));
        }
        return errors;
    }





}
