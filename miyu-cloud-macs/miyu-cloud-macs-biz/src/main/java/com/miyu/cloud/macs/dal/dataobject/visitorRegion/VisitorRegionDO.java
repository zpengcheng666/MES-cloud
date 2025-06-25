package com.miyu.cloud.macs.dal.dataobject.visitorRegion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.miyu.cloud.macs.dal.dataobject.visitor.VisitorDO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 访客区域权限 DO
 *
 * @author 芋道源码
 */
@TableName("macs_visitor_region")
@KeySequence("macs_visitor_region_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisitorRegionDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 访客id
     */
    private String visitorId;
    /**
     * 区域id
     */
    private String regionId;
    /**
     * 访客申请id
     */
    private String applicationId;
    /**
     * 生效日期
     */
    private LocalDateTime effectiveDate;
    /**
     * 失效日期
     */
    private LocalDateTime invalidDate;

    @TableField(exist = false)
    private List<String> regionIds;

    @TableField(exist = false)
    private VisitorDO visitor;

    public VisitorRegionDO(VisitorRegionDO visitorRegion) {
        this.visitorId = visitorRegion.visitorId;
        this.regionId = visitorRegion.regionId;
        this.applicationId = visitorRegion.applicationId;
        this.effectiveDate = visitorRegion.effectiveDate;
        this.invalidDate = visitorRegion.invalidDate;
    }
}
