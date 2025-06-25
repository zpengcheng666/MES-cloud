package com.miyu.module.dc.controller.admin.device.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 设备运行监控 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DeviceOfflineRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4659")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "设备id", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("设备id")
    private String deviceId;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "通信类型")
    private Integer commType;

    @Schema(description = "产品类型")
    private List<ProductTypeDO> productTypeList;

    @Schema(description = "设备状态")
    private Integer deviceStatus;

}
