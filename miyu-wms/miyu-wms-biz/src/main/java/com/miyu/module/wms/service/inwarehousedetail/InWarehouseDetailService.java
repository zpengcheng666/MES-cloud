package com.miyu.module.wms.service.inwarehousedetail;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.controller.admin.inwarehousedetail.vo.*;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.takedelivery.TakeDeliveryDO;

/**
 * 入库详情 Service 接口
 *
 * @author QianJy
 */
public interface InWarehouseDetailService {

    /**
     * 创建入库详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInWarehouseDetail(@Valid InWarehouseDetailSaveReqVO createReqVO);

    /**
     * 更新入库详情
     *
     * @param updateReqVO 更新信息
     */
    void updateInWarehouseDetail(@Valid InWarehouseDetailSaveReqVO updateReqVO);

    int updateInWarehouseDetailsByWarehouseId(Collection<String> inWarehouseIds, String warehouseId);

    /**
     * 删除入库详情
     *
     * @param id 编号
     */
    void deleteInWarehouseDetail(String id);

    /**
     * 获得入库详情
     *
     * @param id 编号
     * @return 入库详情
     */
    InWarehouseDetailDO getInWarehouseDetail(String id);

    /**
     * 获得入库详情分页
     *
     * @param pageReqVO 分页查询
     * @return 入库详情分页
     */
    PageResult<InWarehouseDetailDO> getInWarehouseDetailPage(InWarehouseDetailPageReqVO pageReqVO);


    /**
     * 根据物料id 和入库状态 查询入库详情单
     * @param materialId 物料id
     * @param inWarehouseState 入库状态
     * @return 入库详情单
     */
    InWarehouseDetailDO getInWarehouseDetailByMaterialStockIdAndState(@NotEmpty String materialId, @NotNull Integer inWarehouseState);

    /**
     * 查询-待入库 的入库详情单
     * @return 入库详情单
     */
    List<InWarehouseDetailDO> getWaitInWarehouseInWarehouseDetailList();


    /**
     *  查询-已完成的入库详情单
     * @return
     */
    List<InWarehouseDetailDO> getFinishInWarehouseDetailList(LocalDateTime[] createTimeRange);

    /**
     * 根据物料id 查询-待入库的入库详情单
     * @param materialStockIds 物料ids
     * @return 入库详情单
     */
    List<InWarehouseDetailDO> getWaitInWarehouseInWarehouseDetailListByMaterialStockIds(@NotEmpty Collection<String> materialStockIds);

    /**
     * 根据物料id 查询-待送达的入库详情单
     * @param materialStockIds 物料ids
     * @return 入库详情单
     */
    List<InWarehouseDetailDO> getWaitArriveOutWarehouseInWarehouseDetailListByMaterialStockIds(@NotEmpty Collection<String> materialStockIds);

    /**
     * 根据物料id 查询-待上架的入库详情单
     * @param materialStockIds 物料ids
     * @return 入库详情单
     */
    List<InWarehouseDetailDO> getWaitOnShelfInWarehouseDetailListByMaterialStockIds(@NotEmpty Collection<String> materialStockIds);


    /**
     * 根据目标仓库获取所有未完成的入库详情单
     * @param targetWarehouseId
     * @return
     */
    List<InWarehouseDetailDO> getBatchNotFinishByTargetWarehouseId(String targetWarehouseId);

    /**
     * 根据容器物料id 批量更新其上所有物料的入库详情单状态
     * @param materialStockId
     * @param updateState      更新状态
     * @return
     */
    boolean updateBatchInWarehouseDetailStateByMaterialStockId(@NotEmpty String materialStockId, @NotNull Integer updateState);

    /**
     * 根据容器物料id 查询其上所有物料的入库详情单
     */
    List<InWarehouseDetailDO>  getInWarehouseDetailListByMaterialStockContainer(@NotEmpty List<MaterialStockDO> materialStockListOnContainer);

    /**
     * 物料收货 更新入库详情单 填入收货物料条码信息
     * @param takeDelivery
     * @param materialStockId
     * @param materialConfig
     * @return
     */
    String takeDeliveryUpdateInWarehouseDetail(TakeDeliveryDO takeDelivery, String materialStockId, MaterialConfigDO materialConfig);

