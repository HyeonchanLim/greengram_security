package com.code.mandalart.common;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class ConstantsPage {
    private static int page_size;
    @Value("${const.default-page-size}")
    public void setDefault_page_size(int value) {
        page_size = value;
    }
    public static int page_size() {
        return page_size;
    }
}
