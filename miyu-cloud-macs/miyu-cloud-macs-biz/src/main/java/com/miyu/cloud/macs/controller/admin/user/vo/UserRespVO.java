package com.miyu.cloud.macs.controller.admin.user.vo;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 门禁用户 Response VO")
@Data
@ExcelIgnoreUnannotated
public class UserRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "11711")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "用户id", example = "23009")
    @ExcelProperty("用户id")
    private String userId;

    @Schema(description = "编号/卡号")
    @ExcelProperty("编号/卡号")
    private String code;

    @Schema(description = "人脸特征")
    @ExcelProperty("人脸特征")
    private byte[] facialFeature;

    @Schema(description = "指纹特征")
    @ExcelProperty("指纹特征")
    private byte[] fingerprint;

    @Schema(description = "当前所在区域", example = "7085")
    @ExcelProperty("当前所在区域")
    private String regionId;

    @Schema(description = "人脸图片")
    @ExcelProperty("人脸图片")
    private String facePicture;

    @Schema(description = "指纹图片")
    @ExcelProperty("指纹图片")
    private String fingerprintPicture;

    @Schema(description = "指纹图片")
    @ExcelProperty("指纹图片")
    private String fingerprintPicture0;

    public void setFingerprintPicture(String fingerprintPicture0) {
        this.fingerprintPicture = fingerprintPicture0;
        if (fingerprintPicture0 != null && !"".equals(fingerprintPicture0)) {
            this.fingerprintPicture0 = JsonUtils.parseArray(fingerprintPicture0, String.class).get(0);
        }
    }
}
