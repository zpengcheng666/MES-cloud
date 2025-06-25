package com.miyu.module.es.controller.admin.brake.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 旧厂车牌数据 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BrakeRespVO {

    @Schema(description = "主键id(固定车id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "16009")
    @ExcelProperty("主键id(固定车id)")
    private String id;

    @Schema(description = "车牌号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("车牌号")
    private String registerPlate;

    @Schema(description = "剩余次数(用于充值扣次数)")
    @ExcelProperty("剩余次数(用于充值扣次数)")
    private Integer numberTimes;

    @Schema(description = "客户电话")
    @ExcelProperty("客户电话")
    private String phoneNumber;

    @Schema(description = "账户余额")
    @ExcelProperty("账户余额")
    private Double balance;

    @Schema(description = "证件类型", example = "1")
    @ExcelProperty("证件类型")
    private String identiType;

    @Schema(description = "客户类型", example = "1")
    @ExcelProperty("客户类型")
    private String clientType;

    @Schema(description = "通行证类型", example = "张三")
    @ExcelProperty("通行证类型")
    private String passTypeName;

    @Schema(description = "证件编号")
    @ExcelProperty("证件编号")
    private String identiNumber;

    @Schema(description = "客户编号")
    @ExcelProperty("客户编号")
    private String clientNo;

    @Schema(description = "客户邮箱")
    @ExcelProperty("客户邮箱")
    private String email;

    @Schema(description = "到期时间")
    @ExcelProperty("到期时间")
    private String deadline;

    @Schema(description = "车位类型", example = "1")
    @ExcelProperty("车位类型")
    private String parkingSpaceType;

    @Schema(description = "车辆类型", example = "芋艿")
    @ExcelProperty("车辆类型")
    private String carTypeName;

    @Schema(description = "客户名称", example = "芋艿")
    @ExcelProperty("客户名称")
    private String clientName;

    @Schema(description = "客户住址")
    @ExcelProperty("客户住址")
    private String address;

    @Schema(description = "客户性别")
    @ExcelProperty("客户性别")
    private String sex;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}