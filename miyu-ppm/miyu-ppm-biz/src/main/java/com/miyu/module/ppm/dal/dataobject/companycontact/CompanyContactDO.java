package com.miyu.module.ppm.dal.dataobject.companycontact;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 企业联系人 DO
 *
 * @author Zhangyunfei
 */
@TableName("pd_company_contact")
@KeySequence("pd_company_contact_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyContactDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 企业ID
     */
    private String companyId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 部门
     */
    private String depart;
    /**
     * 职务
     */
    private String position;
    /**
     * 在职状态：0-在职、1-离职
     */
    private Integer status;
    /**
     * 直属上级，子表ID
     */
    private String superior;
    /**
     * 部门负责人
     */
    private Integer header;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 地址
     */
    private String address;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建IP
     */
    private String creationIp;
    /**
     * 更新ip
     */
    private String updatedIp;

    /**
     * 企业名称
     */
    @TableField(exist = false)
    private String companyName;

}