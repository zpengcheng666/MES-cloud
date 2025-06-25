package com.miyu.cloud.macs.dal.dataobject.visitor;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import org.apache.ibatis.type.JdbcType;

/**
 * 申请角色 DO
 *
 * @author 芋道源码
 */
@TableName("macs_visitor")
@KeySequence("macs_visitor_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 编号/卡号
     */
    private String code;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 真实姓名
     */
    private String name;
    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别(0-默认未知,1-男,2-女)
     */
    private Integer sex;
    /**
     * 电话
     */
    private String phone;
    /**
     * 状态(1-正常,2-冻结)
     */
    private Integer status;
    /**
     * 公司/组织
     */
    private String organization;
    /**
     * 部门
     */
    private String department;
    /**
     * 人脸图片
     */
    private String facePicture;
    /**
     * 指纹图片
     */
    private String fingerprintPicture;
    /**
     * 当前所在区域
     */
    private String regionId;
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

}
