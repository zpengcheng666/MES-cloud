package com.miyu.module.wms.service.materialstock;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.wms.api.mateiral.dto.AssembleToolReqDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockLocationTypeDTO;
import com.miyu.module.wms.api.mateiral.dto.ProductToolReqDTO;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockPageReqVO;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockSaveReqVO;
import com.miyu.module.wms.controller.admin.takedelivery.vo.TakeDeliverySaveReqVO;
import com.miyu.module.wms.dal.dataobject.checkdetail.CheckDetailDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import org.jetbrains.annotations.NotNull;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 物料库存 Service 接口
 *
 * @author Qianjy
 */
public interface MaterialStockService {

    /*
     * 通过仓库获取库区为存储区的容器(托盘或工装)物料库存
     */
    List<MaterialStockDO> getMaterialStockListByWarehouseIdAndAreaTypeEqContainer(String warehouseId);

    /**
     * 创建物料库存
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMaterialStock(@Valid MaterialStockSaveReqVO createReqVO);

    /**
     * 更新物料库存
     *
     * @param updateReqVO 更新信息
     */
    void updateMaterialStock(@Valid MaterialStockSaveReqVO updateReqVO);

    /**
     * 删除物料库存
     *
     * @param id 编号
     */
    void deleteMaterialStock(String id);

    /**
     * 获得物料库存
     *
     * @param id 编号
     * @return 物料库存
     */
    MaterialStockDO getMaterialStock(String id);


    /**
     * 物料拣选   不校验出入库任务单
     *
     * @param materialStockId 被拣选的物料库存id
     * @param pickQuantity    拣选数量
     * @param locationId      拣选到的库位id
     * @param storageId       拣选到的储位id
     * @return
     */
    MaterialStockDO materialPicking(String materialStockId, int pickQuantity, String locationId, String storageId);

    MaterialStockDO materialPicking(MaterialStockDO materialStock, int pickQuantity, String locationId, String storageId);

    // 校验出入库任务单
    MaterialStockDO verifyMaterialPicking(String orderId, String materialStockId, String locationId, String storageId);

    // 校验出入库任务单
    MaterialStockDO verifyMaterialPicking(String orderId, MaterialStockDO materialStock, String locationId, String storageId);

    /**
     * 根据物料库存id 查询物料库存
     *
     * @param ids
     * @return
     */
    List<MaterialStockDO> getMaterialStockByIds(Collection<String> ids);

    /**
     * 根据物料库存id 查询物料库存 包括已经逻辑删除的
     *
     * @param ids
     * @return
     */
    List<MaterialStockDO> getMaterialStockIncludeDeletedByIds(Collection<String> ids);


    /**
     * 获得物料库存分页
     *
     * @param pageReqVO 分页查询
     * @return 物料库存分页
     */
    PageResult<MaterialStockDO> getMaterialStockPage(MaterialStockPageReqVO pageReqVO);

    List<MaterialStockDO> getMaterialStockList();

    List<MaterialStockDO> getExistsMaterialConfigStockList();

    int insert(MaterialStockDO entity);

    int updateById(MaterialStockDO entity);

    void saveHandle(@NotNull MaterialStockDO entity);

    void LogRecordMessage(MaterialStockDO entity);

    /**
     * 收货 生成物料库存信息
     *
     * @param createReqVO 收货信息
     * @return 编号
     */
    String createMaterialStock(TakeDeliverySaveReqVO createReqVO);

    Boolean insertOrUpdateBatch(Collection<MaterialStockDO> materialStockDOS);

    /**
     * 获得物料库存列表 物料类型 是否为容器
     *
     * @param isContainer
     * @return
     */
    List<MaterialStockDO> getMaterialStockContainerList(Integer isContainer);

    /**
     * 获得绑定在此容器上的所有物料  -- 不包含此物料
     *
     * @param materialStockId 容器物料id
     * @return 物料类型 是否为容器
     */
    List<MaterialStockDO> getAllMaterialStockListByMaterialStockId(String materialStockId);


