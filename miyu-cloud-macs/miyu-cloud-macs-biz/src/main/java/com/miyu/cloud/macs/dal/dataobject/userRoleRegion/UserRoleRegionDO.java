package com.miyu.cloud.macs.dal.dataobject.userRoleRegion;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;


/**
 * 用户角色 DO
 *
 * @author 芋道源码
 */
@TableName("macs_user_role_region")
@KeySequence("macs_user_role_region_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRegionDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 区域id
     */
    private String regionId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 角色id
     */
    private String roleId;

    public UserRoleRegionDO(String regionId, String roleId, String userId) {
        this.userId = "null".equals(userId)?null:userId;
        this.roleId = "null".equals(roleId)?null:roleId;
        this.regionId = regionId;
    }
}
