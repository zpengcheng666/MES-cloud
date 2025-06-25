package com.miyu.cloud.macs.restServer.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MacsRestPersonUpdateVO {

    //设备编号
    private String deviceNumber;
    //保存id
    private String userID;
    //人员Id
    private String personId;
    //开始时间
    private String validStart;
    //结束时间
    private String validEnd;
    //门号
    private int[] doorsList = {0,1};
    //卡号
    private String cardNo;
    //人脸地址
    private String facePath;
    //指纹地址
    private List<String> fingerprintPathList;



}
