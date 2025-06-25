package com.miyu.cloud.macs.controller.admin.accessRecords.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 通行记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccessRecordsPageReqVO extends PageParam {

    @Schema(description = "访问人员id", example = "26945")
    private String userId;

    @Schema(description = "访客访问人员id", example = "16008")
    private String visitorId;

    @Schema(description = "访问人员编号")
    private String userCode;

    @Schema(description = "访问人员姓名", example = "王五")
    private String userName;

    @Schema(description = "操作人员id", example = "4702")
    private String operatorId;

    @Schema(description = "设备id", example = "20212")
    private String deviceId;

    @Schema(description = "门id", example = "17978")
    private String doorId;

    @Schema(description = "门名称", example = "赵六")
    private String doorName;

    @Schema(description = "采集器Id", example = "28400")
    private String collectorId;

    @Schema(description = "采集器名称", example = "李四")
    private String collectorName;

    @Schema(description = "采集器编码")
    private String collectorCode;

    @Schema(description = "位置Id", example = "3462")
    private String regionId;

    @Schema(description = "位置名称", example = "张三")
    private String regionName;

    @Schema(description = "动作(-1,未知,0校验,1进,2出,3开门,4关门)")
    private Integer action;

    @Schema(description = "备注信息")
    private String message;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}