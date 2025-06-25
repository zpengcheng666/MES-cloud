//package com.miyu.module.ppm.enums;
//import cn.iocoder.yudao.framework.common.exception.ErrorCode;
//
///**
// * 错误码枚举类
// * 采购 系统，使用 2-002-000-000 段
// *
// * @author Zhangyunfei
// *         Created on 2024/05/22
// *             错误码枚举类
// *             1. 仓库表不存在
// *             2. 库位表不存在
// *             3. 货位表不存在
// *             4. 库存表不存在
// *             5. 出入库单表不存在
// *             6. 出入库单明细表不存在
// *             7. 库存盘点单表不存在
// *             8. 库存盘点单明细表不存在
// *             9. 库存调整单表不存在
// *             10. 库存调整单明细表不存在
// *             11. 库存盘点任务表不存在
// *             12. 库存调整任务表不存在
// *             13. 库存盘点任务明细表不存在
// *             14. 库存调整任务明细表不存在
// */
//public interface ErrorCodeConstants {
//    ErrorCode COMPANY_NOT_EXISTS = new ErrorCode(2_002_000_001, "企业基本信息不存在");
//}

package com.miyu.module.ppm.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * CRM 错误码枚举类
 * <p>
 * crm 系统，使用 1-030-000-000 段
 */
public interface ErrorCodeConstants {

    ErrorCode COMPANY_NOT_EXISTS = new ErrorCode(1_030_000_000, "企业基本信息不存在");

    ErrorCode COMPANY_CONTACT_NOT_EXISTS = new ErrorCode(1_030_000_001, "企业联系人不存在");

    ErrorCode COMPANY_FINANCE_NOT_EXISTS = new ErrorCode(1_030_000_002, "企业税务信息不存在");

    ErrorCode COMPANY_PRODUCT_NOT_EXISTS = new ErrorCode(1_030_000_003, "企业产品不存在");

    ErrorCode COMPANY_QUALITY_CONTROL_NOT_EXISTS = new ErrorCode(1_030_000_004, "企业质量控制信息不存在");

    ErrorCode MATERIAL_NOT_EXISTS = new ErrorCode(1_030_000_005, "物料基本信息不存在");

    ErrorCode CONTRACT_NOT_EXISTS = new ErrorCode(1_030_000_006, "购销合同不存在");

    ErrorCode CONTRACT_NO_EXISTS = new ErrorCode(1_030_000_007, "合同编号重复，请重试");

    ErrorCode CONTRACT_UPDATE_FAIL_NOT_DRAFT = new ErrorCode(1_030_000_008, "合同{}失败，原因：合同不是草稿状态");

    ErrorCode COMPANY_PRODUCT_FAIL_DELETE_WITH_CONTRACT = new ErrorCode(1_030_000_009, "企业产品删除失败，原因：存在关联的采购订单");

    ErrorCode COMPANY_UPDATE_FAIL_NOT_DRAFT = new ErrorCode(1_030_000_010, "企业{}失败，原因：公司不是草稿状态");

    ErrorCode COMPANY_FAIL_DELETE_WITH_CONTRACT = new ErrorCode(1_030_000_011, "企业删除失败，原因：存在关联的采购订单");

    ErrorCode COMPANY_FAIL_DELETE_WITH_CONTACT = new ErrorCode(1_030_000_011, "企业删除失败，原因：存在关联的联系人");

