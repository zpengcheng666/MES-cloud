package com.miyu.module.mcc.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * 销售 错误码枚举类
 *
 * mcc 系统，使用 1-014-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 发货单参数配置 1_010_000-000 ==========
    ErrorCode ENCODING_TYPE_NOT_EXISTS = new ErrorCode(1-014-000-001, "编码类别属性表(树形结构)不存在");
    ErrorCode ENCODING_TYPE_EXITS_CHILDREN = new ErrorCode(1-014-000-002, "存在存在子编码类别属性表(树形结构)，无法删除");
    ErrorCode ENCODING_TYPE_PARENT_NOT_EXITS = new ErrorCode(1-014-000-003,"父级编码类别属性表(树形结构)不存在");
    ErrorCode ENCODING_TYPE_PARENT_ERROR = new ErrorCode(1-014-000-004, "不能设置自己为父编码类别属性表(树形结构)");
    ErrorCode ENCODING_TYPE_NAME_DUPLICATE = new ErrorCode(1-014-000-005, "已经存在该名称的编码类别属性表(树形结构)");
    ErrorCode ENCODING_TYPE_PARENT_IS_CHILD = new ErrorCode(1-014-000-006, "不能设置自己的子EncodingType为父EncodingType");
    ErrorCode ENCODING_TYPE_LEVEL_LIMIT = new ErrorCode(1-014-000-007, "超过层级限制");
    ErrorCode ENCODING_TYPE_NUMBER_LIMIT = new ErrorCode(1-014-000-010, "超过位数限制");


    ErrorCode ENCODING_CLASSIFICATION_NOT_EXISTS = new ErrorCode(1-014-001-001, "编码分类不存在");


    ErrorCode ENCODING_RULE_NOT_EXISTS = new ErrorCode(1-014-002-001, "编码规则配置不存在");


    ErrorCode ENCODING_RULE_DETAIL_NOT_EXISTS = new ErrorCode(1-014-003-001, "编码规则配置详情不存在");



    ErrorCode CODE_RECORD_NOT_EXISTS = new ErrorCode(1-014-004-001, "编码记录不存在");
    ErrorCode RULE_CHECK_ERROR = new ErrorCode(1-014-005-001, "编码生成失败");




    ErrorCode ENCODING_ATTRIBUTE_NOT_EXISTS = new ErrorCode(1-014-006-001, "编码自定义属性不存在");

    ErrorCode MATERIAL_CONFIG_NOT_EXISTS = new ErrorCode(1-014-007-001, "物料类型不存在");

    ErrorCode UNIT_NOT_EXISTS = new ErrorCode(1-014-007-002, "物料类型不存在");

}
