package com.miyu.module.wms.service.materialconfigarea;

import java.util.*;
import javax.validation.*;
import com.miyu.module.wms.controller.admin.materialconfigarea.vo.*;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 物料类型关联库区配置 Service 接口
 *
 * @author QianJy
 */
public interface MaterialConfigAreaService {

    /**
     * 创建物料类型关联库区配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMaterialConfigArea(@Valid MaterialConfigAreaSaveReqVO createReqVO);

    /**
     * 更新物料类型关联库区配置
     *
     * @param updateReqVO 更新信息
     */
    void updateMaterialConfigArea(@Valid MaterialConfigAreaSaveReqVO updateReqVO);

    /**
     * 删除物料类型关联库区配置
     *
     * @param id 编号
     */
    void deleteMaterialConfigArea(String id);

    /**
     * 根据物料类型 和 库区id   获取物料类型关联库区配置
     * @param warehouseAreaId
     * @param materialConfigId
     * @return
     */
    MaterialConfigAreaDO getMaterialConfigAreaByWarehouseAreaIdAndMaterialConfigId(String warehouseAreaId, String materialConfigId);
    /**
     * 获得物料类型关联库区配置
     *
     * @param id 编号
     * @return 物料类型关联库区配置
     */
    MaterialConfigAreaDO getMaterialConfigArea(String id);

    /**
     * 获得物料类型关联库区配置分页
     *
     * @param pageReqVO 分页查询
     * @return 物料类型关联库区配置分页
     */
    PageResult<MaterialConfigAreaDO> getMaterialConfigAreaPage(MaterialConfigAreaPageReqVO pageReqVO);

    /**
     * 根据物料类型id和仓库id 获取物料类型关联--接驳库区配置   一个仓库下的接驳库区可能有多个 对应不同类型的托盘
     * @param materialConfigId
     * @param warehouseId
     * @return
     */
    List<MaterialConfigAreaDO> getMaterialConfigTransitAreaByMaterialConfigIdAndWarehouseId(String materialConfigId, String warehouseId);

    /**
     * 根据仓库id 获取物料类型关联--接驳库区配置   一个仓库下的接驳库区可能有多个 对应不同类型的托盘
     * @param warehouseId
     * @return
     */
    List<MaterialConfigAreaDO> getMaterialConfigTransitAreaByWarehouseId(String warehouseId);

    // 获取刀具的接驳库区配置
    List<MaterialConfigAreaDO> getMaterialConfigCutterTransitAreaByWarehouseId(String warehouseId);

    /**
     * 根据物料类型ids和仓库id 获取物料类型关联--接驳库区配置   一个仓库下的接驳库区可能有多个 对应不同类型的托盘
     * @param materialConfigIds
     * @param warehouseId
     * @return
     */
    List<MaterialConfigAreaDO> getMaterialConfigTransitAreaByMaterialConfigIdsAndWarehouseId(Collection<String> materialConfigIds, String warehouseId);

    /**
     * 根据物料类型id和仓库id 获取物料类型关联--存储库区配置
     * @param materialConfigId
     * @param warehouseId
     * @return
     */
    List<MaterialConfigAreaDO> getMaterialConfigStorageAreaByMaterialConfigIdAndWarehouseId(String materialConfigId, String warehouseId);

    /**
     * 根据物料类型id和库位id获取物料类型关联库区配置
     * @param materialConfigId
     * @param locationId
     * @return
     */
    MaterialConfigAreaDO getMaterialConfigAreaByMaterialConfigIdAndLocationId(String materialConfigId, String locationId);


    /**
     * 根据物料类型id和库区ids获取物料类型关联库区配置   一个仓库下的接驳库区可能有多个 对应不同类型的托盘
     * @param materialConfigId
     * @param areaIds
     * @return
     */
    List<MaterialConfigAreaDO> getMaterialConfigAreaByMaterialConfigIdAndAreaIds(String materialConfigId, Collection<String> areaIds);


    /**
     * 根据库区id 获取物料类型关联库区配置列表 物料类型为托盘的
     * @param areaId
     * @return
     */
    List<MaterialConfigAreaDO> getTrayMaterialConfigAreaByAreaId(String areaId);

    /**
     * 根据物料类型id 获取物料类型关联库区配置列表 物料类型为托盘的
     * @param materialConfigId
     * @return
     */
    List<MaterialConfigAreaDO> getTrayMaterialConfigAreaByMaterialConfigId(String materialConfigId);


    /**
     * 获取配置了 起始库区和目标库区的托盘类型ids
     * @param startAreaId
     * @param endAreaId
     * @param appointTrayConfigIds 指定的托盘类型ids
     * @return
     */
    List<String> getMaterialConfigAreaByStartAreaIdAndEndAreaId(String startAreaId, String endAreaId, Collection<String> appointTrayConfigIds);
}