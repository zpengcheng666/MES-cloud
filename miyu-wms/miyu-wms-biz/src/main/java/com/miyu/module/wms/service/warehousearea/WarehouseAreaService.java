package com.miyu.module.wms.service.warehousearea;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.wms.controller.admin.warehousearea.vo.WarehouseAreaPageReqVO;
import com.miyu.module.wms.controller.admin.warehousearea.vo.WarehouseAreaSaveReqVO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 库区 Service 接口
 *
 * @author QianJy
 */
public interface WarehouseAreaService {

    /**
     * 创建库区
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarehouseArea(@Valid WarehouseAreaSaveReqVO createReqVO);

    /**
     * 更新库区
     *
     * @param updateReqVO 更新信息
     */
    void updateWarehouseArea(@Valid WarehouseAreaSaveReqVO updateReqVO);

    /**
     * 删除库区
     *
     * @param id 编号
     */
    void deleteWarehouseArea(String id);

    /**
     * 获得库区
     *
     * @param id 编号
     * @return 库区
     */
    WarehouseAreaDO getWarehouseArea(String id);

    /**
     * 获得仓库表列表
     *
     * @param ids 编号集合
     * @return 仓库表列表
     */
    List<WarehouseAreaDO> getWarehouseAreaList(Collection<String> ids);

    /**
     * 获得仓库表Map
     *
     * @param ids 编号集合
     * @return 仓库表Map
     */
    Map<String, WarehouseAreaDO> getWarehouseAreaMap(Collection<String> ids);


    List<WarehouseAreaDO> getWarehouseAreaList();

    /**
     * 获得库区分页
     *
     * @param pageReqVO 分页查询
     * @return 库区分页
     */
    PageResult<WarehouseAreaDO> getWarehouseAreaPage(WarehouseAreaPageReqVO pageReqVO);


    /**
     * 根据库位id 查询库区
     *
     * @param locationId 库位id
     * @return 库区
     */
    WarehouseAreaDO getWarehouseAreaByLocationId(String locationId);
    WarehouseAreaDO getWarehouseAreaByLocationCode(String locationCode);

    /**
     * 根据库位ids 查询库区
     *
     * @param locationIds 库位id
     * @return 库区
     */
    List<WarehouseAreaDO> getWarehouseAreaByLocationIds(Collection<String> locationIds);

    /**
     * 根据物料id 查询库区
     *
     * @param materialStockId 库位id
     * @return 库区
     */
    WarehouseAreaDO getWarehouseAreaByMaterialStockId(String materialStockId);

    List<WarehouseAreaDO> getWarehouseAreaListByMaterialStockIds(Collection<String> materialStockIds);

    /**
     * 根据根据库位id 查询库区所在仓库下的所有可用于AGV接驳的库区
     *
     * @param areaId 库区id
     * @return 库区
     */
    List<WarehouseAreaDO> getWarehouseAreaInWarehouseByAreaIdForCall(String areaId);

    List<WarehouseAreaDO> getSelectableWarehouseAreaList(Collection<Integer> warehouseTypes, Collection<Integer> areaTypes);
    /**
     * 获得可用于出库的库区列表
     * @return
     */
    List<WarehouseAreaDO> getOutWarehouseOrderSelectAreaList();
    /**
     * 获得可用于移库的库区列表
     * @return
     */
    List<WarehouseAreaDO> getMoveWarehouseOrderSelectAreaList();
    /**
     * 获得可用于入库的库区列表
     * @return
     */
    List<WarehouseAreaDO> getInWarehouseOrderSelectAreaList();

    /**
     * 根据物料库存查询库区  -- 物料不一定在库位上，所以需要传入物料库存对象
     * @param materialStockDO
     * @return
     */
    WarehouseAreaDO getWarehouseAreaByMaterialStock(MaterialStockDO materialStockDO);

    /**
     * 根据物料库存列表查询库区  -- 物料不一定在库位上，所以需要传入物料库存对象列表
     * @param materialStockDOS
     * @return
     */
    Map<String,WarehouseAreaDO> getWarehouseAreaByMaterialStockList(List<MaterialStockDO> materialStockDOS);

    List<WarehouseAreaDO> getWarehouseAreaByIds(Collection<String> warehouseAreaIds);

    WarehouseAreaDO getWarehouseAreaByAreaCode(String areaCode);

    /**
     * 根据仓库id查询库区
     * @param warehouseId
     * @return
     */
    List<WarehouseAreaDO> getWarehouseAreaByWarehouseId(String warehouseId);

    /**
     * 根据仓库id和库区类型查询库区
     * @param warehouseId
     * @param areaTypes
     * @return
     */
    List<WarehouseAreaDO> getWarehouseAreaByWarehouseIdAndAreaTypes(String warehouseId, Collection<String> areaTypes);
}