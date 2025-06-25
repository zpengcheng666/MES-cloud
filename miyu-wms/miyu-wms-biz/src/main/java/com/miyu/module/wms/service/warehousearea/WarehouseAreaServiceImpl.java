package com.miyu.module.wms.service.warehousearea;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.wms.controller.admin.warehousearea.vo.WarehouseAreaPageReqVO;
import com.miyu.module.wms.controller.admin.warehousearea.vo.WarehouseAreaSaveReqVO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.dal.mysql.warehousearea.WarehouseAreaMapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 库区 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class WarehouseAreaServiceImpl implements WarehouseAreaService {

    @Resource
    private WarehouseAreaMapper warehouseAreaMapper;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private MaterialStockService materialStockService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createWarehouseArea(WarehouseAreaSaveReqVO createReqVO) {
        // 插入
        WarehouseAreaDO warehouseArea = BeanUtils.toBean(createReqVO, WarehouseAreaDO.class);
        warehouseAreaMapper.insert(warehouseArea);
        warehouseLocationService.createBatchWarehouseLocation(warehouseArea);
        // 返回
        return warehouseArea.getId();
    }

    @Override
    public void updateWarehouseArea(WarehouseAreaSaveReqVO updateReqVO) {
        // 校验存在
        validateWarehouseAreaExists(updateReqVO.getId());
        // 更新
        WarehouseAreaDO updateObj = BeanUtils.toBean(updateReqVO, WarehouseAreaDO.class);
        warehouseAreaMapper.updateById(updateObj);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteWarehouseArea(String id) {
        // 校验存在
        validateWarehouseAreaExists(id);
        warehouseLocationService.deleteByWarehouseAreaId(id);
        // 删除
        warehouseAreaMapper.deleteById(id);
    }

    private void validateWarehouseAreaExists(String id) {
        if (warehouseAreaMapper.selectById(id) == null) {
            throw exception(WAREHOUSE_AREA_NOT_EXISTS);
        }
    }

    @Override
    public WarehouseAreaDO getWarehouseArea(String id) {
        return warehouseAreaMapper.selectById(id);
    }

    /**
     * 获得库区列表
     *
     * @param ids 编号集合
     * @return
     */
    @Override
    public List<WarehouseAreaDO> getWarehouseAreaList(Collection<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return warehouseAreaMapper.selectBatchIds(ids);
    }

    /**
     * 获得库区Map
     *
     * @param ids 编号集合
     * @return
     */
    @Override
    public Map<String, WarehouseAreaDO> getWarehouseAreaMap(Collection<String> ids) {
        List<WarehouseAreaDO> warehouseAreaDO = getWarehouseAreaList(ids);
        return CollectionUtils.convertMap(warehouseAreaDO, WarehouseAreaDO::getId);
    }

    @Override
    public List<WarehouseAreaDO> getWarehouseAreaList() {
        return warehouseAreaMapper.selectList();
    }

    @Override
    public PageResult<WarehouseAreaDO> getWarehouseAreaPage(WarehouseAreaPageReqVO pageReqVO) {
        return warehouseAreaMapper.selectPage(pageReqVO);
    }

    @Override
    public WarehouseAreaDO getWarehouseAreaByLocationId(String locationId) {
        return warehouseAreaMapper.getWarehouseAreaByLocationId(locationId);
    }

    @Override
    public WarehouseAreaDO getWarehouseAreaByLocationCode(String locationCode) {
        return warehouseAreaMapper.getWarehouseAreaByLocationCode(locationCode);
    }

    @Override
    public List<WarehouseAreaDO> getWarehouseAreaByLocationIds(Collection<String> locationIds) {
        return warehouseAreaMapper.getWarehouseAreaByLocationIds(locationIds);
    }

    /**
     * 通过物料id获取其所在库位的库区实体
     *
     * @param materialStockId 物料id
     * @return
     */
    @Override
    public WarehouseAreaDO  getWarehouseAreaByMaterialStockId(String materialStockId) {
        return warehouseAreaMapper.selectByMaterialStockId(materialStockId);
    }

    @Override
    public List<WarehouseAreaDO> getWarehouseAreaListByMaterialStockIds(Collection<String> materialStockIds) {
        if(CollectionUtils.isAnyEmpty(materialStockIds)){
            return Collections.emptyList();
        }
        return warehouseAreaMapper.selectByMaterialStockIds(materialStockIds);
    }

    @Override
    public List<WarehouseAreaDO> getWarehouseAreaInWarehouseByAreaIdForCall(String areaId) {
        return warehouseAreaMapper.selectWarehouseAreaInWarehouseByAreaIdForCall(areaId);
    }

    @Override
    public List<WarehouseAreaDO> getSelectableWarehouseAreaList(Collection<Integer> warehouseTypes, Collection<Integer> areaTypes) {
        return warehouseAreaMapper.selectSelectableWarehouseAreaList(warehouseTypes, areaTypes);
    }

    @Override
    public List<WarehouseAreaDO> getOutWarehouseOrderSelectAreaList() {
        return this.getSelectableWarehouseAreaList(Arrays.asList(DictConstants.WMS_WAREHOUSE_TYPE_1,DictConstants.WMS_WAREHOUSE_TYPE_2),
                Arrays.asList(DictConstants.WMS_WAREHOUSE_AREA_TYPE_1));
    }

    @Override
    public List<WarehouseAreaDO> getMoveWarehouseOrderSelectAreaList() {
        return this.getSelectableWarehouseAreaList(Arrays.asList(DictConstants.WMS_WAREHOUSE_TYPE_3),
                Arrays.asList(DictConstants.WMS_WAREHOUSE_AREA_TYPE_3));
    }

    @Override
    public List<WarehouseAreaDO> getInWarehouseOrderSelectAreaList() {
        return this.getSelectableWarehouseAreaList(Arrays.asList(DictConstants.WMS_WAREHOUSE_TYPE_1,DictConstants.WMS_WAREHOUSE_TYPE_2,DictConstants.WMS_WAREHOUSE_TYPE_3),
                Arrays.asList(DictConstants.WMS_WAREHOUSE_AREA_TYPE_3));
    }

    @Override
    public WarehouseAreaDO getWarehouseAreaByMaterialStock(MaterialStockDO materialStockDO) {
        String materialLocationId = materialStockDO.getLocationId();
        String materialStorageId = materialStockDO.getStorageId();
        if(StringUtils.isBlank(materialLocationId)){
            if(StringUtils.isBlank(materialStorageId)){
                throw exception(MATERIAL_STOCK_NOT_BIND_POSITION);
            }
            return this.getWarehouseAreaByMaterialStock(materialStockService.getMaterialStockByStorageId(materialStorageId));
        }
        return warehouseAreaMapper.getWarehouseAreaByLocationId(materialLocationId);
    }

    @Override
    public Map<String,WarehouseAreaDO> getWarehouseAreaByMaterialStockList(List<MaterialStockDO> materialStockDOS) {
        Map<String, MaterialStockDO> materialStockDOMap = CollectionUtils.convertMap(materialStockDOS, MaterialStockDO::getId);
        Map<String, MaterialStockDO> sourcdeMaterialIdAndContainerMaterialDOMap = new HashMap<>();
        materialStockService.getMaterialStockByStorageIds(materialStockDOMap, sourcdeMaterialIdAndContainerMaterialDOMap);

        if(sourcdeMaterialIdAndContainerMaterialDOMap.isEmpty() ){
            return Collections.emptyMap();
        }

        Map<String, String> locationIds = new HashMap<>();
        sourcdeMaterialIdAndContainerMaterialDOMap.forEach((sourceMaterialId,containerMaterialDO)->{
            locationIds.put(sourceMaterialId,containerMaterialDO.getLocationId());
        });

        // 根据物料的库位id获取所有库位
        List<WarehouseLocationDO> warehouseLocationDOS = warehouseLocationService.getWarehouseLocationListByIds(locationIds.values());
        // 库位id 和 库区id 映射
        Map<String, String> LocationIdAndAreaId = CollectionUtils.convertMap(warehouseLocationDOS, WarehouseLocationDO::getId, WarehouseLocationDO::getWarehouseAreaId);
        // 获取所有库区
        List<WarehouseAreaDO> warehouseAreaDOS = this.getWarehouseAreaByIds(LocationIdAndAreaId.values());
        // 库区id 和 库区实体映射
        Map<String, WarehouseAreaDO> warehouseAreaDOMap = CollectionUtils.convertMap(warehouseAreaDOS, WarehouseAreaDO::getId);

        Map<String,WarehouseAreaDO> result = new HashMap<>();
        // 遍历库位的库区id集合 将 物料id 与库区实体 映射
        locationIds.forEach((sourceMaterialId,locationId)->{
            if(warehouseAreaDOMap.containsKey(LocationIdAndAreaId.get(locationId))){
                result.put(sourceMaterialId, warehouseAreaDOMap.get(LocationIdAndAreaId.get(locationId)));
            }
        });
        return result;
    }

    @Override
    public List<WarehouseAreaDO> getWarehouseAreaByIds(Collection<String> warehouseAreaIds) {
        return warehouseAreaMapper.selectList(WarehouseAreaDO::getId,warehouseAreaIds);
    }

    @Override
    public WarehouseAreaDO getWarehouseAreaByAreaCode(String areaCode) {
        return warehouseAreaMapper.selectOne(new LambdaQueryWrapper<WarehouseAreaDO>().eq(WarehouseAreaDO::getAreaCode,areaCode));
    }

    @Override
    public List<WarehouseAreaDO> getWarehouseAreaByWarehouseId(String warehouseId) {
        return warehouseAreaMapper.selectList(new LambdaQueryWrapper<WarehouseAreaDO>().eq(WarehouseAreaDO::getWarehouseId,warehouseId));
    }

    @Override
    public List<WarehouseAreaDO> getWarehouseAreaByWarehouseIdAndAreaTypes(String warehouseId, Collection<String> areaTypes) {
        return warehouseAreaMapper.selectWarehouseAreaByWarehouseIdAndAreaTypes(warehouseId,areaTypes);
    }


}