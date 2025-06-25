package com.miyu.module.pdm.enums;

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
    public static final String NAME = "pdm-server";

    public static final String PREFIX = RpcConstants.RPC_API_PREFIX + "/pdm";

    public static final String VERSION = "1.0.0";

    public static final String FEASIBILITY_PROCESS_KEY = "pdm-feasibility-audit"; // 技术评估审批KEY

    public static final String PROCESS_PLAN_PROCESS_KEY = "pdm-process-plan-audit"; // 工艺方案审批KEY

    public static final String PROCESS_PLAN_DETAIL_PROCESS_KEY = "pdm-process-plan-detail-audit"; // 工艺规程审批KEY

    public static final String TOOLING_APPLY_KEY = "pdm-tooling-apply-audit"; // 工装申请审批KEY

    public static final String TOOLING_DETAIL_KEY = "pdm-tooling-detail-audit"; // 工装详细设计审批KEY

    public static final String PROCESS_CHANGE_KEY = "pdm-process-change-audit"; // 工艺更改单审批KEY

    public static final String PROCESS_SUPPLEMENT_PROCESS_KEY = "pdm-process-supplement-audit"; // 补加工工艺规程审批KEY
}
