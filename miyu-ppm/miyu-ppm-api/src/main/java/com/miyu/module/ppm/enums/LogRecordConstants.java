package com.miyu.module.ppm.enums;

/**
 * 采购收货 操作日志枚举
 * 目的：统一管理，也减少 Service 里各种“复杂”字符串
 *
 * @author HUIHUI
 */
public interface LogRecordConstants {

    String PurchaseConsignment_CLUE_TYPE = "采购申请";


    String PurchaseConsignment_SUBMIT_SUB_TYPE = "提交采购审批";


    String PurchaseConsignment_SUBMIT_SUCCESS = "提交采购单【{{#shippingNo}}】审批成功";


    String SHIPPING_CLUE_TYPE = "销售发货申请";


    String SHIPPING_SUBMIT_SUB_TYPE = "提交销售发货审批";


    String SHIPPING_SUBMIT_SUCCESS = "提交发货单【{{#shippingNo}}】审批成功";


    String SHIPPING_RETURN_CLUE_TYPE = "销售退货申请";


    String SHIPPING_RETURN_SUBMIT_SUB_TYPE = "提交销售退货审批";


    String SHIPPING_RETURN_SUBMIT_SUCCESS = "提交退货单【{{#shippingReturnDO}}】审批成功";


    String SHIPPING_REFUND_CLUE_TYPE = "销售退款申请";


    String SHIPPING_REFUND_SUBMIT_SUB_TYPE = "提交销售退款审批";


    String SHIPPING_REFUND_SUBMIT_SUCCESS = "提交退款单【{{#no}}】审批成功";


    String COMPANY_TYPE = "公司COMPANY";

    String COMPANY_UPDATE_TYPE = "更新公司信息";


    String COMPANY_UPDATE_SUCCESS = "更新了公司【{{#company.name}}】: {_DIFF{#updateReqVO}}";


    String COMPANY_CONTACT_TYPE = "公司联系人COMPANYCONTACT";

    String COMPANY_CONTACT_UPDATE_TYPE = "更新公司联系人信息";


    String COMPANY_CONTACT_UPDATE_SUCCESS = "更新了公司联系人【{{#companyContact.companyName}}】: {_DIFF{#updateReqVO}}";

    String COMPANY_FINANCE_TYPE = "公司账户COMPANYFINANCE";

    String COMPANY_FINANCE_UPDATE_TYPE = "更新公司账户信息";

    String COMPANY_FINANCE_UPDATE_SUCCESS = "更新了公司账户【{{#companyFinance.companyName}}】: {_DIFF{#updateReqVO}}";

    String COMPANY_QUANTITY_TYPE = "公司质量认证";

    String COMPANY_QUANTITY_UPDATE_TYPE = "更新公司质量认证信息";

    String COMPANY_QUANTITY_UPDATE_SUCCESS = "更新了质量认证信息【{{#companyQualityControl.companyName}}】: {_DIFF{#updateReqVO}}";



    String CONTRACT_TYPE = "合同COMPANY";

    String CONTRACT_UPDATE_TYPE = "更新合同信息";


    String CONTRACT_UPDATE_SUCCESS = "更新了合同信息【{{#contract.name}}】: {_DIFF{#updateReqVO}}";

}
