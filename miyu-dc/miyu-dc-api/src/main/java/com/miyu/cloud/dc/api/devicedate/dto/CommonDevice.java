package com.miyu.cloud.dc.api.devicedate.dto;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

import java.util.Map;

@Data
@JSONType(orders={"msg", "code","data"})
public class CommonDevice {

    /**
     * 错误码
     *
     * @see ErrorCode#getCode()
     */
    private Integer code;

    /**
     * 错误提示，用户可阅读
     *
     * @see ErrorCode#getMsg() ()
     */
    private String msg;

    /**
     * 返回数据
     */
    private Map<String,Object> data;
}
