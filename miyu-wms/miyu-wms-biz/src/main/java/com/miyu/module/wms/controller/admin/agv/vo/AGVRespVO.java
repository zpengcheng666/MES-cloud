package com.miyu.module.wms.controller.admin.agv.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - AGV 信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AGVRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;


    @Schema(description = "小车编号")
    @ExcelProperty("小车编号")
    private String carNo;

    @Schema(description = "机器人运行模式，手动模式=0，自动	模式=1")
    @ExcelProperty(value = "机器人运行模式，手动模式=0，自动	模式=1", converter = DictConvert.class)
    @DictFormat("wms_mode") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer mode;

    @Schema(description = "机器人的 x 坐标, 单位 m")
    @ExcelProperty("机器人的 x 坐标, 单位 m")
    private String x;

    @Schema(description = "机器人的 y坐标, 单位 m")
    @ExcelProperty("机器人的 y坐标, 单位 m")
    private String y;

    @Schema(description = "机器人当前所在站点的 id（该判断比	较严格，机器人必须很靠近某一个站	点(<30cm， 这个距离可以通过参数	配置中的 CurrentPointDist 修改)，否	则为空字符，即不处于任何站点")
    @ExcelProperty("机器人当前所在站点的 id（该判断比	较严格，机器人必须很靠近某一个站	点(<30cm， 这个距离可以通过参数	配置中的 CurrentPointDist 修改)，否	则为空字符，即不处于任何站点")
    private String currentStation;

    @Schema(description = "机器人上一个所在站点的 id")
    @ExcelProperty("机器人上一个所在站点的 id")
    private String lastStation;

    @Schema(description = "机器人底盘是否静止（行走电机）")
    @ExcelProperty(value = "机器人底盘是否静止（行走电机）", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Boolean isStop;

    @Schema(description = "机器人是否被阻挡")
    @ExcelProperty(value = "机器人是否被阻挡", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Boolean blocked;

    @Schema(description = "true 表示急停按钮处于激活状态(按	下), false 表示急停按钮处于非激活状	态(拔起)")
    @ExcelProperty(value = "true 表示急停按钮处于激活状态(按	下), false 表示急停按钮处于非激活状	态(拔起)", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Boolean emergency;

    @Schema(description = "0 = NONE, 1 = WAITING(目前不可能	出现该状态), 2 = RUNNING, 3 =	SUSPENDED, 4 = COMPLETED, 5 =	FAILED, 6 = CANCELED")
    @ExcelProperty(value = "0 = NONE, 1 = WAITING(目前不可能	出现该状态), 2 = RUNNING, 3 =	SUSPENDED, 4 = COMPLETED, 5 =	FAILED, 6 = CANCELED", converter = DictConvert.class)
    @DictFormat("wms_agv_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String taskStatus;

    @Schema(description = "自身库位id")
    @ExcelProperty("自身库位id")
    private String locationId;

}