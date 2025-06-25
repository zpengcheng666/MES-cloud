package com.miyu.cloud.dc.enums;

import cn.iocoder.yudao.framework.common.enums.RpcConstants;


/**
 * API 相关的枚举
 *
 * @author yuhao
 */
public class ApiConstants {

    /**
     * 服务名
     * <p>
     * 注意，需要保证和 spring.application.name 保持一致
     */
    public static final String NAME = "dc-server";

    public static final String PREFIX = RpcConstants.RPC_API_PREFIX + "/dc";

    public static final String VERSION = "1.0.0";

}

