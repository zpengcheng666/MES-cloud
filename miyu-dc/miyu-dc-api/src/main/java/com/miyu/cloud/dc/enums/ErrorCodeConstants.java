package com.miyu.cloud.dc.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {

    ErrorCode PRODUCT_TYPE_NOT_EXISTS = new ErrorCode(5001, "产品类型不存在");

    ErrorCode DEVICE_NOT_EXISTS = new ErrorCode(5002, "设备不存在");

    ErrorCode DEVICE_TOPIC_ERROR = new ErrorCode(5003, "通讯编码错误");

    ErrorCode DEVICE_TYPE_NULL_ERROR = new ErrorCode(5008, "该设备类型下无绑定设备，请绑定后重试");

    ErrorCode DEVICE_NAME_EXISTS = new ErrorCode(5004, "设备已存在");

    ErrorCode DEVICE_COMMTYPE_ERROR = new ErrorCode(5005, "传输方式错误");

    ErrorCode PRODUCT_TYPE_EXISTS = new ErrorCode(5006, "该产品类型正在使用，不可删除！");

    ErrorCode PRODUCT_TYPE_EXISTS_USE = new ErrorCode(5007, "该设备类型正在使用，不可编辑！");

    ErrorCode COLLECT_TYPE_EXISTS = new ErrorCode(5010, "产品名称已存在");

    ErrorCode COLLECT_TOPIC_EXISTS = new ErrorCode(5011, "产品通讯编码已存在");

    ErrorCode DEVICE_TOPIC_EXISTS = new ErrorCode(5012, "通讯编码已存在");

    ErrorCode DEVICE_CLIENT_EXISTS = new ErrorCode(5013, "客户端id已存在");

    ErrorCode COLLECT_ATTRIBUTES_COMPARE = new ErrorCode(5018, "标准下限值不可大于标准上限值");

    //==========================================================================================================================>

    //数据采集日志类
    ErrorCode DC_CODE_ERROR = new ErrorCode(6005, "传入数据格式错误，请确认后重新传入！");




}
