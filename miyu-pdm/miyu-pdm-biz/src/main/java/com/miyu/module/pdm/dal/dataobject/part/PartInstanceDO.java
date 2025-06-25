package com.miyu.module.pdm.dal.dataobject.part;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;


@TableName("pdm_std_part_instance")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartInstanceDO extends BaseDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 实例号
     */
    private String name;
    /**
     * 产品根节点ID
     */
    private String rootproductId;
    /**
     * 零部件版本ID
     */
    private String partVersionId;
    /**
     * 父节点ID
     */
    private String parentId;
    /**
     * 客户化标识
     */
    private String customizedIndex;
    /**
     * 排序
     */
    private String serialNumber;
    /**
     * 位置坐标系，坐标原点X值
     */
    private String vmatrix01;
    /**
     * 位置坐标系，坐标原点Y值
     */
    private String vmatrix02;
    /**
     * 位置坐标系，坐标原点Z值
     */
    private String vmatrix03;
    /**
     * 位置坐标系，X轴方向向量X值
     */
    private String vmatrix04;
    /**
     * 位置坐标系，X轴方向向量Y值
     */
    private String vmatrix05;
    /**
     * 位置坐标系，X轴方向向量Z值
     */
    private String vmatrix06;
    /**
     * 位置坐标系，Y轴方向向量X值
     */
    private String vmatrix07;
    /**
     * 位置坐标系，Y轴方向向量Y值
     */
    private String vmatrix08;
    /**
     * 位置坐标系，Y轴方向向量Z值
     */
    private String vmatrix09;
    /**
     * 位置坐标系，Z轴方向向量X值
     */
    private String vmatrix10;
    /**
     * 位置坐标系，Z轴方向向量Y值
     */
    private String vmatrix11;
    /**
     * 位置坐标系，Z轴方向向量Z值
     */
    private String vmatrix12;
    /**
     * 节点类型，root产品根节点、comp部件、part零件
     */
    private String type;
    /**
     * 对象id(节点类型对应部件时存部件id)
     */
    private String targetId;

}