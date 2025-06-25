package com.miyu.cloud.es.api.brakeN.issue;

import lombok.Data;

@Data
public class IssueData {

    /**
     * 车牌号码
     */
    private String LicensePlateNumber;

    /**
     * 车主编号
     */
    private String OwnerNo;

    /**
     * 车主名称
     */
    private String OwnerName;

    /**
     * 车主车位数
     */
    private Integer OwnerParkingSpaceCount;

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
     * 添加时间
     */
    private String CreationTime;

    /**
     * 居住地址
     */
    private String ResidentialAddress;

    /**
     * 手机号码
     */
    private String PhoneNumber;

    /**
     * 备注信息
     */
    private String Remarks;

    /**
     * 操作人姓名
     */
    private String OperatorName;
}
