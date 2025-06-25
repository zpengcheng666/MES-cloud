package com.miyu.module.es.controller.admin.brake.vo;

import lombok.*;

import java.time.LocalDate;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 旧厂车牌数据分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BrakePageReqVO extends PageParam {

    private String carPlateNo;

    @Schema(description = "每页数量")
    private Integer pageNum;

    @Schema(description = "车牌号")
    private String registerPlate;

    @Schema(description = "剩余次数(用于充值扣次数)")
    private Integer numberTimes;

    @Schema(description = "客户电话")
    private String phoneNumber;

    @Schema(description = "账户余额")
    private Double balance;

    @Schema(description = "证件类型", example = "1")
    private String identiType;

    @Schema(description = "客户类型", example = "1")
    private String clientType;

    @Schema(description = "通行证类型", example = "张三")
    private String passTypeName;

    @Schema(description = "证件编号")
    private String identiNumber;

    @Schema(description = "客户编号")
    private String clientNo;

    @Schema(description = "客户邮箱")
    private String email;

    @Schema(description = "到期时间")
    private String deadline;

    @Schema(description = "车位类型", example = "1")
    private String parkingSpaceType;

    @Schema(description = "车辆类型", example = "芋艿")
    private String carTypeName;

    @Schema(description = "客户名称", example = "芋艿")
    private String clientName;

    @Schema(description = "客户住址")
    private String address;

    @Schema(description = "客户性别")
    private String sex;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}