    ErrorCode CONTRACT_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_030_000_012, "合同提交审核失败，原因：合同不是未提交状态");

    ErrorCode CONTRACT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS = new ErrorCode(1_030_000_013, "更新合同审核状态失败，原因：合同不是审核中状态");

    ErrorCode MATERIAL_CREATE_FAIL = new ErrorCode(1_030_000_014, "企业产品创建失败，原因：物料ID为空");

    ErrorCode CONTRACT_STATUS_UPDATE_FAIL = new ErrorCode(1_030_000_015, "合同更新失败，原因：{}");

    ErrorCode PURCHASE_CONSIGNMENT_NOT_EXISTS = new ErrorCode(1_030_000_012, "采购收货不存在");

    ErrorCode PURCHASE_CONSIGNMENT_DETAIL_NOT_EXISTS = new ErrorCode(1_030_000_013, "收货明细不存在");

    ErrorCode CONTRACT_PAYMENT_SCHEME_NOT_EXISTS = new ErrorCode(1_030_000_016, "合同付款计划不存在");

    ErrorCode CONTRACT_PAYMENT_SCHEME_AMOUNT_ERROR = new ErrorCode(1_030_000_017, "合同付款计划金额与合同金额不相等");

    ErrorCode CONTRACT_PAYMENT_NOT_EXISTS = new ErrorCode(1_030_000_018, "合同付款不存在");

    ErrorCode CONTRACT_PAYMENT_SCHEME_ERROR = new ErrorCode(1_030_000_019, "合同付款计划不存在");

    ErrorCode CONTRACT_PAYMENT_AMOUNT_ERROR = new ErrorCode(1_030_000_020, "付款计划{}实际付款金额大于剩余计划付款金额");

    ErrorCode PAYMENT_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_030_000_021, "支付提交审核失败，原因：支付不是未提交状态");

    ErrorCode PAYMENT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS = new ErrorCode(1_030_000_022, "更新付款审核状态失败，原因：付款不是审核中状态");

    ErrorCode PAYMENT_DETAIL_NOT_EXISTS = new ErrorCode(1_030_000_023, "合同付款详细不存在");

    ErrorCode CONTRACT_CONSIGNMENT_SIGNEDAMOUNT_ERROR = new ErrorCode(1_030_000_021, "请在采购数量与收货数量一致后确认签收");

    ErrorCode CONTRACT_INVOICE_NOT_EXISTS = new ErrorCode(1_030_000_024, "购销合同发票不存在");

    ErrorCode CONTRACT_INVOICE_DETAIL_NOT_EXISTS = new ErrorCode(1_030_000_025, "购销合同发票表详细不存在");

    ErrorCode CONTRACT_INVOICE_DETAIL_AMOUNT_ERROR = new ErrorCode(1_030_000_026, "付款单据{}实际开具金额大于剩余付款金额");

    ErrorCode CONTRACT_INVOICE_AMOUNT_ERROR = new ErrorCode(1_030_000_027, "实际开具金额不等于实际开具金额明细总和");

    ErrorCode CONTRACT_INVOICE_SUM_AMOUNT_ERROR = new ErrorCode(1_030_000_028, "实际开具金额总和大于合同总金额");

    ErrorCode CONTRACT_INVOICE_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_030_000_029, "发票提交审核失败，原因：发票不是未提交状态");

    ErrorCode CONTRACT_INVOICE_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS = new ErrorCode(1_030_000_030, "更新合同发票审核状态失败，原因：合同发票不是审核中状态");

    ErrorCode COMPANY_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_030_000_031, "支付提交审核失败，原因：供应商不是未提交状态");

    ErrorCode COMPANY_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS = new ErrorCode(1_030_000_032, "更新供应商审核状态失败，原因：供应商不是审核中状态");

    // ========== 参数配置 1-010-000-000 ==========
    ErrorCode CONSIGNMENT_NOT_EXISTS = new ErrorCode(1_010_000_001, "销售发货不存在");

    ErrorCode CONSIGNMENT_DETAIL_NOT_EXISTS = new ErrorCode(1_010_000_002, "采购明细不存在");

    ErrorCode CONSIGNMENT_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_010_000_003, "采购提交审核失败，原因：采购单没处在未提交状态");

    ErrorCode CONSIGNMENT_ORDER_EXCEED = new ErrorCode(1_010_000_004, "采购单数量超出合同订单数量");

    ErrorCode CONSIGNMENT_ORDER_NOT_EXISTS = new ErrorCode(1_010_000_005, "采购单对应订单不存在");

    ErrorCode CONSIGNMENT_RETURN_NOT_EXISTS = new ErrorCode(1_010_000_006, "采购退货单不存在");

    ErrorCode CONSIGNMENT_RETURN_DETAIL_NOT_EXISTS = new ErrorCode(1_010_000_007, "采购退货单详情不存在");

    ErrorCode CONSIGNMENT_RETURN_DETAIL_NOT_EXCEEES = new ErrorCode(1_010_000_008, "退货数量不能大于签收数量");

    ErrorCode CONSIGNMENT_RETURN_DETAIL_QUANTITY = new ErrorCode(1_010_000_009, "退货数量不能大于库存数量");

    ErrorCode CONSIGNMENT_RETURN__SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_010_001_003, "退货提交审核失败，原因：发货单没处在未提交状态");

    ErrorCode CONSIGNMENT_REFUND_NOT_EXISTS = new ErrorCode(1_010_000_010, "采购退款单不存在");

    ErrorCode CONSIGNMENT_RETURN_DETAIL_DETAIL = new ErrorCode(1_030_000_033, "{}已存在退货单");

    ErrorCode CONSIGNMENT_REFUND_NOT_PRICE = new ErrorCode(1_030_000_010, "退款金额超出剩余可退款金额");

    ErrorCode CONSIGENMENT_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_010_000_003, "采购退款提交审核失败，原因：退款单没处在未提交状态");

    ErrorCode PURCHASE_REQUIREMENT_NOT_EXISTS = new ErrorCode(1_040_000_001, "采购申请不存在");

    ErrorCode PURCHASE_REQUIREMENT_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_040_000_002, "采购申请提交审核失败，原因：采购申请不是未提交状态");

    ErrorCode PURCHASE_REQUIREMENT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS = new ErrorCode(1_030_000_022, "更新采购申请审核状态失败，原因：采购申请不是审核中状态");

    ErrorCode PURCHASE_REQUIREMENT_DETAIL_QUANTITY_ERROR = new ErrorCode(1_030_000_023, "合同订单数量大于关联采购申请({})的数量");

    ErrorCode PURCHASE_REQUIREMENT_DETAIL_MATERIAL_DUPLICATE = new ErrorCode(1_030_000_024, "采购申请详细物料不能重复");
    ErrorCode PURCHASE_CONTRACT_ERROR = new ErrorCode(1_030_000_025, "查询不到外协的物料");



    ErrorCode PURCHASE_CONSIGNMENT_INBOUND_ERROR= new ErrorCode(1_030_000_025, "调用WMS采购入库失败");
    ErrorCode PURCHASE_CONSIGNMENT_SHEET_ERROR= new ErrorCode(1_030_000_027, "调用QMS质检单失败");
    ErrorCode PURCHASE_CONSIGNMENT_OUTBOUND_ERROR= new ErrorCode(1_030_000_026, "调用WMS采购退货出库失败");

    ErrorCode SHIPPING_OUTBOUND_ERROR = new ErrorCode(1_010_000_007, "WMS创建出库单失败");
    ErrorCode SHIPPING_OUTBOUND_CANCEL_ERROR = new ErrorCode(1_010_000_007, "WMS作废出库单失败");
    ErrorCode PMS_OUTBOUND_CANCEL_ERROR = new ErrorCode(1_010_000_007, "pms作废条码失败");
    ErrorCode INSPECTION_SCHEME_ERROR = new ErrorCode(1_010_000_008, "找不到质检单");
    ErrorCode SHIPPING_OUTBOUND_SUBMIT_ERROR = new ErrorCode(1_010_000_009, "WMS出库删库存失败");

    ErrorCode SHIPPING_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_010_000_003, "发货提交审核失败，原因：发货单没处在未提交状态");

    ErrorCode CONTRACT_CONSIGNMENT_NOT_EXISTS = new ErrorCode(1_031_000_001, "外协发货不存在");

    ErrorCode CONTRACT_CONSIGNMENT_DETAIL_NOT_EXISTS = new ErrorCode(1_031_000_002, "外协发货单详情不存在");

    ErrorCode CONTRACT_OUT_RELATION_ERROR = new ErrorCode(1_031_000_003, "调用PMS更新外协出库码失败");
    ErrorCode CONTRACT_CONSIGNMENT_RELATION_ERROR = new ErrorCode(1_031_000_004, "调用WMS作废出库单失败");



    // ========== 发货单参数配置 1_010_000-000 ==========
    ErrorCode SHIPPING_NOT_EXISTS = new ErrorCode(1_010_000_001, "销售发货不存在");
    ErrorCode SHIPPING_DETAIL_NOT_EXISTS = new ErrorCode(1_010_000_002, "销售发货明细不存在");

    ErrorCode SHIPPING_ORDER_EXCEED = new ErrorCode(1_010_000_004, "发货单数量超出合同订单数量");

    ErrorCode SHIPPING_ORDER_ERROR = new ErrorCode(1_010_000_005, "该产品已经被其他发货单占用");

    ErrorCode SHIPPING_ORDER_NOT_EXISTS = new ErrorCode(1_010_000_006, "发货单对应订单不存在");



    //退货  ========== 发货单参数配置 1_010_001-000 ==========
    ErrorCode SHIPPING_RETURN_NOT_EXISTS = new ErrorCode(1_010_001_001, "销售退货单不存在");

    ErrorCode SHIPPING_RETURN_DETAIL_NOT_EXISTS = new ErrorCode(1_010_001_002, "销售退货单详情不存在");


    ErrorCode SHIPPING_RETURN__SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_010_001_003, "退货提交审核失败，原因：发货单没处在未提交状态");
    ErrorCode SHIPPING_RETURN_DETAIL_NUMBER_ERROR= new ErrorCode(1_010_001_004, "退货单数量超出发货单数量");
    ErrorCode SHIPPING_RETURN_IN_ERROR= new ErrorCode(1_010_001_004, "退货入库失败");





    //退款  ========== 参数配置 1_010_006-000 ==========
    ErrorCode CONTRACT_REFUND_NOT_EXISTS = new ErrorCode(1_010_002_001, "合同退款不存在");
    ErrorCode CONTRACT_REFUND_OUT = new ErrorCode(1_010_002_002, "退款金额超过付款金额");
    ErrorCode CONTRACT_REFUND_NOT_NULL = new ErrorCode(1_010_002_003, "退款金额不能为0");



    ErrorCode SHIPPING_INSTORAGE_NOT_EXISTS = new ErrorCode(1_010_003_001, "销售订单入库不存在");
    ErrorCode SHIPPING_PMS_NOT_SUCESS = new ErrorCode(1_010_003_002, "调用PMS更新条码失败");
    ErrorCode SHIPPING_INSTORAGE_DETAIL_NOT_EXISTS = new ErrorCode(1_010_003_003, "销售订单入库明细不存在");
    ErrorCode SHIPPING_INSTORAGE_NUMBER_OUT_LIMIT = new ErrorCode(1_010_003_004, "选择数量超过订单限制");





    ErrorCode SHIPPING_INFO_NOT_EXISTS = new ErrorCode(1_010_004_001, "销售发货产品不存在");


    ErrorCode CONSIGNMENT_INFO_NOT_EXISTS = new ErrorCode(1_010_005_001, "收货产品不存在");
    ErrorCode CONSIGNMENT_SIGN_ERROR = new ErrorCode(1_010_005_002, "签收失败--调用WMS生码失败");
    ErrorCode CONSIGNMENT_SIGN_MATERIAL_ERROR = new ErrorCode(1_010_005_003, "签收失败--请选择条码");



    ErrorCode INBOUND_EXCEPTION_HANDLING_NOT_EXISTS = new ErrorCode(1_010_006_001, "入库异常处理不存在");
}
