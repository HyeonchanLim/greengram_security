package com.green.greengram.common.exception;

public interface ErrorCode {
    String name();
    String getMessage(); // 나를 상속받은 ENUM 은 String message 멤버필드를 꼭 가져야 한다.

}
