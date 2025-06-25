package com.miyu.module.ppm.enums;

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
    public static final String NAME = "ppm-server";

    public static final String PREFIX = RpcConstants.RPC_API_PREFIX + "/ppm";

    public static final String VERSION = "1.0.0";

    /** 采购合同对应流程定义 KEY*/
    public static final String CONTRACT_AUDIT_PROCESS_KEY = "pm-contract-audit";

    /** 采购付款对应流程定义 KEY*/
    public static final String CONTRACT_PAYMENT_AUDIT_PROCESS_KEY = "pm-contract-payment-audit";

    /** 采购发票对应流程定义 KEY*/
    public static final String CONTRACT_INVOICE_AUDIT_PROCESS_KEY = "pm-contract-invoice-audit";

    /** 采购收货对应流程定义 KEY*/
    public static final String CONSIGNMENT_PROCESS_KEY = "ppm-purchase-audit";


    /** 采购退货对应流程定义 KEY*/
    public static final String PM_RETURN_AUDIT_KEY = "pm-return-audit";

    /** 采购供应商对应流程定义 KEY*/
    public static final String COMPANY_AUDIT_PROCESS_KEY = "pm-company-audit";

    /** 外协供应商对应流程定义 KEY*/
    public static final String COMPANY_COORD_AUDIT_PROCESS_KEY = "pm-company-database-audit";

    /** 采购合同退款审批流程 KEY */
    public static final String PM_REFUND_PROCESS_KEY = "pm-refund-audit";

    /** 采购申请审批流程 KEY */
    public static final String PURCHASE_REQUIREMENT_PROCESS_KEY = "pm-requirement-audit";

    /** 外协发货审批流程 KEY */
    public static final String CONTRACT_CONSIGNMENT_PROCESS_KEY = "pm-contract-consignment-audit";


    /** 外协退货审批流程 KEY */

    public static final String CONTRACT_CONSIGNMENT_RETURN_PROCESS_KEY = "pm-contract-consignment-return-audit";

    /***
     * 默认出库仓库
     */
    public static final String OUT_WARHOUSE = "1796370351509213185";
    /***
     * 入库签收位
     */
    public static final String IN_WARHOUSE = "1805175807161786369";



    /***
     * 销售审批流程key
     */
    public static final String PROCESS_KEY = "dm-receivable-audit";

    /***
     * 销售退货审批流程key
     */
    public static final String RETURN_PROCESS_KEY = "dm-return-audit";
    /***
     * 销售合同退款审批流程key
     */
    public static final String REFUND_PROCESS_KEY = "dm-refund-audit";


    /***
     * 销售合同流程key
     */
    public static final String DM_CONTRACT_PROCESS_KEY = "dm-contract-audit";

    /***
     * 销售合同付款流程key
     */
    public static final String DM_CONTRACT_PAYMENT_PROCESS_KEY = "dm-contract-payment-audit";


    /** 销售供应商对应流程定义 KEY*/
    public static final String DM_COMPANY_AUDIT_PROCESS_KEY = "dm-company-audit";


    /** 销售发票对应流程定义 KEY*/
    public static final String DM_CONTRACT_INVOICE_AUDIT_PROCESS_KEY = "dm-contract-invoice-audit";


    /** 委托加工入库对应流程定义 KEY*/
    public static final String SHIPPING_INSTORAGE_PROCESS_KEY = "dm-instorage-audit";


    /** 委托加工退货对应流程定义 KEY*/
    public static final String SHIPPING_INSTORAGE_RETURN_PROCESS_KEY = "dm-instorage-return-audit";


}
