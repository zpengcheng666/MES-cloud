package com.miyu.module.wms.api.material;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.*;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.framework.generator.seivice.ICodeGeneratorService;
import com.miyu.module.wms.service.alarm.AlarmService;
import com.miyu.module.wms.service.materialconfig.MaterialConfigService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
@Transactional(rollbackFor = Exception.class)
public class MaterialStockApiImpl implements MaterialStockApi {

    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private MaterialConfigService materialConfigService;
    @Resource
    @Lazy
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private AlarmService alarmService;
    @Resource
    private ICodeGeneratorService codeGeneratorService;

    @Override
    public CommonResult<List<MaterialStockRespDTO>> getOutOrderMaterialsByConfigIds(Collection<String> materialConfigIds) {
        if(CollectionUtils.isAnyEmpty(materialConfigIds)){
            return CommonResult.success(Collections.emptyList());
        }

        List<MaterialStockDO> materialStockList = materialStockService.getOrderMaterialStockListByMaterialConfigIds(materialConfigIds,()-> warehouseAreaService.getOutWarehouseOrderSelectAreaList());

        return CommonResult.success(BeanUtils.toBean(materialStockList,MaterialStockRespDTO.class));
    }

    @Override
    public CommonResult<List<MaterialStockRespDTO>> getMoveOrderMaterialsByConfigIds(Collection<String> materialConfigIds) {
        if(CollectionUtils.isAnyEmpty(materialConfigIds)){
            return CommonResult.success(Collections.emptyList());
        }

        List<MaterialStockDO> materialStockList = materialStockService.getOrderMaterialStockListByMaterialConfigIds(materialConfigIds,()-> warehouseAreaService.getMoveWarehouseOrderSelectAreaList());

        return CommonResult.success(BeanUtils.toBean(materialStockList,MaterialStockRespDTO.class));
    }


    @Override
    public CommonResult<List<MaterialStockRespDTO>> getInOrderMaterialsByConfigIds(Collection<String> materialConfigIds) {
        if(CollectionUtils.isAnyEmpty(materialConfigIds)){
            return CommonResult.success(Collections.emptyList());
        }

        List<MaterialStockDO> materialStockList = materialStockService.getOrderMaterialStockListByMaterialConfigIds(materialConfigIds,()-> warehouseAreaService.getInWarehouseOrderSelectAreaList());

        return CommonResult.success(BeanUtils.toBean(materialStockList,MaterialStockRespDTO.class));
    }

    @Override
    public CommonResult<List<MaterialStockRespDTO>> getMaterialsByConfigIds(Collection<String> materialConfigIds) {
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByMaterialConfigIds(materialConfigIds);
        return CommonResult.success(BeanUtils.toBean(materialStockList,MaterialStockRespDTO.class));
    }


    @Override
    public CommonResult<List<MaterialStockLocationTypeDTO>> getMaterialsAndLocationInfoByConfigIds(Collection<String> materialConfigIds) {
        if(CollectionUtils.isAnyEmpty(materialConfigIds)){
            return CommonResult.success(Collections.emptyList());
        }

        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByMaterialConfigIds(materialConfigIds);

        return materialStockService.getMaterialStockLocationTypeByMaterialStockList(materialStockList);
    }



    /**
     * 物料编码查库存
     * @param barCode
     * @return
     */
    @Override
    public CommonResult<List<MaterialStockRespDTO>> getMaterialsByBarCode(String barCode) {
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(Collections.singletonList(barCode));
        return CommonResult.success(BeanUtils.toBean(materialStockList,MaterialStockRespDTO.class));
    }

    @Override
    public CommonResult<MaterialStockRespDTO> getMaterialAtLocationByBarCode(String barCode) {
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(Collections.singletonList(barCode));
        if(materialStockList.size() == 1){
            MaterialStockDO materialStockDO = materialStockList.get(0);
            String locationIdByMaterialStock = materialStockService.getLocationIdByMaterialStock(materialStockDO);
            MaterialStockRespDTO materialStockRespDTO = BeanUtils.toBean(materialStockDO, MaterialStockRespDTO.class);
            materialStockRespDTO.setAtLocationId(locationIdByMaterialStock);
            return CommonResult.success(materialStockRespDTO);
        }else {
            return CommonResult.error(MATERIAL_STOCK_NOT_EXISTS);
        }
    }

    @Override
    public CommonResult<List<MaterialStockRespDTO>> getMaterialAtLocationByBarCodes(Collection<String> barCodes) {
        if(CollectionUtils.isAnyEmpty(barCodes)){
            return CommonResult.success(Collections.emptyList());
        }
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(barCodes);

        return CommonResult.success(warehouseLocationService.getWarehouseLocationByMaterialStockList(materialStockList));
    }

    /**
     * 物料编码查库存
     * @param barCodes
     * @return
     */
    @Override
    public CommonResult<List<MaterialStockRespDTO>> getMaterialsByBarCodes(Collection<String> barCodes) {
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(barCodes);
        return CommonResult.success(BeanUtils.toBean(materialStockList,MaterialStockRespDTO.class));
    }