    /**
     * 仅获得绑定在此容器上的物料 包含物料所在储位编码  -- 刀具专用
     *
     * @param tray 托盘id
     * @return 物料类型 是否为容器
     */
    List<MaterialStockDO> getMaterialStockListIncludingStorageByTrayId(String tray);

    /**
     * 仅获得绑定在此容器上的物料
     *
     * @param materialStockId 容器物料id
     * @return 物料类型 是否为容器
     */
    List<MaterialStockDO> getMaterialStockListByContainerId(String materialStockId);

    List<MaterialStockDO> getMaterialStockListByContainerIds(Collection<String> materialStockIds);


    /**
     * 获得绑定在此库位上的物料库存列表
     *
     * @param locationId
     * @return
     */
    List<MaterialStockDO> getMaterialStockListByLocationId(String locationId);

    List<MaterialStockDO> getMaterialStockListByLocationIds(Collection<String> locationIds);

    Map<String, List<MaterialStockDO>> getRootLocationIdMaterialStockMap();

    /**
     * 根据库位查询库存 仅返回库存信息
     * @param locationIds
     * @return
     */
    List<MaterialStockDO> getSimpleMaterialStockListByLocationIds(Collection<String> locationIds);


    /**
     * 获得绑定在此库位上的物料库存列表 返回值携带物料类型 是否为容器
     *
     * @param locationId
     * @return
     */
    List<MaterialStockDO> getMaterialStockListAndContainerByLocationId(String locationId);

    /**
     * 获取库位上的 物料库存
     *
     * @param locationId
     * @return
     */
    List<MaterialStockDO> getMaterialStockByLocationId(String locationId);


    boolean updateMaterialStock(String id, String locationId);

    Boolean updateBatchMaterialStock(Collection<String> ids, String locationId);

    boolean updateMaterialStorage(String id, String storageId);

    /**
     * 根据托盘id 查询非容器类物料库存集合  改版 ：废弃托盘上的全查，不用再把工装刨除去了
     *
     * @param containerStockId
     * @return
     */
//    List<MaterialStockDO> getNonContainerMaterialStockListByMaterialStockContainerId(String containerStockId);

    /**
     * 根据托盘上的物料库存集合 获得所有非容器类物料库存集合  改版 ：废弃托盘上的全查，不用再把工装刨除去了
     * 1.先查出托盘上的物料库存
     * 2.如果为空 代表是空托盘回库 直接返回null
     * 3.如果不为空 那他上边可能不为空啊 那就得先把他上边的东西拿出来 （1.全为非容器类物料2.全为容器类物料（工装）3.有容器类物料，也有非容器类物料，容器类物料上还有非物料（工装上的物料））
     * 4.托盘上的 非容器类物料
     * 5.托盘上的 容器类物料id
     * 6.找出托盘上的 容器类物料 和 非容器类物料 区分开来    -- 剩下的全是工装 ，但是工装上边可能还有物料 还得继续找
     * 7.容器类物料继续找他上边的 非容器类物料
     *
     * @param materialStockListOnContainer
     * @return
     */
//    List<MaterialStockDO> getNonContainerMaterialStockListByMaterialStockContainer(List<MaterialStockDO> materialStockListOnContainer);

    /**
     * 判定托盘上是否绑定其他物料  todo：此方法将会被循环调用 待优化  但这个可能影响不大，还好
     *
     * @param containerId 容器物料id
     * @return
     */
    boolean hasNonContainerMaterial(String containerId);

    /**
     * 根据库位id 判断库位是否为空 todo：此方法将会被循环调用 可能得优化
     *
     * @param locationId
     * @return
     */
    boolean checkLocationIsVacant(String locationId);

    /**
     * 根据储位id 查询储位所属的容器类物料
     *
     * @param storageId
     * @return
     */
    MaterialStockDO getMaterialStockByStorageId(String storageId);

    /**
     * 根据储位ids 查询储位所属的容器类物料
     *
     * @param storageIds
     * @return 返回容器物料被绑定的储位id
     */
    List<MaterialStockDO> getMaterialStockListByStorageIds(Collection<String> storageIds);

    /**
     * 根据物料 遍历 物料所在的库位id
     *
     * @param materialStock
     * @return
     */
    String getLocationIdByMaterialStock(MaterialStockDO materialStock);

