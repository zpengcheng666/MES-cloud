package com.miyu.module.wms.service.warehouselocation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.controller.admin.warehouselocation.vo.WarehouseLocationPageReqVO;
import com.miyu.module.wms.controller.admin.warehouselocation.vo.WarehouseLocationSaveReqVO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.dal.mysql.warehouselocation.WarehouseLocationMapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.framework.generator.seivice.ICodeGeneratorService;
import com.miyu.module.wms.service.instruction.InstructionService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 库位 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class WarehouseLocationServiceImpl implements WarehouseLocationService {

    @Resource
    private WarehouseLocationMapper warehouseLocationMapper;
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private ICodeGeneratorService codeGeneratorService;
    @Resource
    @Lazy
    private InstructionService instructionService;
    @Resource
    private MaterialStockService materialStockService;

    @Override
    public String createWarehouseLocation(WarehouseLocationSaveReqVO createReqVO) {
        // 插入
        WarehouseLocationDO warehouseLocation = BeanUtils.toBean(createReqVO, WarehouseLocationDO.class);
        warehouseLocationMapper.insert(warehouseLocation);
        // 返回
        return warehouseLocation.getId();
    }

    @Override
    public void updateWarehouseLocation(WarehouseLocationSaveReqVO updateReqVO) {
        // 校验存在
        validateWarehouseLocationExists(updateReqVO.getId());
        // 更新
        WarehouseLocationDO updateObj = BeanUtils.toBean(updateReqVO, WarehouseLocationDO.class);
        warehouseLocationMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarehouseLocation(String id) {
        // 校验存在
        validateWarehouseLocationExists(id);
        // 删除
        warehouseLocationMapper.deleteById(id);
    }

    private void validateWarehouseLocationExists(String id) {
        if (warehouseLocationMapper.selectById(id) == null) {
            throw exception(WAREHOUSE_LOCATION_NOT_EXISTS);
        }
    }

    @Override
    public WarehouseLocationDO getWarehouseLocation(String id) {
        return warehouseLocationMapper.selectById(id);
    }

    @Override
    public List<WarehouseLocationDO> getWarehouseLocationListByIds(Collection<String> locationIds) {
        if (CollectionUtils.isAnyEmpty(locationIds)) {
            return Collections.emptyList();
        }
        return warehouseLocationMapper.selectWarehouseLocationListByIds(locationIds);
    }

    @Override
    public PageResult<WarehouseLocationDO> getWarehouseLocationPage(WarehouseLocationPageReqVO pageReqVO) {
        return warehouseLocationMapper.selectPage(pageReqVO);
    }

    @Override
    @CacheEvict(value= "locationList#1h", key = "'getWarehouseLocationList'")
    public Boolean createBatchWarehouseLocation(WarehouseAreaDO warehouseArea) {
        /*List<WarehouseLocationDO> list = this.getListBydefaultWarehouseId(warehouseArea.getId());
        if(!CollectionUtils.isEmpty(list)){throw exception(WAREHOUSE_LOCATION_IS_EXISTS);}*/
        WarehouseDO warehouse = warehouseService.getWarehouse(warehouseArea.getWarehouseId());
        Map<String, Object> codeMap = codeGeneratorService.generateCodes("#|#|-C|-G|-L|-S",
                "#|#|-通道|-组|-层|-位",
                new String[]{warehouse.getWarehouseCode(), warehouseArea.getAreaCode()},
                new String[]{warehouse.getWarehouseName(), warehouseArea.getAreaName()},
                new Integer[]{warehouseArea.getAreaChannels(), warehouseArea.getAreaGroup(), warehouseArea.getAreaLayer(), warehouseArea.getAreaSite()});
        List<String> codes = (List<String>) codeMap.get("codes");
        List<String> names = (List<String>) codeMap.get("names");
        List<Integer[]> numbers = (List<Integer[]>) codeMap.get("numbers");
        List<WarehouseLocationDO> warehouseLocations = new ArrayList<>();
        for (int i = 0; i < codes.size(); i++) {
            WarehouseLocationDO warehouseLocationDO = new WarehouseLocationDO();
            warehouseLocationDO.setLocationCode(codes.get(i));
            warehouseLocationDO.setLocationName(names.get(i));

            warehouseLocationDO.setChannel(numbers.get(i)[0]);
            warehouseLocationDO.setGroupp(numbers.get(i)[1]);
            warehouseLocationDO.setLayer(numbers.get(i)[2]);
            warehouseLocationDO.setSite(numbers.get(i)[3]);

            warehouseLocationDO.setWarehouseAreaId(warehouseArea.getId());
            warehouseLocationDO.setLocked(DictConstants.INFRA_BOOLEAN_TINYINT_NO);
            warehouseLocationDO.setValid(DictConstants.INFRA_BOOLEAN_TINYINT_YES);
            warehouseLocations.add(warehouseLocationDO);
        }
        return warehouseLocationMapper.insertBatch(warehouseLocations);
    }

    @Override
    public int deleteByWarehouseAreaId(String warehouseAreaId) {
        return warehouseLocationMapper.delete(Wrappers.lambdaUpdate(WarehouseLocationDO.class).eq(WarehouseLocationDO::getWarehouseAreaId, warehouseAreaId));
    }

    @Override
    @Cacheable(value= "locationList#1h", key = "'getWarehouseLocationList'", unless = "#result == null")
    public List<WarehouseLocationDO> getWarehouseLocationList() {
        return warehouseLocationMapper.selectList();
    }


    /*                                               新增内容                                                          */
    /******************************************************************************************************************/


    @Override
    public boolean unlockLocation(String locationId) {
        return warehouseLocationMapper.update(new LambdaUpdateWrapper<WarehouseLocationDO>()
                .set(WarehouseLocationDO::getLocked, DictConstants.INFRA_BOOLEAN_TINYINT_NO)
                .eq(WarehouseLocationDO::getId, locationId)) > 0;
    }

    @Override
    public boolean lockLocation(String locationId) {
        return warehouseLocationMapper.update(new LambdaUpdateWrapper<WarehouseLocationDO>()
                .set(WarehouseLocationDO::getLocked, DictConstants.INFRA_BOOLEAN_TINYINT_YES)
                .eq(WarehouseLocationDO::getId, locationId)) > 0;
    }


    @Override
    public List<WarehouseLocationDO> getAvailableLocationListByAreaIds(Collection<String> areaIds) {
        return warehouseLocationMapper.selectAvailableLocationListByAreaIds(areaIds);
    }


    @Override
    public List<WarehouseLocationDO> getAvailableNoLockedLocationListByAreaIds(List<String> areaIds) {
        return warehouseLocationMapper.selectAvailableNoLockedLocationListByAreaIds(areaIds);
    }



    /**
     * 获取一个 存储库位 用来上架 这个校验存不存在指令就可以了 不用管搬运任务了
     *
     * @param areaIds  所有可用的库区集合
     * @param startLocationId  上架的起始库位id
     * @return
     */
    @Override
    public WarehouseLocationDO getAvailableStorageLocationId(List<String> areaIds, String startLocationId) {

        // 获取此库区集合下的所有库位
        List<WarehouseLocationDO> availableAGVLocationList = this.getAvailableNoLockedLocationListByAreaIds(areaIds);
        // 获得库位上无物料的库位
        List<WarehouseLocationDO> emptyLocationList = availableAGVLocationList.stream().filter(availableAGVLocation -> !materialStockService.checkLocationIsVacant(availableAGVLocation.getId())).collect(Collectors.toList());

        if (emptyLocationList.isEmpty()) {
            throw exception(CARRYING_TASK_MATERIAL_NOT_FOUND_CARRYING_AREA);
        }

        for (WarehouseLocationDO startLocation : emptyLocationList) {
            // 判断是否有 未完成的指令
            if (instructionService.hasUnfinishedInstruction(startLocation.getId(), startLocationId)) {
                continue;
            }
            // 找到就 return
            return startLocation;
        }
        // 找不到就抛异常
        throw exception(CARRYING_TASK_TRAY_NOT_FOUND);
    }


    @Override
    public List<WarehouseLocationDO> getEmptyLocationByAreaIds(Collection<String> areaIds) {
        // 先获取所有有效 未锁定的库位
        List<WarehouseLocationDO> warehouseLocationList = this.getAvailableLocationListByAreaIds(areaIds);
        if(CollectionUtils.isAnyEmpty(warehouseLocationList)){
            return Collections.emptyList();

        }
        // 在根据库位去查询库存
        List<String> locationIds = warehouseLocationList.stream().map(WarehouseLocationDO::getId).collect(Collectors.toList());
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByLocationIds(locationIds);
        Set<String> materialStockSet = CollectionUtils.convertSet(materialStockList, MaterialStockDO::getLocationId);
        // 过滤掉库位上有库存的库位
        return warehouseLocationList.stream().filter(l -> !materialStockSet.contains(l.getId())).collect(Collectors.toList());
    }

    @Override
    public List<WarehouseLocationDO> getAvailableTransitLocation() {
        return warehouseLocationMapper.selectAvailableTransitLocation();
    }

    @Override
    public List<WarehouseLocationDO> getWarehouseLocationByAreaCode(WarehouseAreaDO warehouseAreaDO) {
        List<WarehouseLocationDO> list = warehouseLocationMapper.selectList(new LambdaQueryWrapperX<WarehouseLocationDO>().eq(WarehouseLocationDO::getWarehouseAreaId, warehouseAreaDO.getId()));
        return list;
    }

    @Override
    public WarehouseLocationDO getWarehouseLocationByLocationCode(String locationCode) {
        return warehouseLocationMapper.selectOne(new LambdaQueryWrapperX<WarehouseLocationDO>().eq(WarehouseLocationDO::getLocationCode, locationCode));
    }

    @Override
    public List<WarehouseLocationDO> getWarehouseLocationByLocationCodeS(Collection<String> locationCodeS) {
        return warehouseLocationMapper.selectList(new LambdaQueryWrapperX<WarehouseLocationDO>().in(WarehouseLocationDO::getLocationCode, locationCodeS));
    }

    @Override
    public List<WarehouseLocationDO> getWarehouseLocationByAreaId(String areaId) {
        return warehouseLocationMapper.selectList(WarehouseLocationDO::getWarehouseAreaId, areaId);
    }

    @Override
    public List<WarehouseLocationDO> getLocationListByWarehouseId(String warehouseId) {
        return warehouseLocationMapper.getLocationListByWarehouseId(warehouseId);
    }

    @Override
    public WarehouseLocationDO getWarehouseLocationAndAreaTypeById(String locationId) {
        return warehouseLocationMapper.getWarehouseLocationAndAreaTypeById(locationId);
    }

    @Override
    public boolean checkLocationLock(String locationId) {
        WarehouseLocationDO warehouseLocation = this.getWarehouseLocation(locationId);
        return DictConstants.INFRA_BOOLEAN_TINYINT_YES.equals(warehouseLocation.getLocked());
    }

    @Override
    public List<MaterialStockRespDTO> getWarehouseLocationByMaterialStockList(List<MaterialStockDO> materialStockDOS) {
        Map<String, MaterialStockDO> materialStockDOMap = CollectionUtils.convertMap(materialStockDOS, MaterialStockDO::getId);
        Map<String, MaterialStockDO> sourcdeMaterialIdAndContainerMaterialDOMap = new HashMap<>();
        materialStockService.getMaterialStockByStorageIds(materialStockDOMap, sourcdeMaterialIdAndContainerMaterialDOMap);

        if(sourcdeMaterialIdAndContainerMaterialDOMap.isEmpty() ){
            return Collections.emptyList();
        }

        List<MaterialStockRespDTO> materialStockRespDTOS = BeanUtils.toBean(materialStockDOS, MaterialStockRespDTO.class);
        materialStockRespDTOS.forEach(materialStockRespDTO -> {
            if(sourcdeMaterialIdAndContainerMaterialDOMap.containsKey(materialStockRespDTO.getId())){
                materialStockRespDTO.setAtLocationId(sourcdeMaterialIdAndContainerMaterialDOMap.get(materialStockRespDTO.getId()).getLocationId());
            }
        });

        return materialStockRespDTOS;
    }

    @Override
    public List<WarehouseLocationDO> getWarehouseLocationByWarehouseType(Integer warehouseType) {
        return warehouseLocationMapper.getWarehouseLocationByWarehouseType(warehouseType);
    }
}
