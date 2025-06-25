package com.miyu.module.wms.controller.admin.warehouse.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import lombok.Data;

import java.util.List;

@Data
public class WarehouseSimpleVO {

    private String id;

    //类型：0根节点，1仓库，2库区，3库位
    private Integer type = 1;
    //节点名字
    private String name;
    //库区列表
    private List<WarehouseAreaSimpleVO> childrens;
}
