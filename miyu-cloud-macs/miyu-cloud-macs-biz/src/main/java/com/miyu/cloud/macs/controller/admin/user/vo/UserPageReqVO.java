package com.miyu.cloud.macs.controller.admin.user.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 门禁用户分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserPageReqVO extends PageParam {

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