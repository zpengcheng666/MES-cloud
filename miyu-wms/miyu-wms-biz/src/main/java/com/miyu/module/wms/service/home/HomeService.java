package com.miyu.module.wms.service.home;

import com.alibaba.fastjson.JSONObject;
import com.miyu.module.wms.controller.admin.home.vo.InOutWarehouseRespVO;
import com.miyu.module.wms.controller.admin.home.vo.InOutWarehouseStatisticsVO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface HomeService {


    /**
     * 获取库位占用率
     * @param warehouseLocationDOS 仓库-库位列表
     * @return 库位占用率
     */
    Map<String, Map<String, Integer>> warehouseOccupancyRate (List<WarehouseLocationDO> warehouseLocationDOS);

    /**
     * 出入库统计
     */
    JSONObject warehouseInOutAnalysis(LocalDateTime[] createTimeRange);

    List<InOutWarehouseRespVO> getManualInList();

    List<InOutWarehouseRespVO> getManualOutList();
}
