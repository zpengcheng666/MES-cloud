package com.miyu.module.es.dal.dataobject.monthlyCar;

import lombok.Data;

@Data
public class MonthlyCarData {

    //记录编号  //固定车id
    private String No;

    //车牌号码  //车牌号
    private String LicensePlateNumber;

    //车场车位号 //默认无
    private String ParkingSpotNumber;

    //车主车位数 //默认1
    private Integer OwnerParkingSpaceCount;

    //车主名称 //对应客户名称
    private String OwnerName;

    //车主编号 //对应客户编号
    private String OwnerNo;

    //车主身份证信息
    private String OwnerIDCard;

    //车主驾驶证
    private String OwnerLicense;

    //车主住址 //客户住址
    private String OwnerAddress;

    //手机号码 //手机号码
    private String PhoneNumber;

    //邮箱地址 //邮箱
    private String Email;

    //开始时间 //无
    private String StartTime;

    //结束时间 //结束时间
    private String EndTime;

    //储值车余额 //账户余额
    private String StoredVehicleBalance;

    //车辆类型编号 //判断
    private String VehicleTypeNo;

    //车牌类型编号 //判断
    private String ParkingCardTypeNo;

    //操作人姓名 //获取登陆人
    private String OperatorName;

    //备注信息 //住址
    private String Remarks;

    //白名单使能 //默认1
    private Integer EnableOffline;

    //是否删除车主信息  //默认false
    private Boolean IsDeleteOwner;
}
