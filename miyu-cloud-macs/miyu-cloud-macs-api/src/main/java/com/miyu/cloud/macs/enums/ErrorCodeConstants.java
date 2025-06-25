package com.miyu.cloud.macs.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * macs 错误码枚举类
 */
public interface ErrorCodeConstants {

    ErrorCode VISITOR_REGION_NOT_EXISTS = new ErrorCode(401, "访客区域权限不存在");
    ErrorCode ACCESS_APPLICATION_NOT_EXISTS = new ErrorCode(401, "访客区域权限不存在");
    ErrorCode ACCESS_RECORDS_NOT_EXISTS = new ErrorCode(401, "访客区域权限不存在");
    ErrorCode COLLECTOR_NOT_EXISTS = new ErrorCode(401, "访客区域权限不存在");
    ErrorCode DEVICE_NOT_EXISTS = new ErrorCode(401, "访客区域权限不存在");
    ErrorCode DOOR_NOT_EXISTS = new ErrorCode(401, "访客区域权限不存在");
    ErrorCode REGION_NOT_EXISTS = new ErrorCode(401, "访客区域权限不存在");
    ErrorCode STRATEGY_NOT_EXISTS = new ErrorCode(401, "访客区域权限不存在");
    ErrorCode COLLECTOR_STRATEGY_NOT_EXISTS = new ErrorCode(401, "访客区域权限不存在");
    ErrorCode USER_NOT_EXISTS = new ErrorCode(401, "访客区域权限不存在");
    ErrorCode VISITOR_NOT_EXISTS = new ErrorCode(401, "访客区域权限不存在");
    ErrorCode REGION_EXITS_CHILDREN = new ErrorCode(402, "存在存在子区域，无法删除");
    ErrorCode REGION_PARENT_NOT_EXITS = new ErrorCode(402,"父级区域不存在");
    ErrorCode REGION_PARENT_ERROR = new ErrorCode(402, "不能设置自己为父区域");
    ErrorCode REGION_CODE_DUPLICATE = new ErrorCode(402, "已经存在该区域编码的区域");
    ErrorCode REGION_PARENT_IS_CHILD = new ErrorCode(402, "不能设置自己的子Region为父Region");

}
