package com.miyu.module.wms.dal.dataobject.materialstock;

import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

import javax.validation.constraints.NotEmpty;

/**
 * 物料库存 DO
 *
 * @author Qianjy
 */
@TableName("wms_material_stock")
@KeySequence("wms_material_stock_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialStockDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 物料类型id
     */
    private String materialConfigId;

    /**
     * 物料编码
     */
    @TableField(exist = false)
    private String materialNumber;
    /**
     * 物料类码
     */
    @TableField(exist = false)
    private String materialCode;

    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 物料批次号
     */
    private String batchNumber;
    /**
     * 物料id
     */
//    private String materialId;
    /**
     * 储位id
     */
    private String storageId;
    /**
     * 库位id
     */
    private String locationId;
    @TableField(exist = false)
    private String locationCode;
    /**
     * 根库位id
     */
    private String rootLocationId;
    @TableField(exist = false)
    private String rootLocationCode;
    /**
     * 绑定类型
     *
     * 枚举 {@link TODO wms_bind_type 对应的类}
     */
    private Integer bindType;
    /**
     * 总库存
     */
    private Integer totality;
    /**
     * 是否存在--用于判定是否已经出库 不被维护位置
     */
    private Boolean isExists=true;

    /**
     * 物料状态（1-待质检、2-合格、3-不合格）
     */
    private Integer materialStatus;
    /**
     * 锁定库存
     */
//    private Integer locked;
    /**
     * 可用库存
     */
//    private Integer available;
    /**
     * 容器满载比例  TODO 暂时废弃
     */
//    private Integer byOccupancyRatio;
    /**
     * 占用比例
     */
    private Integer occupancyRatio;

    /**
     * 来源物料(被分拣时生成)
     */
    private String sourceId;

    @TableField(exist = false)
    private String storageName;
    @TableField(exist = false)
    private String storageCode;

    @TableField(exist = false)
    private String locationName;
    /*@TableField(exist = false)
    private String stockBarcode;*/
    //是否为容器
    /*@TableField(exist = false)
    private Integer materialContainer;*/
    // 默认存放仓库
    @TableField(exist = false)
    private String defaultWarehouseId;
    /**
     * 物料类型（零件、托盘、工装、夹具、刀具）
     */
    @TableField(exist = false)
    private String materialType;
    //物料自己的储位id
    @TableField(exist = false)
    private String ownStorageId;

    //库存所在仓库
    @TableField(exist = false)
    private String atWarehouseId;
    //库存所在库区
    @TableField(exist = false)
    private String atAreaId;
    //库存所在库区类型
    @TableField(exist = false)
    private Integer atAreaType;

    @TableField(exist = false)
    private String orderNumber;
    @TableField(exist = false)
    private String materialTypeName;
    //订单id
    @TableField(exist = false)
    private String orderId;
    // 订单需求数量
    @TableField(exist = false)
    private Integer orderQuantity;
    // 真实出库库存id
    @TableField(exist = false)
    private String realStockId;
    @TableField(exist = false)
    private String materialName;
    //物料管理模式
    @TableField(exist = false)
    private Integer materialManage;
    // 是否单储位
    @TableField(exist = false)
    private Boolean materialStorage;
    //存放指定容器
    @TableField(exist = false)
    private String containerConfigIds;
    // 库位是否锁定
    @TableField(exist = false)
    private Boolean locationLocked;
    // 物料审批状态 1-审批中 2-审批通过 3-审批拒绝
    @TableField(exist = false)
    private Integer approvalStatus;
    //层
    @TableField(exist = false)
    private Integer materialLayer;
    //排
    @TableField(exist = false)
    private Integer materialRow;
    //列
    @TableField(exist = false)
    private Integer materialCol;

    /**
     * 节点名字
     */
    @TableField(exist = false)
    private String name;

    /**
     * 类型：0根节点，1容器，2储位
     */
    @TableField(exist = false)
    private Integer type = 1;

    /**
     * 库位列表
     */
    @TableField(exist = false)
    private List<MaterialStorageDO> childrens;
}