    /**
     * 根据入库单号和物料类型id查询入库详情
     * @param orderNumber 入库单号
     * @param materialConfigId 物料类型id
     */
    List<InWarehouseDetailDO> getInWarehouseDetailListByOrderNumberAndMaterialConfigId(@NotEmpty String orderNumber, @NotEmpty String materialConfigId);


    /**
     * 校验入库单
     * @param allMaterialStockMap
     */
    List<InWarehouseDetailDO> checkInWarehouseDetail( Map<String, MaterialStockDO> allMaterialStockMap);

    List<String> createBatchInWarehouseDetail(List<OrderReqDTO> orderReqDTOList);

    /**
     * 校验物料库存是否存在入库详情  并插入
     * @param inWarehouseDetailDOS
     * @return 失败的物料库存ids
     */
    List<String> checkStockAndCreateInDetail(List<InWarehouseDetailDO> inWarehouseDetailDOS);

    /**
     * 校验物料库存是否存在入库详情  并插入 创建采购入库单专用
     * @param inWarehouseDetailDOS
     * @return 失败的物料库存ids
     */
    List<String> checkStockAndCreatePurchaseInDetail(List<InWarehouseDetailDO> inWarehouseDetailDOS);

    /**
     * 校验物料库存是否存在入库详情  并插入 创建采购退货入库单专用
     * @param inWarehouseDetailDOS
     * @return 失败的物料库存ids
     */
    List<String> checkStockAndCreatePurchaseReturnInDetail(List<InWarehouseDetailDO> inWarehouseDetailDOS);

    /**
     * 根据入库单号和 物料类型id 查询存在的 入库单
     * @param materialConfigIds
     * @return
     */
    List<InWarehouseDetailDO> checkMaterialConfigsOrderExists(String orderNumber, Collection<String> materialConfigIds);


    /**
     * 根据入库单号和 物料条码 查询存在的 入库单
     * @param barCodes
     * @return
     */
    List<InWarehouseDetailDO> checkMaterialStockOrderExistsByBarCodes(String orderNumber,Collection<String> barCodes);

    /**
     * 根据入库单号查询入库详情
     * @param inOrderNumbers
     * @return
     */
    Collection <OrderReqDTO> getBatchInOrderList(List<String> inOrderNumbers);

    /**
     * 根据入库单号 填入操作人
     * @param orderIds
     * @return
     */
    int setOperatorInBatchInWarehouseDetail(Set<String> orderIds);

    /**
     * 根据批次号获取   入库单列表
     * @param orderType
     * @param batchNumber
     * @return
     */
    List<OrderReqDTO> getInOrderListByOrderTypeAndBatchNumber(Integer orderType, String batchNumber);

    int updateById(InWarehouseDetailDO inWarehouseDetail);

    /**
     * 创建盘盈入库单
     * @param profitTotality
     * @param materialStockDO
     * @param warehouseId
     * @param operator
     * @param operatorTime
     * @return
     */
    int createProfitInWarehouseDetail(int profitTotality, MaterialStockDO materialStockDO, String warehouseId, String operator, LocalDateTime operatorTime);

    /**
     * 获取未完成的入库单
     * @return
     */
    List<InWarehouseDetailDO> getNotFinishedInWarehouseDetailListByAreaId(String areaId);

    /**
     * 呼叫托盘
     * @param callTrayStockId
     * @param callLocationId
     */
    void callTray(String callTrayStockId, String callLocationId);

    /**
     * 入库操作
     * @param locationId
     */
    CommonResult<String> inWarehouseAction(MaterialStockDO materialStock, String locationId);

    String tryGenerateInWarehouseDetail(MaterialStockDO materialStock, String startWarehouseId, String targetWarehouseId, Integer quantity, MaterialStockDO realMaterialStock);

    String generateInWarehouseDetail(Integer inState, String startWarehouseId, String targetWarehouseId, Integer quantity, MaterialStockDO realMaterialStock, String trayId);

    List<OrderReqDTO> getInWarehouseDetailByChooseBarCode(String barCode);


}