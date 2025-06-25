package com.miyu.cloud.macs.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 门禁用户新增/修改 Request VO")
@Data
public class UserSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "11711")
    private String id;

    @Schema(description = "用户id", example = "23009")
    private String userId;

    @Schema(description = "编号/卡号")
    private String code;

    @Schema(description = "人脸特征")
    private byte[] facialFeature;

    @Schema(description = "指纹特征")
    private byte[] fingerprint;

    @Schema(description = "当前所在区域", example = "7085")
    private String regionId;

    @Schema(description = "人脸图片")
    private String facePicture;

    @Schema(description = "指纹图片")
    private String fingerprintPicture;

}