    @Override
    public CommonResult<MaterialStockRespDTO> getById(String id) {
        MaterialStockDO materialStock = materialStockService.getMaterialStock(id);
        return CommonResult.success(BeanUtils.toBean(materialStock,MaterialStockRespDTO.class));
    }

    @Override
    public CommonResult<List<MaterialStockRespDTO>> getMaterialsByLocationId(String locationId) {
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByLocationId(locationId);
        return CommonResult.success(BeanUtils.toBean(materialStockList, MaterialStockRespDTO.class));
    }

    //包含储位、库存信息
    @Override
    public CommonResult<List<MaterialStockRespDTO>> getMaterialsAndLocationByIds(Collection<String> ids) {
        List<MaterialStockDO> materialStockList =  materialStockService.getMaterialsAndLocationByIds(ids);
        return CommonResult.success(BeanUtils.toBean(materialStockList, MaterialStockRespDTO.class));
    }

    @Override
    public CommonResult<List<MaterialStockRespDTO>> getMaterialsAndConfigByIds(Collection<String> ids) {
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialsAndConfigByIds(ids);
        return CommonResult.success(BeanUtils.toBean(materialStockList, MaterialStockRespDTO.class));
    }

    @Override
    public CommonResult<List<MaterialStockRespDTO>> getMaterialsByIds(Collection<String> ids) {
        List<MaterialStockDO> materialStockList =  materialStockService.getMaterialsByIds(ids);
        return CommonResult.success(BeanUtils.toBean(materialStockList, MaterialStockRespDTO.class));
    }

    @Override
    public CommonResult<MaterialStockRespDTO> getMaterialGZByBarCode(String barCode) {
        MaterialStockDO materialStock = materialStockService.getMaterialStockByBarCode(barCode);
        if(materialStock == null){
            alarmService.createSystemErrorAlarm(MATERIAL_STOCK_NOT_EXISTS,"物料条码："+barCode);
            return null;
        }
        if(StringUtils.isNotBlank(materialStock.getStorageId())){
            MaterialStockDO containerStock = materialStockService.getMaterialStockByStorageId(materialStock.getStorageId());
            if(containerStock == null){
                alarmService.createSystemErrorAlarm(MATERIAL_STOCK_NOT_EXISTS,"物料条码："+barCode);
                return null;
            }
            return CommonResult.success(BeanUtils.toBean(containerStock,MaterialStockRespDTO.class));
        }
        alarmService.createSystemErrorAlarm(MATERIAL_STOCK_NOT_CONTAINER_MATERIAL_CANNOT_BIND_BIN,"物料条码："+barCode);
        return null;
    }

    /**
     * 物料拣选   不校验出入库任务单
     * @param materialStockId 被拣选的物料库存id
     * @param pickQuantity 拣选数量
     * @param locationId 拣选到的库位id
     * @param storageId 拣选到的储位id
     * @return
     */
    @Override
    public CommonResult<MaterialStockRespDTO> materialPicking(String materialStockId, int pickQuantity,String locationId,String storageId){
        MaterialStockDO materialStockDO = materialStockService.materialPicking(materialStockId, pickQuantity, locationId, storageId);
        return CommonResult.success(BeanUtils.toBean(materialStockDO,MaterialStockRespDTO.class));
    }

    /**
     * 物料拣选  校验出入库任务单
     * @param materialStockId 被拣选的物料库存id
     * @param locationId 拣选到的库位id
     * @param storageId 拣选到的储位id
     * @return  返回拣选后新生成的物料库存信息（如果拣选数量与库存一致，将会返回旧的物料信息）
     */
    @Override
    public CommonResult<MaterialStockRespDTO> verifyMaterialPicking(String orderId, String materialStockId, String locationId, String storageId) {
        MaterialStockDO materialStockDOS = materialStockService.verifyMaterialPicking(orderId, materialStockId, locationId, storageId);
        return CommonResult.success(BeanUtils.toBean(materialStockDOS,MaterialStockRespDTO.class));
    }

    @Override
    public CommonResult<Boolean> updateMaterialStockConfig(String materialStockId, String materialConfigId) {
        return CommonResult.success(materialStockService.updateMaterialStockConfig(materialStockId, materialConfigId));
    }

