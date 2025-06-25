package com.miyu.module.qms.enums;

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
    public static final String NAME = "qms-server";

    public static final String PREFIX = RpcConstants.RPC_API_PREFIX + "/qms";

    public static final String VERSION = "1.0.0";

    /***
     * 不合格品登记审批
     */
    public static final String PROCESS_KEY = "qms-unqualified-audit";

    /** 质量管理体系管理资料库对应流程定义 KEY*/
    public static final String DATABASE1_AUDIT_PROCESS_KEY = "qms-management-database-1-audit";

    /** 质量目标预计划管理资料库对应流程定义 KEY*/
    public static final String DATABASE2_AUDIT_PROCESS_KEY = "qms-management-database-2-audit";

    /** 产品质量设计管理资料库对应流程定义 KEY*/
    public static final String DATABASE3_AUDIT_PROCESS_KEY = "qms-management-database-3-audit";

    /** 质量控制管理资料库对应流程定义 KEY*/
    public static final String DATABASE4_AUDIT_PROCESS_KEY = "qms-management-database-4-audit";

    /** 质量改进管理资料库对应流程定义 KEY*/
    public static final String DATABASE5_AUDIT_PROCESS_KEY = "qms-management-database-5-audit";

    /** 质量成本控制管理资料库对应流程定义 KEY*/
    public static final String DATABASE6_AUDIT_PROCESS_KEY = "qms-management-database-6-audit";

    /** 供应商管理资料库对应流程定义 KEY*/
    public static final String DATABASE7_AUDIT_PROCESS_KEY = "qms-management-database-7-audit";

}
