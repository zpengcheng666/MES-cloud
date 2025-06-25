package com.miyu.module.wms.controller.admin.warehouse.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import lombok.Data;

import java.util.List;

@Data
public class WarehouseAreaSimpleVO {

    private String id;
    /**
     * 仓库id
     */
    private String warehouseId;

    //类型：0根节点，1仓库，2库区，3库位
    private Integer type = 2;
    //节点名字
    private String name;
    private List<WarehouseLocationSimpleVO> childrens;

}
