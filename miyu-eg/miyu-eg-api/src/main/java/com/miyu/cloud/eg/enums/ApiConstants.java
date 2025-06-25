package com.miyu.cloud.eg.enums;

import cn.iocoder.yudao.framework.common.enums.RpcConstants;

public class ApiConstants {

    /**
     * 服务名
     *
     * 注意，需要保证和 spring.application.name 保持一致
     */
    public static final String NAME = "eg-server";

    public static final String PREFIX = RpcConstants.RPC_API_PREFIX + "/eg";

    public static final String VERSION = "1.0.0";
}
