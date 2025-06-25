package com.miyu.cloud.macs.controller.admin.visitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 申请角色 Response VO")
@Data
@ExcelIgnoreUnannotated
public class VisitorRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "17592")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "编号/卡号")
    @ExcelProperty("编号/卡号")
    private String code;

    @Schema(description = "身份证号")
    @ExcelProperty("身份证号")
    private String idCard;

    @Schema(description = "真实姓名", example = "李四")
    @ExcelProperty("真实姓名")
    private String name;

    @Schema(description = "头像")
    @ExcelProperty("头像")
    private String avatar;

    @Schema(description = "人脸特征")
    @ExcelProperty("人脸特征")
    private byte[] facialFeature;

    @Schema(description = "指纹特征")
    @ExcelProperty("指纹特征")
    private byte[] fingerprint;

    @Schema(description = "性别(0-默认未知,1-男,2-女)")
    @ExcelProperty("性别(0-默认未知,1-男,2-女)")
    private Integer sex;

    @Schema(description = "电话")
    @ExcelProperty("电话")
    private String phone;

    @Schema(description = "状态(1-正常,2-冻结)", example = "2")
    @ExcelProperty("状态(1-正常,2-冻结)")
    private Integer status;

    @Schema(description = "删除状态(0-正常,1-已删除)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("删除状态(0-正常,1-已删除)")
    private Boolean deleted;

    @Schema(description = "公司/组织")
    @ExcelProperty("公司/组织")
    private String organization;

    @Schema(description = "部门")
    @ExcelProperty("部门")
    private String department;

    @Schema(description = "当前所在区域", example = "15249")
    @ExcelProperty("当前所在区域")
    private String regionId;

    @Schema(description = "创建人")
    @ExcelProperty("创建人")
    private String createBy;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    @ExcelProperty("更新人")
    private String updateBy;

    @Schema(description = "人脸图片")
    @ExcelProperty("人脸图片")
    private String facePicture;

    @Schema(description = "指纹图片")
    @ExcelProperty("指纹图片")
    private String fingerprintPicture;

}
