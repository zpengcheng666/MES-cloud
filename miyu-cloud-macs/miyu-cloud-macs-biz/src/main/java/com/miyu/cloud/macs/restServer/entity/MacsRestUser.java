package com.miyu.cloud.macs.restServer.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MacsRestUser {

    //id
    private String id;
    //人员卡号
    private String code;
    //人员工号
    private String userCode;
    //真实姓名
    private String name;
    //身份证号码是
    private String idCard;
    //头像
    private String avatar;
    //电话
    private String phone;
    //人脸特征信息
//    private transient String facialFeatureString;
    //指纹特征信息
//    private transient String fingerprintString;
    //校验状态
    private boolean verifyStatus;
    //校验结果信息
    private String verifyMsg;

    List<Map> applicationList;
}
