package com.miyu.cloud.macs.controller.admin.door.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 门分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DoorPageReqVO extends PageParam {

    @Schema(description = "名称", example = "李四")
    private String name;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "关联区域id", example = "31210")
    private String regionId;

    @Schema(description = "关联设备id", example = "31452")
    private String deviceId;

    @Schema(description = "位置", example = "赵六")
    private String locationName;

    @Schema(description = "门禁状态(0关闭,1打开,2故障)", example = "2")
    private Integer doorStatus;

    @Schema(description = "描述/备注", example = "随便")
    private String description;

    @Schema(description = "关联设备位置")
    private Integer devicePort;

}