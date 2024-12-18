package com.code.mandalart.common.model;

import com.code.mandalart.common.ConstantsPage;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MandalartPaging {
    private int size;
    private int page;
    private int startIndex;

    public MandalartPaging(Integer size, Integer page) {
        this.size = (size==null || size <=0) ? ConstantsPage.page_size() : size;
        this.page = page==null || page <= 0 ? 1 : page;
        this.startIndex = (this.size - 1) * this.size;
    }
}
