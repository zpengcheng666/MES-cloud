package com.miyu.cloud.macs.dal.dataobject.accessApplication;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.miyu.cloud.macs.controller.admin.visitorRegion.vo.VisitorRegionRespVO;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;

/**
 * 通行申请 DO
 *
 * @author 芋道源码
 */
@TableName("macs_access_application")
@KeySequence("macs_access_application_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccessApplicationDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 申请单号
     */
    private String applicationNumber;
    /**
     * 申请代理人
     */
    private String agent;
    /**
     * 公司/组织
     */
    private String organization;
    /**
     * 部门
     */
    private String department;
    /**
     * 申请原因/目的
     */
    private String reason;
    /**
     * 申请状态
     */
    private String status;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String createBy;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableLogic
    private Boolean deleted;

    @TableField(exist = false)
    List<VisitorRegionRespVO> regionList;
}
