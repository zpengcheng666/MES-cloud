package com.miyu.module.wms.controller.admin.agv.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - AGV 信息新增/修改 Request VO")
@Data
public class AGVSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "小车编号")
    @ExcelProperty("小车编号")
    private String carNo;

    @Schema(description = "机器人运行模式，手动模式=0，自动	模式=1")
    private Integer mode;

    @Schema(description = "机器人的 x 坐标, 单位 m")
    private String x;

    @Schema(description = "机器人的 y坐标, 单位 m")
    private String y;

    @Schema(description = "机器人当前所在站点的 id（该判断比	较严格，机器人必须很靠近某一个站	点(<30cm， 这个距离可以通过参数	配置中的 CurrentPointDist 修改)，否	则为空字符，即不处于任何站点")
    private String currentStation;

    @Schema(description = "机器人上一个所在站点的 id")
    private String lastStation;

    @Schema(description = "机器人底盘是否静止（行走电机）")
    private Boolean isStop;

    @Schema(description = "机器人是否被阻挡")
    private Boolean blocked;

    @Schema(description = "true 表示急停按钮处于激活状态(按	下), false 表示急停按钮处于非激活状	态(拔起)")
    private Boolean emergency;

    @Schema(description = "0 = NONE, 1 = WAITING(目前不可能	出现该状态), 2 = RUNNING, 3 =	SUSPENDED, 4 = COMPLETED, 5 =	FAILED, 6 = CANCELED")
    private String taskStatus;

    @Schema(description = "自身库位id")
    private String locationId;

}