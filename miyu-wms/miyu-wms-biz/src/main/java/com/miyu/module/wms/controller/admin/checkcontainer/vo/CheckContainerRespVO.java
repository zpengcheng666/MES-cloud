package com.miyu.module.wms.controller.admin.checkcontainer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 库存盘点容器 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CheckContainerRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "盘点计划id")
    @ExcelProperty("盘点计划id")
    private String checkPlanId;

    @Schema(description = "容器库存id")
    @ExcelProperty("容器库存id")
    private String containerStockId;

    @Schema(description = "盘点状态")
    @ExcelProperty(value = "盘点状态", converter = DictConvert.class)
    @DictFormat("check_detail_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer checkStatus;

}