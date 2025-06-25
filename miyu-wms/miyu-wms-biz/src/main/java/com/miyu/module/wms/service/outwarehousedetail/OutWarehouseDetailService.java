package com.miyu.module.wms.service.outwarehousedetail;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.api.order.dto.OrderUpdateDTO;
import com.miyu.module.wms.api.order.dto.ProductionOrderRespDTO;
import com.miyu.module.wms.controller.admin.outwarehousedetail.vo.*;
import com.miyu.module.wms.dal.dataobject.checkdetail.CheckDetailDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 出库详情 Service 接口
 *
 * @author Qianjy
 */
public interface OutWarehouseDetailService {

    /**
     * 创建出库详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createOutWarehouseDetail(@Valid OutWarehouseDetailSaveReqVO createReqVO);

    /**
     * 更新出库详情
     *
     * @param updateReqVO 更新信息
     */
    void updateOutWarehouseDetail(@Valid OutWarehouseDetailSaveReqVO updateReqVO);

    /**
     * 删除出库详情
     *
     * @param id 编号
     */
    void deleteOutWarehouseDetail(String id);

    /**
     * 获得出库详情
     *
     * @param id 编号
     * @return 出库详情
     */
    OutWarehouseDetailDO getOutWarehouseDetail(String id);

    /**
     * 获得出库详情分页
     *
     * @param pageReqVO 分页查询
     * @return 出库详情分页
     */
    PageResult<OutWarehouseDetailDO> getOutWarehouseDetailPage(OutWarehouseDetailPageReqVO pageReqVO);

    List<OutWarehouseDetailDO> getOutWarehouseDetailList();

    /**
     * 根据订单号获取出库详情列表
     * @param orderNumber
     * @return
     */
    List<OutWarehouseDetailDO> getOutWarehouseDetailListByOrderNumber(String orderNumber, Integer outState);

    /**
     * 获取出库详情单 出库单号列表
     * @return
     */
    List<String> getOutWarehouseDetailGroupByOrderNumberList(Integer outState);

    /**
     * 根据容器物料id 查询其上所有物料的-待出库的 出库详情单
     */
    List<OutWarehouseDetailDO> getOutWarehouseDetailListByMaterialStockContainer(List<MaterialStockDO> materialStockListOnContainer);

    /**
     * 查询-待出库的出库详情单
     * @return
     */
    List<OutWarehouseDetailDO> getWaitOutWarehouseOutWarehouseDetailList();


    /**
     * 根据物料id 查询-审批的出库详情单
     * @param materialStockIds 物料ids
     * @return 入库详情单
     */
    List<OutWarehouseDetailDO> getWaitApprovalOutWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds);

    /**
     * 根据物料id 查询-待出库的出库详情单
     * @param materialStockIds 物料ids
     * @return 入库详情单
     */
    List<OutWarehouseDetailDO> getWaitOutWarehouseOutWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds);

    /**
     * 根据物料id 查询-待送达的出库详情单
     * @param materialStockIds 物料ids
     * @return 入库详情单
     */
    List<OutWarehouseDetailDO> getWaitArriveOutWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds);

    /**
     * 根据物料id 查询-待签收的出库详情单  --待签收  ---包括待出库的
     * @param materialStockIds 物料ids
     * @return 入库详情单
     */
    List<OutWarehouseDetailDO> getWaitSignForOutWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds);


    /**
     * 根据物料id批量更新出库单状态
     * @param materialStockId
     * @param updateState
     * @return
     */
    boolean updateBatchOutWarehouseDetailStateByMaterialStockId(@NotEmpty String materialStockId, @NotNull Integer updateState);


    boolean updateBatchOutWarehouseDetail(OrderUpdateDTO orderUpdateDTO);

    /**
     * 校验出库详情单
     * @param outWarehouseId
     * @param allMaterialStockMap
     */
    List<OutWarehouseDetailDO> checkOutWarehouseDetail(String outWarehouseId, Map<String, MaterialStockDO> allMaterialStockMap);

    /**出库详情单*/
    List<OutWarehouseDetailDO> selectWaitOutWarehouseDetail(String warehouseId);

    List<String> createBatchOutWarehouseDetail(List<OrderReqDTO> orderReqDTOList);

    /**
     * 校验物料库存是否存在出库详情  并插入
     * @param outWarehouseDetailDOS
     * @return 失败的物料库存ids
     */
    List<String> checkStockAndCreateOutDetail(List<OutWarehouseDetailDO> outWarehouseDetailDOS);

    /**
     * 根据出库单号获取出库单
     * @param outOrderNumbers
     * @return
     */
    List<OrderReqDTO> getBatchOutOrderList(List<String> outOrderNumbers);

    /**
     * 根据条码和订单号查询出库详情
     * @param orderNumber
     * @param barCodes
     * @return
     */
    List<OutWarehouseDetailDO> checkMaterialStockOrderExistsByBarCodes(String orderNumber, Collection<String> barCodes);

    /**
     * 根据单号 填入操作人
     * @param orderIds
     * @return
     */
    int setOperatorInBatchOutWarehouseDetail(Set<String> orderIds);

    /**
     * 根据批次号获取出库单
     * @param orderType
     * @param batchNumber
     * @return
     */
    List<OrderReqDTO> getOutOrderListByOrderTypeAndBatchNumber(Integer orderType, String batchNumber);

    int updateById(OutWarehouseDetailDO outWarehouseDetail);

    /**
     * 生产盘亏出库单记录
     * @param lossTotality
     * @param materialStockDO
     * @param warehouseId
     * @param operator
     * @param operateTime
     * @return
     */
    int createLossOutWarehouseDetail(int lossTotality, MaterialStockDO materialStockDO, String warehouseId, String operator, LocalDateTime operateTime);

    /**
     * 获取未完成的出库单
     * @return
     */
    List<OutWarehouseDetailDO> getNotFinishedOutWarehouseDetailListByAreaId(String areaId);

    Boolean updateBatch(Collection<OutWarehouseDetailDO> entities);

    /**
     * 呼叫物料
     * @param callMaterialStockId
     * @param callLocationId
     */
    void callMaterial(String callMaterialStockId, String callLocationId);

    /**
     * 尝试生成出库搬运任务 和 出库详情单
     * @param materialStock
     * @param targetLocationId
     * @param startWarehouseId
     * @param targetWarehouseId
     * @param quantity
     * @param realMaterialStock
     */
    String tryGenerateOutWarehouseDetail(MaterialStockDO materialStock, String targetLocationId,String startWarehouseId, String targetWarehouseId, Integer quantity, MaterialStockDO realMaterialStock);


    String generateOutWarehouseDetail(Integer outState, String startWarehouseId, String targetWarehouseId, String targetLocationId, Integer quantity, MaterialStockDO realMaterialStock);

    List<OrderReqDTO> getOutWarehouseDetailByChooseBarCode(String barCode);

    /**
     * 生成刀具配送任务
     * @param containerStock
     */
    void generateDistributionTask(MaterialStockDO containerStock);

    List<String> getDistributionPath(MaterialStockDO containerStock);

    /**
     * 获取已完成的出库单
     * @return
     */
    List<OutWarehouseDetailDO> getFinishOutWarehouseDetailList(LocalDateTime[] createTimeRange);

    /**
     * 获取未完成的出库单
     * @return
     */
    List<OutWarehouseDetailDO> getNotFinishedOutWarehouseDetailList();
}
