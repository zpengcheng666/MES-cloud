package com.miyu.module.wms.service.materialstorage;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.wms.controller.admin.materialstorage.vo.MaterialStoragePageReqVO;
import com.miyu.module.wms.controller.admin.materialstorage.vo.MaterialStorageSaveReqVO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 物料储位 Service 接口
 *
 * @author QianJy
 */
public interface MaterialStorageService {

    /**
     * 创建物料储位
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMaterialStorage(@Valid MaterialStorageSaveReqVO createReqVO);

    /**
     * 更新物料储位
     *
     * @param updateReqVO 更新信息
     */
    void updateMaterialStorage(@Valid MaterialStorageSaveReqVO updateReqVO);

    /**
     * 删除物料储位
     *
     * @param id 编号
     */
    void deleteMaterialStorage(String id);

    /**
     * 获得物料储位
     *
     * @param id 编号
     * @return 物料储位
     */
    MaterialStorageDO getMaterialStorage(String id);

    /**
     * 获得物料储位分页
     *
     * @param pageReqVO 分页查询
     * @return 物料储位分页
     */
    PageResult<MaterialStorageDO> getMaterialStoragePage(MaterialStoragePageReqVO pageReqVO);


    List<MaterialStorageDO> getMaterialStorageList();

    /**
     * 批量插入物料储位
     *
     * @param materialStock 物料库存DO
     * @return 是否成功
     */
    Boolean createBatchMaterialStorage(MaterialStockDO materialStock);

    /**
     * 根据物料库存ID删除物料储位
     *
     * @param materialStockId 物料库存ID
     * @return 删除数量
     */
    int deleteByMaterialStockId(String materialStockId);


    /**
     * 根据托盘ids 查询所有被占用的储位集合
     */
    List<MaterialStorageDO> getOccupyMaterialStockListByTrayIds(List<String> trayIds);

    /**
     * 根据托盘ids 查询所有被占用的储位集合 包含物料信息
     * @param trayIds
     * @return
     */
    List<MaterialStorageDO> getDetailMaterialStockListByTrayIds(List<String> trayIds);


    /**
     * 根据容器库存id 查询所有未被占用的储位集合
     */
    List<MaterialStorageDO> getFreeMaterialStockListByTrayId(String id);

    /**
     * 获取储位列表 根据容器库存ID
     */
    List<MaterialStorageDO> getMaterialStorageListByContainerStockId(String containerStockId);

    List<MaterialStorageDO> getAllMaterialStorageListByContainerStockId(String containerStockId);

    /**
     * 获取储位列表 根据容器库存IDs
     */
    List<MaterialStorageDO> getMaterialStorageListByContainerStockIds(List<String> containerStockIds);

    /**
     * 根据库位编码获取物料储位
     *
     * @param storageCode
     * @return
     */
    MaterialStorageDO getMaterialStorageByStorageCode(String storageCode);

    /**
     * 根据库位返回 所在库位上的托盘储位id
     *
     * @param locationId
     * @return
     */
    String getStorageIdByLocationId(String locationId);

    /**
     * 根据物料库存id 和 储位号 获取物料储位id
     * @param materialStockId
     * @param site
     * @return
     */
    String getStorageIdByMaterialStockIdAndSite(String materialStockId, int site);

}
