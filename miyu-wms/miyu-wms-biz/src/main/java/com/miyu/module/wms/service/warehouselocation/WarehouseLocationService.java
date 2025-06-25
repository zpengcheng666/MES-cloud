package com.miyu.module.wms.service.warehouselocation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.controller.admin.warehouselocation.vo.WarehouseLocationPageReqVO;
import com.miyu.module.wms.controller.admin.warehouselocation.vo.WarehouseLocationSaveReqVO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 库位 Service 接口
 *
 * @author QianJy
 */
public interface WarehouseLocationService {

    /**
     * 创建库位
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarehouseLocation(@Valid WarehouseLocationSaveReqVO createReqVO);

    /**
     * 更新库位
     *
     * @param updateReqVO 更新信息
     */
    void updateWarehouseLocation(@Valid WarehouseLocationSaveReqVO updateReqVO);

    /**
     * 删除库位
     *
     * @param id 编号
     */
    void deleteWarehouseLocation(String id);

    /**
     * 获得库位
     *
     * @param id 编号
     * @return 库位
     */
    WarehouseLocationDO getWarehouseLocation(String id);

    List<WarehouseLocationDO> getWarehouseLocationListByIds(Collection<String> locationIds);

    /**
     * 获得库位分页
     *
     * @param pageReqVO 分页查询
     * @return 库位分页
     */
    PageResult<WarehouseLocationDO> getWarehouseLocationPage(WarehouseLocationPageReqVO pageReqVO);

    /**
     * 批量创建库位
     *
     * @param warehouseArea 库区信息
     * @return 是否成功
     */
    Boolean createBatchWarehouseLocation(WarehouseAreaDO warehouseArea);

    /**
     * 批量删除库位
     *
     * @param warehouseAreaId 库区编号
     * @return 被删除的库位数量
     */
    int deleteByWarehouseAreaId(String warehouseAreaId);


    List<WarehouseLocationDO> getWarehouseLocationList();

    /******************************************************************************************************************/
    /*                                               新增内容                                                          */
    /******************************************************************************************************************/

    /**
     * 根据库位id解锁库位
     */
    boolean unlockLocation(String locationId);

    /**
     * 根据库位id锁定库位
     */
    boolean lockLocation(String locationId);

    /**
     * 根据库区ids 获取此库区集合下的所有库位
     *
     * @param areaIds
     * @return
     */
    List<WarehouseLocationDO> getAvailableLocationListByAreaIds(Collection<String> areaIds);

    /**
     * 根据库区ids 获取此库区集合下的所有库位   有效的 未锁定的 -- 目前锁定仅用来锁定接驳库位上的托盘，放置托盘使用时被呼叫走
     *
     * @param areaIds
     * @return
     */
    List<WarehouseLocationDO> getAvailableNoLockedLocationListByAreaIds(List<String> areaIds);


    /**
     * 获取一个 存储库位 用来上架
     *
     * @param areaIds         所有可用的库区集合
     * @param startLocationId 上架的起始库位id
     * @return
     */
    WarehouseLocationDO getAvailableStorageLocationId(List<String> areaIds, String startLocationId);


    /**
     * 根据库区ids 获取此库区集合下的所有空库位 有效 未锁定
     *
     * @param areaIds
     */
    List<WarehouseLocationDO> getEmptyLocationByAreaIds(Collection<String> areaIds);

    /**
     * 所有 在自动或半自动库区的接驳位 库位  有效 未锁定
     *
     * @return
     */
    List<WarehouseLocationDO> getAvailableTransitLocation();

    List<WarehouseLocationDO> getWarehouseLocationByAreaCode(WarehouseAreaDO warehouseAreaDO);

    /**
     * 根据库位编码获取库位信息
     *
     * @param locationCode
     * @return
     */
    WarehouseLocationDO getWarehouseLocationByLocationCode(String locationCode);

    List<WarehouseLocationDO> getWarehouseLocationByLocationCodeS(Collection<String> locationCodeS);

    List<WarehouseLocationDO> getWarehouseLocationByAreaId(String areaId);

    /**
     * 通过仓库id获得库位
     *
     * @param warehouseId 仓库id
     * @return
     */
    List<WarehouseLocationDO> getLocationListByWarehouseId(String warehouseId);

    WarehouseLocationDO getWarehouseLocationAndAreaTypeById(String locationId);

    /**
     * 检查库位是否锁定
     * @param joinLocationId
     * @return
     */
    boolean checkLocationLock(String joinLocationId);

    /**
     * 根据物料库存 获取器所在库位信息 atLocationId
     * @param materialStockDOS
     * @return
     */
    List<MaterialStockRespDTO> getWarehouseLocationByMaterialStockList(List<MaterialStockDO> materialStockDOS) ;

    /**
     * 根据仓库类型获取库位信息
     * @param warehouseType
     * @return
     */
    List<WarehouseLocationDO> getWarehouseLocationByWarehouseType(Integer warehouseType);
}
