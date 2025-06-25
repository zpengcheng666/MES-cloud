package com.miyu.cloud.dms.controller.admin.linestationgroup.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 产线/工位组 Response VO")
@Data
@ExcelIgnoreUnannotated
public class LineStationGroupRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "17888")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "编号")
    @ExcelProperty("编号")
    private String code;

    @Schema(description = "名称", example = "李四")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "产线/工位组", example = "2")
    @ExcelProperty(value = "产线/工位组", converter = DictConvert.class)
    @DictFormat("dms_line_station_group") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer type;

    @Schema(description = "是否启用")
    @ExcelProperty(value = "是否启用", converter = DictConvert.class)
    @DictFormat("enableStatus") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer enable;

    @Schema(description = "所属类型", example = "1")
    @ExcelProperty("所属类型")
    private String affiliationDeviceType;

    @Schema(description = "本机ip", example = "1")
    @ExcelProperty("本机ip")
    private String ip;

    @Schema(description = "位置")
    @ExcelProperty("位置")
    private String location;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}