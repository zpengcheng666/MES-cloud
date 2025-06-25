package com.miyu.cloud.dms.dal.dataobject.devicetype;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 设备类型 DO
 *
 * @author 王正浩
 */
@TableName("dms_device_type")
@KeySequence("dms_device_type_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTypeDO extends BaseDO {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 类型编号
     */
    private String code;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 设备/工位
     */
    private Integer type;
    /**
     * 是否启用
     */
    private Integer enable;
    /**
     * 规格型号
     */
    private String specification;
    /**
     * 生产厂家
     */
    private String manufacturer;
    /**
     * 产地
     */
    private String countryRegion;
    /**
     * 厂家联系人
     */
    private String contacts;
    /**
     * 厂家联系电话
     */
    private String contactPhone;
    /**
     * 备注
     */
    private String remark;
}