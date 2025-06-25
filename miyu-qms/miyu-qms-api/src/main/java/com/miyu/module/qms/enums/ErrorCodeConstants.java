package com.miyu.module.qms.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * 质量分析 错误码枚举类
 * <p>
 * 质量分析 系统，使用 1-012-000-000 段
 */
public interface ErrorCodeConstants {

    ErrorCode INSPECTION_ITEM_TYPE_NOT_EXISTS = new ErrorCode(1 - 012 - 000 - 001, "检测项目分类不存在");
    ErrorCode INSPECTION_ITEM_TYPE_EXITS_CHILDREN = new ErrorCode(1 - 012 - 000 - 002, "存在存在子检测项目分类，无法删除");
    ErrorCode INSPECTION_ITEM_TYPE_PARENT_NOT_EXITS = new ErrorCode(1 - 012 - 000 - 003, "父级检测项目分类不存在");
    ErrorCode INSPECTION_ITEM_TYPE_PARENT_ERROR = new ErrorCode(1 - 012 - 000 - 004, "不能设置自己为父检测项目分类");
    ErrorCode INSPECTION_ITEM_TYPE_ITEM_TYPE_NAME_DUPLICATE = new ErrorCode(1 - 012 - 000 - 005, "已经存在该检测项目分类名称的检测项目分类");
    ErrorCode INSPECTION_ITEM_TYPE_PARENT_IS_CHILD = new ErrorCode(1 - 012 - 000 - 006, "不能设置自己的子InspectionItemType为父InspectionItemType");

    ErrorCode INSPECTION_ITEM_TYPE_EXITS_ITEM = new ErrorCode(1 - 012 - 000 - 007, "该分类存在关联的检测项目");

    ErrorCode INSPECTION_ITEM_NOT_EXISTS = new ErrorCode(1 - 012 - 001 - 001, "检测项目不存在");


    ErrorCode INSPECTION_ITEM_DETAIL_NOT_EXISTS = new ErrorCode(1 - 012 - 002 - 001, "检测项目详情不存在");


    ErrorCode INSPECTION_ITEM_CONFIG_NOT_EXISTS = new ErrorCode(1 - 012 - 003 - 001, "检测项配置表（检测内容名称）不存在");

    ErrorCode INSPECTION_SCHEME_NOT_EXISTS = new ErrorCode(1 - 012 - 004 - 001, "检验方案不存在");


    ErrorCode INSPECTION_SCHEME_ITEM_NOT_EXISTS = new ErrorCode(1 - 012 - 004 - 002, "检验方案检测项目详情不存在");

    ErrorCode INSPECTION_SCHEME_ITEM_DETAIL_NOT_EXISTS = new ErrorCode(1 - 012 - 004 - 003, "请配置方案检测内容标准");

    ErrorCode INSPECTION_TOOL_NOT_EXISTS = new ErrorCode(1-012-004-001, "检测工具不存在");
    ErrorCode INSPECTION_TOOL_VERIFICATION_RECORD_NOT_EXISTS = new ErrorCode(1-012-004-002, "检验工具校准记录不存在");

    ErrorCode SAMPLING_STANDARD_NOT_EXISTS = new ErrorCode(1-012-005-001, "抽样标准不存在");
    ErrorCode SAMPLING_STANDARD_NAME_DUPLICATE = new ErrorCode(1-012-005-002, "已经存在该抽样标准名称");
    ErrorCode SAMPLING_STANDARD_PARENT_IS_CHILD = new ErrorCode(1-012-005-003, "不能设置自己的子抽样标准为父抽样标准");
    ErrorCode SAMPLING_STANDARD_PARENT_NOT_EXITS = new ErrorCode(1-012-005-004,"父级抽样标准不存在");
    ErrorCode SAMPLING_STANDARD_PARENT_ERROR = new ErrorCode(1-012-005-005, "不能设置自己为父抽样标准");
    ErrorCode SAMPLING_STANDARD_EXITS_CHILDREN = new ErrorCode(1-012-005-006, "存在存在子项目，无法删除");
    ErrorCode SAMPLING_STANDARD_EXITS_RULE = new ErrorCode(1-012-005-007, "该分类存在关联的抽样规则");
    ErrorCode SAMPLING_STANDARD_RULE_NOT_EXISTS = new ErrorCode(1-012-005-010, "抽样规则不存在");

    ErrorCode INSPECTION_SHEET_NOT_EXISTS = new ErrorCode(1-012-006-001, "检验单不存在");
    ErrorCode INSPECTION_SHEET_SCHEME_NOT_EXISTS = new ErrorCode(1-012-006-002, "检验单方案任务计划不存在");
    ErrorCode INSPECTION_SHEET_SCHEME_EXISTS = new ErrorCode(1-012-006-003, "检验单方案任务计划已存在");
    ErrorCode INSPECTION_SHEET_PROCESSID_NOT_EXISTS = new ErrorCode(1-012-006-004, "工序不能为空");
    ErrorCode INSPECTION_SHEET_NO_DUPLICATE = new ErrorCode(1-012-006-005, "已经存在该检验单编号");
    ErrorCode INSPECTION_SHEET_NAME_DUPLICATE = new ErrorCode(1-012-006-006, "已经存在该检验单名称");
    ErrorCode INSPECTION_SHEET_UPDATE_FAIL = new ErrorCode(1-012-006-007, "检验单状态不是待派工或待检验");
    ErrorCode INSPECTION_SHEET_EXISTS = new ErrorCode(1-012-006-010, "检验单已存在");

