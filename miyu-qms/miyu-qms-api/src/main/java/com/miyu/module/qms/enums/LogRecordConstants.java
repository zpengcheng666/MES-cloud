package com.miyu.module.qms.enums;

/**
 * System 操作日志枚举
 * 目的：统一管理，也减少 Service 里各种“复杂”字符串
 *
 * @author 芋道源码
 */
public interface LogRecordConstants {

    // ======================= 资料库 =======================

    String QMS_DATABASE_TYPE = "QMS 资料库";
    String QMS_UPDATE_DATABASE_SUB_TYPE = "更新资料库";
    String QMS_UPDATE_DATABASE_SUCCESS = "更新资料库【{{#name}}】";

    // ======================= 检测工具 =======================

    String QMS_TOOL_TYPE = "QMS 检测工具";
    String QMS_UPDATE_TOOL_SUB_TYPE = "更新检测工具";
    String QMS_UPDATE_TOOL_SUCCESS = "更新了检测工具【{{#tool.name}}】";


    // ======================= 检测项目分类 =======================

    String QMS_ITEM_CLASS_TYPE = "QMS 检测项目分类";
    String QMS_UPDATE_ITEM_CLASS_SUB_TYPE = "更新检测项目分类";
    String QMS_UPDATE_ITEM_CLASS_SUCCESS = "更新了检测项目分类【{{#itemType.name}}】";

    // ======================= 检测项目 =======================

    String QMS_ITEM_TYPE = "QMS 检测项目";
    String QMS_UPDATE_ITEM_SUB_TYPE = "更新检测项目";
    String QMS_UPDATE_ITEM_SUCCESS = "更新了检测项目【{{#item.itemName}}】";


    // ======================= 检验方案 =======================

    String QMS_SCHEME_TYPE = "QMS 检验方案";
    String QMS_UPDATE_SCHEME_SUB_TYPE = "更新检验方案";
    String QMS_UPDATE_SCHEME_SUCCESS = "更新了检测方案【{{#scheme.schemeName}}】";


    // ======================= 缺陷代码 =======================

    String QMS_DEFECTIVE_CODE_TYPE = "QMS 缺陷代码";
    String QMS_UPDATE_DEFECTIVE_CODE_SUB_TYPE = "更新缺陷代码";
    String QMS_UPDATE_DEFECTIVE_CODE_SUCCESS = "更新了缺陷代码【{{#defectiveCode.name}}】";

    // ======================= 追溯字段配置 =======================

    String QMS_RETRACE_CONFIG_TYPE = "QMS 追溯字段配置";
    String QMS_UPDATE_RETRACE_CONFIG_SUB_TYPE = "更新追溯字段配置";
    String QMS_UPDATE_RETRACE_CONFIG_SUCCESS = "更新了追溯字段配置【{{#config.name}}】";
}