    /**
     * 获取物料所在库位上的容器类物料
     *
     * @param materialStock
     * @return
     */
    MaterialStockDO getContainerStockByMaterialStock(MaterialStockDO materialStock) throws ServiceException;

    /**
     * 获取在库位上的物料
     *
     * @param materialStock
     * @return
     */
    MaterialStockDO getMaterialAtLocationByMaterialStock(MaterialStockDO materialStock);

    MaterialStockDO getContainerStockByLocationId(String locationId);

    String getLocationIdByMaterialStockId(String materialStockId);

    /**
     * 根据物料id 查询物料库存 包含是否为 、容器字段、物料类型、库区类型
     *
     * @param materialStockId
     * @return
     */
    MaterialStockDO getMaterialStockById(String materialStockId);

    /**
     * 根据物料id 查询物料库存 包含物料类型字段
     *
     * @param materialStockId
     * @return
     */
    MaterialStockDO getMaterialStockAndMaterialTypeById(String materialStockId);

    /**
     * 根据物料类型ids 查询物料库存列表
     *
     * @param materialConfigIds
     * @return
     */
    List<MaterialStockDO> getMaterialStockListByMaterialConfigIds(Collection<String> materialConfigIds);

    /**
     * 根据物料类型ids 查询物料库存列表  总库存大于0
     *
     * @param materialConfigIds
     * @return
     */
    List<MaterialStockDO> getMaterialStockListByMaterialConfigIdsAndLockedZero(Collection<String> materialConfigIds);


    /**
     * 根据物料ids 查询物料库存 在 自动化库区 空托盘 物料库存列表
     *
     * @param trayIds
     * @return
     */
    List<MaterialStockDO> getFreeTrayStockListInAutoStockAreaByMaterialStockIds(List<String> trayIds);

    /**
     * 根据托盘类型 查询所有空托盘  所在库位 有效 未锁定 所在库区 自动化存储区 或 接驳区
     *
     * @param trayConfigIds
     * @param callAreaId 呼叫库区id       在此库区的空托盘也过滤掉
     * @return 返回托盘所在仓库 和 所在库区类型
     */
    List<MaterialStockDO> getEmptyTrayStockListByMaterialConfigIds(Collection<String> trayConfigIds, String callAreaId);

    /**
     * 根据物料ids 查询物料库存 包含库位信息
     *
     * @param materialStockIds
     * @return
     */
    List<MaterialStockDO> getMaterialStockListAndLocationByIds(List<String> materialStockIds);


    /**
     * 根据物料类型ids 获取可用于创建 出库单或入库单或移库单 的物料库存列表
     *
     * @param materialConfigIds
     * @param canOutWarehouseAreaSupplier 可用于创建订单的 库区集合
     * @return
     */
    List<MaterialStockDO> getOrderMaterialStockListByMaterialConfigIds(Collection<String> materialConfigIds, Supplier<List<WarehouseAreaDO>> canOutWarehouseAreaSupplier);

    /**
     * 根据物料 查询所在库位  如果物料绑定在容器上 就再往下 查询容器所在库位
     *
     * @param materialStockList
     * @return 要查询的物料id 承载物料在库位上的的物料库存（一般为，托盘或者工装，也可能物料本身就在库位上）
     */
    Map<String, MaterialStockDO> getWarehouseLocationListByMaterialStockList(List<MaterialStockDO> materialStockList);


    /**
     * 根据物料ids 查询存在订单单据的物料库存列表
     *
     * @param materialStockIdSet
     * @return
     */
    List<MaterialStockDO> checkMaterialStockOrderExists(Collection<String> materialStockIdSet);


    /**
     * 根据物料ids 查询存在订单单据的被选择的物料库存列表
     *
     * @param materialStockIdSet
     * @return
     */
    List<MaterialStockDO> checkChooseStockOrderExists(Collection<String> materialStockIdSet);

    /**
     * 根据储位code 查询物料库存
     *
     * @param storageCode
     * @return
     */
    MaterialStockDO getMaterialStorageByStorageCode(String storageCode);

