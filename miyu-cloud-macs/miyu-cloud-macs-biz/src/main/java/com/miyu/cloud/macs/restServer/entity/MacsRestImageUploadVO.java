package com.miyu.cloud.macs.restServer.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MacsRestImageUploadVO {

    private List<MultipartFile> fileList;

    private List<String> fileListAddr;
    //卡号
    private String cardNumber;

    private String operatorId;

    //文件信息类别 1.卡号 2.人脸 3.指纹
    private int infoType;

    //用户访客Id
    private String id;
    //姓名
    private String name;
    //身份证号
    private String idCard;


    private boolean visitor;

    private String idType = "idCard";

    private String phone;

}
