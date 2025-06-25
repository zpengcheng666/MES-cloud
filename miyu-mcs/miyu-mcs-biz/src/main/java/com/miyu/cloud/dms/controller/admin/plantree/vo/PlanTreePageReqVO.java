package com.miyu.cloud.dms.controller.admin.plantree.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 计划关联树分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PlanTreePageReqVO extends PageParam {

    @Schema(description = "id", example = "14340")
    private String id;

    @Schema(description = "父节点")
    private String parent;

    @Schema(description = "节点名", example = "李四")
    private String name;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "关联设备", example = "12631")
    private String deviceId;

    @Schema(description = "关联设备类型", example = "21029")
    private String deviceTypeId;

}