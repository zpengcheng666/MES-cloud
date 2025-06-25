package com.miyu.module.es.dal.dataobject.brake;

import lombok.*;

import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 旧厂车牌数据 DO
 *
 * @author 上海弥彧
 */
@TableName("es_brake")
@KeySequence("es_brake_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrakeDO extends BaseDO {

    /**
     * 主键id(固定车id)
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 车牌号
     */
    private String registerPlate;
    /**
     * 剩余次数(用于充值扣次数)
     */
    private Integer numberTimes;
    /**
     * 客户电话
     */
    private String phoneNumber;
    /**
     * 账户余额
     */
    private Double balance;
    /**
     * 证件类型
     */
    private String identiType;
    /**
     * 客户类型
     */
    private String clientType;
    /**
     * 通行证类型
     */
    private String passTypeName;
    /**
     * 证件编号
     */
    private String identiNumber;
    /**
     * 客户编号
     */
    private String clientNo;
    /**
     * 客户邮箱
     */
    private String email;
    /**
     * 到期时间
     */
    private String deadline;
    /**
     * 车位类型
     */
    private String parkingSpaceType;
    /**
     * 车辆类型
     */
    private String carTypeName;
    /**
     * 客户名称
     */
    private String clientName;
    /**
     * 客户住址
     */
    private String address;
    /**
     * 客户性别
     */
    private String sex;

}