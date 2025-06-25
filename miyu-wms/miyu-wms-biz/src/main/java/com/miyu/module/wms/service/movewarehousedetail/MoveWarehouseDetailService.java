package com.miyu.module.wms.service.movewarehousedetail;

import javax.validation.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.controller.admin.movewarehousedetail.vo.*;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import org.checkerframework.checker.units.qual.C;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 库存移动详情 Service 接口
 *
 * @author QianJy
 */
public interface MoveWarehouseDetailService {

    /**
     * 创建库存移动详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMoveWarehouseDetail(@Valid MoveWarehouseDetailSaveReqVO createReqVO);

    /**
     * 更新库存移动详情
     *
     * @param updateReqVO 更新信息
     */
    void updateMoveWarehouseDetail(@Valid MoveWarehouseDetailSaveReqVO updateReqVO);

    /**
     * 删除库存移动详情
     *
     * @param id 编号
     */
    void deleteMoveWarehouseDetail(String id);

    /**
     * 获得库存移动详情
     *
     * @param id 编号
     * @return 库存移动详情
     */
    MoveWarehouseDetailDO getMoveWarehouseDetail(String id);

    /**
     * 获得库存移动详情分页
     *
     * @param pageReqVO 分页查询
     * @return 库存移动详情分页
     */
    PageResult<MoveWarehouseDetailDO> getMoveWarehouseDetailPage(MoveWarehouseDetailPageReqVO pageReqVO);

    /**
     * 获得库待出库的 移库详情列表
     * @param materialStockIds
     * @return
     */
    List<MoveWarehouseDetailDO> getWaitOutMoveWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds);

    /**
     * 根据移库状态 获得库存移动详情列表
     * @param materialStockIds
     * @param moveState
     * @return
     */
    List<MoveWarehouseDetailDO> getMoveWarehouseDetailListByMaterialStockIds(List<String> materialStockIds, Integer moveState);

    /**
     * 根据ids 获得移库详情列表
     * @param inWarehouseIds
     * @return
     */
    List<MoveWarehouseDetailDO> getMoveWarehouseDetailIds(List<String> inWarehouseIds);

    /**
     * 根据物料id 批量更新移库单状态
     * @param materialStockId
     * @param updateState
     * @return
     */
    boolean updateBatchMoveWarehouseDetailStateByMaterialStockId(@NotEmpty String materialStockId, @NotNull Integer updateState);

    /**
     * 获得移库详情列表 -- 待出库
     * @return
     */
    List<MoveWarehouseDetailDO> getWaitMoveWarehouseMoveWarehouseDetailList();


    /**
     * 根据物料获取移库详情列表 -- 待出库
     * @param materialStockIds
     * @return
     */
    List<MoveWarehouseDetailDO> getWaitOutWarehouseMoveWarehouseDetailListByMaterialStockIds(List<String> materialStockIds);

    /**
     * 根据物料获取移库详情列表 -- 待送达
     * @param materialStockIds
     * @return
     */
    List<MoveWarehouseDetailDO> getWaitArriveMoveWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds);

    /**
     * 根据物料获取移库详情列表 -- 待签收  --包括待出库的
     * @param materialStockIds
     * @return
     */
    List<MoveWarehouseDetailDO> getWaitSignForMoveWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds);

    /**
     * 根据物料id 更新移库单状态
     * @param containerStockId
     * @param updateState
     * @return
     */
    boolean updateMoveWarehouseDetailStateByMaterialStockId(String containerStockId, Integer updateState);

    List<String> createBatchMoveWarehouseDetail(List<OrderReqDTO> orderReqDTOList);

    /**
     * 校验物料库存是否存在移库详情  并插入
     * @param moveWarehouseDetailDOS
     * @return 失败的物料库存ids
     */
    List<String> checkStockAndCreateMoveDetail(List<MoveWarehouseDetailDO> moveWarehouseDetailDOS);

    /**
     * 获取未完成的移库详情
     */
    List<MoveWarehouseDetailDO> getNotFinishMoveDetailList();

    /**
     * 校验移库详情
     * @param targetWarehouseId
     * @param allMaterialStockMap
     */
    List<MoveWarehouseDetailDO> checkMoveWarehouseDetail(String targetWarehouseId, Map<String, MaterialStockDO> allMaterialStockMap);

    /**
     * 获取批量移库订单列表
     * @param moveOrderNumbers
     * @return
     */
    List<OrderReqDTO> getBatchMoveOrderList(List<String> moveOrderNumbers);

    /**
     * 根据单号 填入操作人
     * @param orderIds
     * @return
     */
    int setOperatorInBatchMoveWarehouseDetail(Set<String> orderIds);
    /**
     * 根据批次号获取移库单列表
     * @param batchNumber
     * @return
     */
    List<OrderReqDTO> getMoveOrderListByOrderTypeAndBatchNumber(Integer orderType, String batchNumber);

    int updateById(MoveWarehouseDetailDO moveWarehouseDetail);

    /**
     * 获取未完成的移库单列表
     * @return
     */
    List<MoveWarehouseDetailDO> getNotFinishedMoveWarehouseDetailListByAreaId(String areaId);

    /**
     * 尝试生成移库单
     * @param materialStock
     * @param targetLocationId
     * @param startWarehouseId
     * @param targetWarehouseId
     * @param quantity
     * @param realMaterialStock
     * @return
     */
    String tryGenerateMoveWarehouseDetail(MaterialStockDO materialStock, String targetLocationId, String startWarehouseId, String targetWarehouseId, Integer quantity, MaterialStockDO realMaterialStock);

    /**
     * 尝试生成移库移库单  库位→库位  -- 三坐标专用
     * @param containerStock
     * @param startLocationId
     * @param targetLocationId
     * @param realMaterialStock
     * @return
     */
    String tryGenerateMoveWarehouseDetail(MaterialStockDO containerStock, String startLocationId, String targetLocationId, MaterialStockDO realMaterialStock);


    String generateMoveWarehouseDetail(Integer moveState, String startWarehouseId, String targetWarehouseId, String targetLocationId,Integer quantity, MaterialStockDO realMaterialStock, String trayId);
    /**
     * 移库操作
     * @param locationId
     * @param targetWarehouseId 目标仓库id
     * @param targetLocationId 目标库位id
     */
    void moveWarehouseAction(String locationId, String targetWarehouseId, String targetLocationId);

    void insetBatchMoveWarehouseDetail(List<OutWarehouseDetailDO> waitOutWarehouseDetailList, Integer moveType, Integer moveStatus, String targetWarehouseId);

    List<OrderReqDTO> getMoveWarehouseDetailByChooseBarCode(String barCode);

    /**
     * 获取未完成的移库单列表
     * @return
     */
    List<MoveWarehouseDetailDO> getNotFinishedMoveWarehouseDetailList();
}