    ErrorCode INSPECTION_SHEET_SCHEME_MATERIAL_NOT_EXISTS = new ErrorCode(1-012-007-001, "检验单产品不存在");
    ErrorCode INSPECTION_SHEET_SCHEME_QUALIFIED_QUANTITY_ERROR = new ErrorCode(1-012-007-002, "合格数量不能大于检验产品总数量");
    ErrorCode INSPECTION_SHEET_SCHEME_ASSIGN_FAIL = new ErrorCode(1-012-007-003, "检验单任务状态不是待派工或待检验");
    ErrorCode INSPECTION_SHEET_SCHEME_STATUS_ERROR = new ErrorCode(1-012-007-004, "检验单任务状态不是待检验或检验中");
    ErrorCode INSPECTION_SHEET_SCHEME_MATERIAL_BAR_CODE_DUPLICATE = new ErrorCode(1-012-007-005, "物料管理模式为单件，物料条码不能重复");
    ErrorCode INSPECTION_SHEET_SCHEME_MATERIAL_STOCK_NOT_EXISTS = new ErrorCode(1-012-007-006, "检验单产品不存在");
    ErrorCode INSPECTION_SHEET_SCHEME_MATERIAL_BATCH_ERROR = new ErrorCode(1-012-007-007, "检验单产品批次与检验任务不同");
    ErrorCode INSPECTION_SHEET_SCHEME_CLAIM_FAIL = new ErrorCode(1-012-007-010, "检验单任务状态不是待认领");
    ErrorCode WAREHOUSE_NOT_EXISTS = new ErrorCode(1-012-007-011, "仓库不存在");
    ErrorCode INSPECTION_OUTBOUND_ERROR = new ErrorCode(1-012-007-012, "wms创建出库单失败");
    ErrorCode INSPECTION_OUTBOUND_STATUS_ERROR = new ErrorCode(1-012-007-013, "出库单状态不是已完成");
    ErrorCode INSPECTION_INOUTBOUND_ORDER_ERROR = new ErrorCode(1-012-007-014, "当前物料已存在出库入库单");

    ErrorCode INSPECTION_SHEET_RECORD_NOT_EXISTS = new ErrorCode(1-010-010-001, "检验记录不存在");

    ErrorCode DEFECTIVE_CODE_NOT_EXISTS = new ErrorCode(1-012-011-001, "缺陷代码不存在");
    ErrorCode DEFECTIVE_CODE_DUPLICATE = new ErrorCode(1-012-011-002, "缺陷代码不能重复");
    ErrorCode UNQUALIFIED_REGISTRATION_NOT_EXISTS = new ErrorCode(1-012-011-003, "不合格品登记不存在");
    ErrorCode UNQUALIFIED_REGISTRATION_NUMBER_ERROR = new ErrorCode(1-012-011-004, "不合格总数不能大于核验不合格数");

    ErrorCode INSPECTION_SHEET_SAMPLING_RULE_NOT_EXISTS = new ErrorCode(1-012-012-001, "检验单抽样规则关系不存在");

    ErrorCode UNQUALIFIED_MATERIAL_NOT_EXISTS = new ErrorCode(1-012-013-001, "不合格品产品不存在");
    ErrorCode UNQUALIFIED_MATERIAL_DUPLICATE = new ErrorCode(1_012_013_002, "不合格产品不能重复");
    ErrorCode UNQUALIFIED_MATERIAL_FAIL_NOT_PROCESS = new ErrorCode(1_012_013_003, "不合格产品登记不是审核中状态");

    ErrorCode RETRACE_CONFIG_NOT_EXISTS = new ErrorCode(1 - 012 - 004 - 004, "追溯字段配置不存在");
    ErrorCode MATERIAL_NOT_EXISTS = new ErrorCode(1 - 012 - 004 - 005, "请填写物料编码");


    ErrorCode SAMPLING_RULE_CONFIG_NOT_EXISTS = new ErrorCode(1-012-005-006, "抽样规则（检验抽样方案）不存在");
    ErrorCode SAMPLING_RULE_AQL_NOT_EXISTS = new ErrorCode(1-012-005-007, "请填写AQL信息");

    ErrorCode MATERIAL_STOCK_NOT_EXISTS = new ErrorCode(1 - 012 - 014 - 001, "查询不到条码信息");
    ErrorCode MATERIAL_STOCK_INSPECTION_STATUS_ERROR = new ErrorCode(1 - 012 - 014 - 002, "更新库存质检状态失败");
//    ErrorCode MATERIAL_CONFIG_ERROR = new ErrorCode(1 - 012 - 014 - 002, "当前条码物料类型与检验任务物料类型不同");

    ErrorCode MANAGEMENT_TREE_NOT_EXISTS = new ErrorCode(1 - 012 - 015 - 001, "质量管理关联树不存在");
    ErrorCode MANAGEMENT_DATABASE_NOT_EXISTS = new ErrorCode(1 - 012 - 015 - 002, "质量管理资料库不存在");
    ErrorCode MANAGEMENT_DATABASE_SUBMIT_FAIL_NOT_DRAFT = new ErrorCode(1_012_015_003, "资料库不是未提交状态");
    ErrorCode MANAGEMENT_DATABASE_SUBMIT_FAIL_NOT_PROCESS = new ErrorCode(1_030_000_032, "资料库不是审核中状态");

    ErrorCode INSPECTION_SHEET_GENERATE_TASK_NOT_EXISTS = new ErrorCode(1_013_000_001, "检验单生成任务不存在");
    ErrorCode INSPECTION_SHEET_GENERATE_TASK_DETAIL_NOT_EXISTS = new ErrorCode(1_013_000_002, "检验单生成任务明细不存在");


    ErrorCode MATERIAL_LOCATION_ERROR = new ErrorCode(1-013-000-003, "当前物料不在当前工位");


}