    /**
     * 根据库位id 查询其上所有物料库存
     *
     * @param locationId
     * @return
     */
    List<MaterialStockDO> getAllMaterialStockByLocationId(String locationId);

    /**
     * 物料编码查库存
     *
     * @param barCode
     * @return
     */
    List<MaterialStockDO> getMaterialStockListByBarCodes(Collection<String> barCode);

    /**
     * 物料编码查库存
     *
     * @param barCode
     * @return
     */
    MaterialStockDO getMaterialStockByBarCode(String barCode);

    /**
     * 根据物料条码查询物料库存  -- 包含已出库的物料
     *
     * @param barCodes
     * @return
     */
    List<MaterialStockDO> getAllMaterialStockListByBarCodes(Collection<String> barCodes);

    /**
     * 根据库区id 和 物料类型ids 查询物料库存列表
     *
     * @param checkAreaId
     * @param strings
     * @return
     */
    List<MaterialStockDO> getMaterialStockListByCheckAreaIdAndMaterialConfigIds(String checkAreaId, List<String> strings);

    /**
     * 盘盈 增加物料库存
     *
     * @param materialStockDO
     * @param checkDetailDO
     */
    void increaseMaterialStock(MaterialStockDO materialStockDO, CheckDetailDO checkDetailDO);

    /**
     * 根据盘点容器id 查询物料库存
     *
     * @param checkContainerId
     * @return
     */
    MaterialStockDO getMaterialStockByCheckContainerId(String checkContainerId);

    void decreaseMaterialStockt(MaterialStockDO materialStockDO, CheckDetailDO checkDetailDO);


    /**
     * 校验物料是否在锁盘范围内
     *
     * @param materialStockDO
     * @return
     */
    boolean validateMaterialInLocked(MaterialStockDO materialStockDO, String... warehouseIds);

    boolean validateMaterialInLocked(String materialStockId, String... warehouseIds);

    /**
     * 根据物料id 和 物料类型id 更新物料库存的物料类型id
     *
     * @param materialStockId
     * @param materialConfigId
     * @return
     */
    Boolean updateMaterialStockConfig(String materialStockId, String materialConfigId);

    /**
     * 根据物料集合 获取 所在库区和仓库信息
     */
    CommonResult<List<MaterialStockLocationTypeDTO>> getMaterialStockLocationTypeByMaterialStockList(List<MaterialStockDO> materialStockList);


    /**
     * 根据库位编码查询物料库存
     *
     * @param locationCode
     * @return
     */
    List<MaterialStockDO> getMaterialStockByLocationCode(String locationCode);

    /**
     * 根据物料id和其实体对照表获取  其所在库位上的 容器物料  （原始物料id和容器物料实体对照表）
     *
     * @param materialIdAndMaterialStockDOMap
     * @param result
     */
    void getMaterialStockByStorageIds(Map<String, MaterialStockDO> materialIdAndMaterialStockDOMap,Map<String, MaterialStockDO> result);


    /**
     * 获取物料来源id 被分拣时使用
     * @param materialStock
     * @return
     */
    String getSourceIdByMaterialStock(MaterialStockDO materialStock);


    PageResult<MaterialStockDO> getMaterialStockAtStorageAreaByMaterialNumber(MaterialStockPageReqVO pageReqVO);

    /**
     * 根据物料id 查询物料库存 包含 不存在的物料
     */
    MaterialStockDO getMaterialStockIncludeNotExistById(String id);

    // 包含储位库位信息
    List<MaterialStockDO> getMaterialsAndLocationByIds(Collection<String> ids);

    List<MaterialStockDO> getMaterialsByIds(Collection<String> ids);

    // 包含物料类型信息
    List<MaterialStockDO> getMaterialsAndConfigByIds(Collection<String> ids);

    /**
     * 生成或拆卸成品刀
     * 装配或恢复原料刀
     * @param assembleToolReqDTOList
     * @return
     */
    Boolean assembleOrRecoveryMaterial(List<AssembleToolReqDTO> assembleToolReqDTOList);

    /**
     * 创建成品刀具
     * @param productToolReqDTO
     * @return
     */
    String generateProductTool(ProductToolReqDTO productToolReqDTO);
}