    @Override
    public CommonResult<List<MaterialStockRespDTO>> receiveMaterial(List<ReceiveMaterialReqDTO> receiveMaterialReqDTOList) {
        Set<String> materialConfigSet = new HashSet<>();
        Set<String> barCodeSet = new HashSet<>();
        Map<String, String> batchNumberMap = new HashMap<>();
        for (ReceiveMaterialReqDTO receiveMaterialReqDTO : receiveMaterialReqDTOList) {
            materialConfigSet.add(receiveMaterialReqDTO.getMaterialConfigId());
            if(StringUtils.isNotBlank(receiveMaterialReqDTO.getBarCode())){
                barCodeSet.add(receiveMaterialReqDTO.getBarCode());
            }
        }
        // 获取物料类型集合
        List<MaterialConfigDO> materialConfigList = materialConfigService.getMaterialConfigListByIds(materialConfigSet);
        Map<String, MaterialConfigDO> stringMaterialConfigDOMap = CollectionUtils.convertMap(materialConfigList, MaterialConfigDO::getId);

        // 获取物料库存集合
        List<MaterialStockDO> allMaterialStockList = materialStockService.getAllMaterialStockListByBarCodes(barCodeSet);
        Map<String, MaterialStockDO> materialStockDOMap = CollectionUtils.convertMap(allMaterialStockList, MaterialStockDO::getBarCode);

        List<MaterialStockDO> materialStockList = new ArrayList<>();
        for (ReceiveMaterialReqDTO receiveMaterialReqDTO : receiveMaterialReqDTOList) {
            MaterialStockDO materialStock = new MaterialStockDO();
            if(StringUtils.isNotBlank(receiveMaterialReqDTO.getBarCode())
                    && materialStockDOMap.containsKey(receiveMaterialReqDTO.getBarCode())){
                materialStock = materialStockDOMap.get(receiveMaterialReqDTO.getBarCode());
                if(materialStock.getIsExists()){
                    throw exception(MATERIAL_STOCK_ALREADY_IN_WAREHOUSE);
                }
            }
            if(batchNumberMap.containsKey(receiveMaterialReqDTO.getMaterialConfigId())){
                batchNumberMap.put(receiveMaterialReqDTO.getMaterialConfigId(),codeGeneratorService.getMaterialBatchNumber());
            }
            materialStock.setBatchNumber(batchNumberMap.get(receiveMaterialReqDTO.getMaterialConfigId()));
            materialStock.setMaterialConfigId(receiveMaterialReqDTO.getMaterialConfigId());
            materialStock.setLocationId(receiveMaterialReqDTO.getLocationId());
            materialStock.setIsExists(true);
            materialStock.setTotality(Integer.parseInt(stringMaterialConfigDOMap.get(receiveMaterialReqDTO.getMaterialConfigId()).getMaterialSpecification()));
            materialStock.setMaterialStatus(receiveMaterialReqDTO.getMaterialStatus());
            materialStockService.saveHandle(materialStock);
            materialStockList.add(materialStock);
        }
        Boolean b = materialStockService.insertOrUpdateBatch(materialStockList);
        if(!b){
            throw exception(MATERIAL_STOCK_TAKE_DELIVERY_ERROR);
        }
        return CommonResult.success(BeanUtils.toBean(materialStockList,MaterialStockRespDTO.class));
    }

    @Override
    public CommonResult<Boolean> sendMaterial(Collection<String> barCodes) {
        // 获取物料库存集合
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(barCodes);
        materialStockList.forEach(materialStockDO -> {
            materialStockDO.setIsExists(false);
            materialStockDO.setLocationId("");
            materialStockDO.setStorageId("");
        });
        Boolean b = materialStockService.insertOrUpdateBatch(materialStockList);
        if(!b){
            throw exception(MATERIAL_STOCK_SEND_DELIVERY_ERROR);
        }
        return CommonResult.success(true);
    }

    @Override
    public CommonResult<Boolean> updateQualityCheckStatus(List<MaterialQualityCheckStatus> materialQualityCheckStatusList) {
        Map<String, Integer> barCodeAndStatusMap = CollectionUtils.convertMap(materialQualityCheckStatusList, MaterialQualityCheckStatus::getBarCode, MaterialQualityCheckStatus::getMaterialStatus);
        List<MaterialStockDO> allMaterialStockList = materialStockService.getAllMaterialStockListByBarCodes(barCodeAndStatusMap.keySet());
        if(allMaterialStockList.size() != materialQualityCheckStatusList.size()){
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }
        for (MaterialStockDO materialStockDO : allMaterialStockList) {
            materialStockDO.setMaterialStatus(barCodeAndStatusMap.get(materialStockDO.getBarCode()));
        }
        Boolean b = materialStockService.insertOrUpdateBatch(allMaterialStockList);
        return CommonResult.success(b);
    }


    @Override
    public CommonResult<List<MaterialStockLocationTypeDTO>> getMaterialStockLocationTypeByMaterialStockList(List<MaterialStockRespDTO> materialStockList) {
        return materialStockService.getMaterialStockLocationTypeByMaterialStockList(BeanUtils.toBean(materialStockList,MaterialStockDO.class));
    }

    @Override
    public CommonResult<String> generateOrDisassembleProductTool(ProductToolReqDTO productToolReqDTO) {
        String materialStockId = materialStockService.generateProductTool(productToolReqDTO);
        return CommonResult.success(materialStockId);
    }

    @Override
    public CommonResult<Boolean> assembleOrRecoveryMaterial(List<AssembleToolReqDTO> assembleToolReqDTOList) {
        Boolean b = materialStockService.assembleOrRecoveryMaterial(assembleToolReqDTOList);
        return CommonResult.success(b);
    }

}
