package com.miyu.cloud.dms.dal.dataobject.plantree;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 计划关联树 DO
 *
 * @author 王正浩
 */
@TableName("dms_plan_tree")
@KeySequence("dms_plan_tree_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanTreeDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 父节点
     */
    private String parent;
    /**
     * 节点名
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
    /**
     * 关联设备
     */
    private String deviceId;
    /**
     * 关联设备类型
     */
    private String deviceTypeId;

}