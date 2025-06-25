package com.miyu.module.dc.controller.admin.device.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 设备 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DeviceRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4659")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "设备id", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("设备id")
    private String deviceId;

    @Schema(description = "设备类型", example = "王五")
    private String deviceTypeId;

    @Schema(description = "产品类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "30217")
    @ExcelProperty("产品类型id")
    private String[] productTypeId;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    private String productTypeName;

    @Schema(description = "通信类型")
    private Integer commType;

    @Schema(description = "mqtt客户端id")
    private String deviceClientId;

    @Schema(description ="通讯url")
    private String deviceUrl;

    @Schema(description ="账号")
    private String username;

    @Schema(description ="密码")
    private String password;

    @Schema(description = "数据格式")
    private String DeviceJson;;

}