package com.miyu.cloud.macs.dal.dataobject.user;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 门禁用户 DO
 *
 * @author 芋道源码
 */
@TableName("macs_user")
@KeySequence("macs_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 编号/卡号
     */
    private String code;
    /**
     * 当前所在区域
     */
    private String regionId;
    /**
     * 人脸图片
     */
    private String facePicture;
    /**
     * 指纹图片
     */
    private String fingerprintPicture;

    @TableField(exist = false)
    private String phone;

}
