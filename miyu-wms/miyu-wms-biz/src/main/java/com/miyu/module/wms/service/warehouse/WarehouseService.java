package com.miyu.module.wms.service.warehouse;

import java.util.*;
import javax.validation.*;
import com.miyu.module.wms.controller.admin.warehouse.vo.*;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 仓库表 Service 接口
 *
 * @author Qianjy
 */
public interface WarehouseService {

    /**
     * 创建仓库表
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarehouse(@Valid WarehouseSaveReqVO createReqVO);

    /**
     * 更新仓库表
     *
     * @param updateReqVO 更新信息
     */
    void updateWarehouse(@Valid WarehouseSaveReqVO updateReqVO);

    /**
     * 删除仓库表
     *
     * @param id 编号
     */
    void deleteWarehouse(String id);

    /**
     * 获得仓库表
     *
     * @param id 编号
     * @return 仓库表
     */
    WarehouseDO getWarehouse(String id);

    List<WarehouseDO> getWarehouseByIds(Collection<String> ids);


    /**
     * 获得仓库表列表
     *
     * @param ids 编号集合
     * @return 仓库表列表
     */
    List<WarehouseDO> getWarehouseList(Collection<String> ids);

    /**
     * 获得仓库表Map
     *
     * @param ids 编号集合
     * @return 仓库表Map
     */
    Map<String, WarehouseDO> getWarehouseMap(Collection<String> ids);
    /**
     * 获得仓库表分页
     *
     * @param pageReqVO 分页查询
     * @return 仓库表分页
     */
    PageResult<WarehouseDO> getWarehousePage(WarehousePageReqVO pageReqVO);

    List<WarehouseDO> getWarehouseList();

    /**
     * 根据物料库存id 查询物料所在仓库
     * @param materialStockId 物料库存id
     */
    WarehouseDO getWarehouseByMaterialStockId(String materialStockId);
    /**
     * 根据库位id 查询库位所在仓库
     * @param locationId 库位id
     */
    WarehouseDO getWarehouseByLocationId(String locationId);

    /**
     * 仓库编码 查询仓库
     * @param code
     * @return
     */
    List<WarehouseDO> getWarehouseByCode(String code);

    /**
     * 根据仓库类型 查询仓库
     * 启用的仓库
     *
     * @param warehouseType
     * @return
     */
    List<WarehouseDO> getWarehouseByTypeS(Collection<Integer> warehouseType);
}
