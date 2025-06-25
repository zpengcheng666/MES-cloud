package cn.iocoder.yudao.module.pms.enums;

import cn.iocoder.yudao.framework.common.enums.RpcConstants;

/**
 * API 相关的枚举
 *
 * @author 芋道源码
 */
public class ApiConstants {

    /**
     * 服务名
     *
     * 注意，需要保证和 spring.application.name 保持一致
     */
    public static final String NAME = "pms-server";

    public static final String PREFIX = RpcConstants.RPC_API_PREFIX + "/pms";

    public static final String VERSION = "1.0.0";

    /**
     * 对应的流程定义 KEY
     */
    public static final String Project_PROCESS_KEY = "pms_approval";
    public static final String Assessment_PROCESS_KEY = "pms_assessment";

}
