package com.code.mandalart;

import com.code.mandalart.model.MandalartGetReq;
import com.code.mandalart.model.MandalartGetRes;
import com.code.mandalart.model.MandalartPostReq;
import com.code.mandalart.model.MandalartPostRes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MandalartMapper {
    MandalartGetRes getMandalart (MandalartGetReq p);
    MandalartPostRes postMandalart (MandalartPostReq p);

}
