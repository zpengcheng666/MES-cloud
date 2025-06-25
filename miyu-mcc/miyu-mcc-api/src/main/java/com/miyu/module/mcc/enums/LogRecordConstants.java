package com.miyu.module.mcc.enums;

/**
 * 销售发货 操作日志枚举
 * 目的：统一管理，也减少 Service 里各种“复杂”字符串
 *
 * @author HUIHUI
 */
public interface LogRecordConstants {

    // ======================= CRM_CLUE 线索 =======================

    String SHIPPING_CLUE_TYPE = "销售发货申请";


    String SHIPPING_SUBMIT_SUB_TYPE = "提交销售发货审批";


    String SHIPPING_SUBMIT_SUCCESS = "提交发货单【{{#shippingNo}}】审批成功";





    String SHIPPING_RETURN_CLUE_TYPE = "销售退货申请";


    String SHIPPING_RETURN_SUBMIT_SUB_TYPE = "提交销售退货审批";


    String SHIPPING_RETURN_SUBMIT_SUCCESS = "提交退货单【{{#shippingReturnDO}}】审批成功";




    String SHIPPING_REFUND_CLUE_TYPE = "销售退款申请";


    String SHIPPING_REFUND_SUBMIT_SUB_TYPE = "提交销售退款审批";


    String SHIPPING_REFUND_SUBMIT_SUCCESS = "提交退款单【{{#no}}】审批成功";

}
