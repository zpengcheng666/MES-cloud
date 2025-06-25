package com.miyu.cloud.macs.dal.dataobject.accessRecords;

import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.miyu.cloud.macs.dal.dataobject.collector.CollectorDO;
import com.miyu.cloud.macs.dal.dataobject.device.DeviceDO;
import com.miyu.cloud.macs.dal.dataobject.door.DoorDO;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import com.miyu.cloud.macs.dal.dataobject.visitor.VisitorDO;
import lombok.*;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 通行记录 DO
 *
 * @author 芋道源码
 */
@TableName("macs_access_records")
@KeySequence("macs_access_records_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessRecordsDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 访问人员id
     */
    private String userId;
    /**
     * 访客访问人员id
     */
    private String visitorId;
    /**
     * 访问人员编号
     */
    private String userCode;
    /**
     * 访问人员姓名
     */
    private String userName;
    /**
     * 操作人员id
     */
    private String operatorId;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 门id
     */
    private String doorId;
    /**
     * 门名称
     */
    private String doorName;
    /**
     * 采集器Id
     */
    private String collectorId;
    /**
     * 采集器名称
     */
    private String collectorName;
    /**
     * 采集器编码
     */
    private String collectorCode;
    /**
     * 位置Id
     */
    private String regionId;
    /**
     * 位置名称
     */
    private String regionName;
    /**
     * 动作(-1,未知,0校验,1进,2出,3开门,4关门)
     */
    private Integer action;
    /**
     * 备注信息
     */
    private String message;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    private CollectorDO collector;
    @TableField(exist = false)
    private DoorDO door;
    @TableField(exist = false)
    private DeviceDO device;
    @TableField(exist = false)
    private RegionDO region;
    @TableField(exist = false)
    private String avatar;

    public enum InstructType {
        open,//长开
        close,//关
        general,//通行一次
        validate//验证
    }

    public AccessRecordsDO setUser(AdminUserRespDTO user) {
        this.userId = user.getId()+"";
        this.userName = user.getNickname();
//        this.userCode = user.get(); //todo 芋道原框架无此字段
//        this.avatar = user.getAvatar();
        return this;
    }

    public AccessRecordsDO setVisitor(VisitorDO visitor) {
        this.visitorId = visitor.getId();
        this.userName = visitor.getName();
        this.avatar = visitor.getAvatar();
        return this;
    }

    public AccessRecordsDO setOperator(AdminUserRespDTO user) {
        this.operatorId = user.getId() + "";
        this.userName = user.getNickname();
//        this.userCode = user.getWorkNo(); //todo 无
//        this.avatar = user.getAvatar();
        return this;
    }

    public AccessRecordsDO setRegion(RegionDO region) {
        this.region = region;
        this.regionId = region.getId();
        this.regionName = region.getName();
        return this;
    }

    public AccessRecordsDO setDevice(DeviceDO device) {
        this.device = device;
        this.deviceId = device.getId();
        return this;
    }

    public AccessRecordsDO setDoor(DoorDO door) {
        this.door = door;
        this.doorId = door.getId();
        this.doorName = door.getName();
        return this;
    }

    public AccessRecordsDO setCollector(CollectorDO collector) {
        this.collector = collector;
        this.collectorId = collector.getId();
        this.collectorName = collector.getName();
        this.collectorCode = collector.getCode();
        return this;
    }
}
