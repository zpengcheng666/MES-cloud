package com.miyu.cloud.dms.controller.admin.linestationgroup.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 产线/工位组分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LineStationGroupPageReqVO extends PageParam {

    @Schema(description = "编号")
    private String code;

    @Schema(description = "名称", example = "李四")
    private String name;

    @Schema(description = "产线/工位组", example = "2")
    private Integer type;

    @Schema(description = "是否启用")
    private Integer enable;

    @Schema(description = "所属类型", example = "1")
    private String affiliationDeviceType;

    @Schema(description = "本机ip", example = "1")
    private String ip;

    @Schema(description = "位置")
    private String location;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "更新者")
    private String updater;

}