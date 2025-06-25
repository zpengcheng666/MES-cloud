package com.miyu.cloud.dms.controller.admin.calendar.specialvo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 基础日历的工作日（特别版，调休节假日等特殊日期） Response VO")
@Data
@ExcelIgnoreUnannotated
public class CalendarSpecialRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "30709")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "基础日历id", example = "12632")
    @ExcelProperty("基础日历id")
    private String bcid;

    @Schema(description = "休息/工作(1/2)", example = "芋艿")
    @ExcelProperty(value = "休息/工作(1/2)", converter = DictConvert.class)
    @DictFormat("calendar_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer csname;

    @Schema(description = "日期")
    @ExcelProperty("日期")
    private String csdate;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
