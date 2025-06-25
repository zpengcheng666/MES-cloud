package com.miyu.module.es.controller.admin.brakeN.vo;


import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 新厂车牌数据分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BrakeNVO extends PageParam {

    /**
     * 记录唯一主键
     */
    private Integer Uid;

    /**
     * 记录编号
     */
    private String No;

    /**
     *车牌号码
     */
    private String LicensePlateNumber;

    /**
     *系统车位号
     */
    private String SystemSpotNumber;

    /**
     * 车场车位号
     */
    private String ParkingSpotNumber;

    /**
     * 车主车位数
     */
    private Integer OwnerParkingSpaceCount;

    /**
     * 车主名称
     */
    private String OwnerName;

    /**
     * 车主编号
     */
    private String OwnerNo;

    /**
     * 车主身份证信息
     */
    private String OwnerIDCard;

    /**
     * 车主驾驶证
     */
    private String OwnerLicense;

    /**
     * 车主住址
     */
    private String OwnerAddress;

    /**
     * 手机号码
     */
    private String PhoneNumber;

    /**
     * 邮箱地址
     */
    private String Email;

    /**
     * 开始时间
     */
    private String StartTime;

    /**
     * 结束时间
     */
    private String EndTime;

    /**
     * 储值车余额
     */
    private Double StoredVehicleBalance;

    /**
     * 车辆类型编号
     */
    private String VehicleTypeNo;

    /**
     * 车辆类型名称
     */
    private String VehicleTypeName;

    /**
     * 车牌类型编号
     */
    private String ParkingCardTypeNo;

    /**
     * 车牌类型名称
     */
    private String ParkingCardTypeName;

    /**
     * 操作人姓名
     */
    private String OperatorName;

    /**
     * 添加时间
     */
    private String CreationTime;

    /**
     * 备注信息
     */
    private String Remarks;

    /**
     * 车牌状态
     */
    private Integer Status;

    /**
     * 白名单使能
     */
    private Integer EnableOffline;

    /**
     * 是否删除车主信息
     */
    private Boolean IsDeleteOwner;

}
