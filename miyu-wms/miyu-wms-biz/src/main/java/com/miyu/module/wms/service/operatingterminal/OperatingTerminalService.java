package com.miyu.module.wms.service.operatingterminal;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.alibaba.fastjson.JSONObject;
import com.miyu.module.wms.controller.admin.operatingterminal.vo.CutterOrderVO;
import com.miyu.module.wms.controller.admin.operatingterminal.vo.OrderVO;
import com.miyu.module.wms.dal.dataobject.instruction.InstructionDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface OperatingTerminalService {
    /**
     * 获取所有 待入库 待出库 待移库 的订单明细
     *
     * @return
     */
    List<OrderVO> getWaitOrderDetailList(String areaCode);

    List<CutterOrderVO> getCutterWaitOrderDetailList(String warehouseId);

    List<CutterOrderVO> getCutterWaitInOrderDetailList(String warehouseId);


    /**
     * 根据locationId查询物料*
     */
    List<MaterialStockDO> getMaterialStockByLocationIds(Collection<String> locationIds);

    MaterialStockDO getMaterialStockByLocationCode(String locationCode);

    /**
     * 查询库位上空托盘
     */
    List<MaterialStockDO> getEmptyTrayListByWareHouseArea();

    /**
     * 查询出库单上物料
     */
    List<MaterialStockDO> getMaterialStockByOutWarehouseDetail(String warehouseId);

    /**
     * 获取仓库下所有物料库存列表
     */
    List<MaterialStockDO> getMaterialStockListByWarehouseId(String warehouseId);

    /**
     * 获取仓库下存储库区的托盘和工装
     */
    List<MaterialStockDO> getMaterialStockListByWarehouseIdAndAreaTypeEq1(String warehouseId);

    /**
     * 托盘入库(上架)操作
     *
     * @param map sourceLocationCode:源库位编码
     * @return
     */
    InstructionDO available(Map<String, String> map);


    /**
     * 选择托盘(下架)操作
     *
     * @param map materialStockId  物料id
     * @param map startLocationId  起始库位id
     * @param map targetLocationCode 目标库位编码
     * @return
     */
    InstructionDO palletUnloading(Map<String, String> map);

    /**
     * 将物料储位改为指定容器的储位
     *
     * @param materialStockId 物料id
     * @param storageStockId  容器id
     * @param orderId 拣选订单id
     * @param materialPosition 容器位号(容器上的位置),可为null
     */
    void changeMaterialStockStorage(String materialStockId, String storageStockId, String orderId,Integer materialPosition);


    /**
     * 通过条码查看物料类型
     */
    MaterialConfigDO getMaterialConfigByBarcode(String barcode);

    /**
     * 物料指定的容器列表
     */
    List<MaterialConfigDO> getContainerIdListByMaterialId(String materialId);

    /**
     * 物料绑定托盘或者工装
     */
    Boolean bindTrayOrClamp(Map<String, Object> map) throws Exception;

    /**
     * 工步开工和工序开工
     */
    void batchRecordStartForMCS(Map<String, Object> map);

    /**
     * 根据条码获取刀具信息
     * @param barCode
     * @return
     */
    JSONObject getCutterInfo(String barCode);
}
