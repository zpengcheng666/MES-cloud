package com.miyu.module.wms.service.materialstock;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.module.wms.api.mateiral.dto.AssembleToolReqDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockLocationTypeDTO;
import com.miyu.module.wms.api.mateiral.dto.ProductToolReqDTO;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockPageReqVO;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockSaveReqVO;
import com.miyu.module.wms.controller.admin.takedelivery.vo.TakeDeliverySaveReqVO;
import com.miyu.module.wms.dal.dataobject.checkdetail.CheckDetailDO;
import com.miyu.module.wms.dal.dataobject.checkplan.CheckPlanDO;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.dal.mysql.materialstock.MaterialStockMapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.framework.generator.seivice.ICodeGeneratorService;
import com.miyu.module.wms.service.checkplan.CheckPlanService;
import com.miyu.module.wms.service.inwarehousedetail.InWarehouseDetailService;
import com.miyu.module.wms.service.materialconfig.MaterialConfigService;
import com.miyu.module.wms.service.materialmaintenance.MaterialMaintenanceService;
import com.miyu.module.wms.service.materialstorage.MaterialStorageService;
import com.miyu.module.wms.service.movewarehousedetail.MoveWarehouseDetailService;
import com.miyu.module.wms.service.outwarehousedetail.OutWarehouseDetailService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;
import static com.miyu.module.wms.enums.LogRecordConstants.*;

/**
 * 物料库存 Service 实现类
 *
 * @author Qianjy
 */
@Service
@Validated
@Transactional(rollbackFor = Exception.class)
public class MaterialStockServiceImpl implements MaterialStockService {

    private static final Logger log = LoggerFactory.getLogger(MaterialStockServiceImpl.class);

    @Resource
    private MaterialStockMapper materialStockMapper;
    @Resource
    private MaterialStorageService materialStorageService;
    @Resource
    private MaterialConfigService materialConfigService;
    @Resource
    private MaterialMaintenanceService materialMaintenanceService;
    @Resource
    @Lazy
    private WarehouseLocationService warehouseLocationService;
    @Resource
    @Lazy
    private WarehouseAreaService warehouseAreaService;
    @Resource
    @Lazy
    private InWarehouseDetailService inWarehouseDetailService;
    @Resource
    @Lazy
    private OutWarehouseDetailService outWarehouseDetailService;
    @Resource
    @Lazy
    private MoveWarehouseDetailService moveWarehouseDetailService;
    @Resource
    @Lazy
    private WarehouseService warehouseService;
    @Resource
    @Lazy
    private CheckPlanService checkPlanService;
    @Resource
    private ICodeGeneratorService codeGeneratorService;

    @Override
    public List<MaterialStockDO> getMaterialStockListByWarehouseIdAndAreaTypeEqContainer(String warehouseId){
        return materialStockMapper.getMaterialStockListByWarehouseIdAndAreaTypeEqContainer(warehouseId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @LogRecord(type = WMS_MATERIAL_STOCK, subType = "{{#type}}", bizNo = "{{#entity.id}}",
            success = "{{#massage}}")
    public String createMaterialStock(MaterialStockSaveReqVO createReqVO) {
        // 插入
        MaterialStockDO materialStock = BeanUtils.toBean(createReqVO, MaterialStockDO.class);
        this.insert(materialStock);
        materialStorageService.createBatchMaterialStorage(materialStock);
        // 返回
        return materialStock.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @LogRecord(type = WMS_MATERIAL_STOCK, subType = "{{#type}}", bizNo = "{{#entity.id}}",
            success = "{{#massage}}")
    public void updateMaterialStock(MaterialStockSaveReqVO updateReqVO) {
        // 校验存在
        validateMaterialStockExists(updateReqVO.getId());
        // 更新
        MaterialStockDO updateObj = BeanUtils.toBean(updateReqVO, MaterialStockDO.class);

        this.updateById(updateObj);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteMaterialStock(String id) {
        // 校验存在
        validateMaterialStockExists(id);
        materialStorageService.deleteByMaterialStockId(id);
        // 删除
        materialStockMapper.deleteById(id);
    }

    private void validateMaterialStockExists(String id) {
        if (materialStockMapper.selectById(id) == null) {
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }
    }

    @Override
    public MaterialStockDO getMaterialStock(String id) {
        return materialStockMapper.selectById(id);
    }

    @Override
    public MaterialStockDO materialPicking(String materialStockId, int pickQuantity, String locationId, String storageId) {
        return materialPicking(materialStockMapper.selectById(materialStockId), pickQuantity,locationId, storageId);
    }

    @Override
    public MaterialStockDO verifyMaterialPicking(String orderId, String materialStockId, String locationId, String storageId) {
        return verifyMaterialPicking(orderId, materialStockMapper.selectById(materialStockId),locationId, storageId);
    }

    @Override
    public MaterialStockDO verifyMaterialPicking(String orderId, MaterialStockDO materialStock, String locationId, String storageId) {

        List<MaterialStockDO> materialStockList = this.checkChooseStockOrderExists(Collections.singletonList(materialStock.getId()));
        if(CollectionUtils.isAnyEmpty(materialStockList)){
            throw exception(MATERIAL_STOCK_NO_STOCK_IN_OUT_ORDER);
        }
        // 存在多条出入库单时 ，如果未指定订单号，则报错
        if(orderId == null && materialStockList.size() > 1){
            throw exception(MATERIAL_STOCK_MULTIPLE_STOCK_IN_OUT_ORDER);
        }

        // 被拣选的物料
        MaterialStockDO pickMaterialStockDO = materialStockList.get(0);
        if(orderId == null){
            orderId = pickMaterialStockDO.getOrderId();
        }

        // 要拣选的数量  等于出入库订单的出入库数量
        int pickQuantity = pickMaterialStockDO.getOrderQuantity();
        // 真实出入库的物料
        MaterialStockDO realMaterialStock = materialPicking(materialStock, pickQuantity, locationId, storageId);

        // 根据关联的单号id 查询详情单
        OutWarehouseDetailDO outWarehouseDetail = outWarehouseDetailService.getOutWarehouseDetail(orderId);
        if(outWarehouseDetail == null){
            InWarehouseDetailDO inWarehouseDetail = inWarehouseDetailService.getInWarehouseDetail(orderId);
            if(inWarehouseDetail == null){
                MoveWarehouseDetailDO moveWarehouseDetail = moveWarehouseDetailService.getMoveWarehouseDetail(orderId);
                if(moveWarehouseDetail == null){
                    log.error("此物料不存在库存出入库单,物料条码：{}",pickMaterialStockDO.getBarCode());
                    throw exception(MATERIAL_STOCK_NO_STOCK_IN_OUT_ORDER);
                }else {
                    // 填入真实出入库的物料id
                    moveWarehouseDetail.setMaterialStockId(realMaterialStock.getId());
                    moveWarehouseDetailService.updateById(moveWarehouseDetail);
                }
            }else {
                // 填入真实出入库的物料id
                inWarehouseDetail.setMaterialStockId(realMaterialStock.getId());
                inWarehouseDetailService.updateById(inWarehouseDetail);
            }
        }else {
            // 填入真实出入库的物料id
            outWarehouseDetail.setMaterialStockId(realMaterialStock.getId());
            outWarehouseDetailService.updateById(outWarehouseDetail);
        }

        return realMaterialStock;
    }

    @Override
    public MaterialStockDO materialPicking(MaterialStockDO materialStock, int pickQuantity, String locationId, String storageId) {
        if(materialStock == null || pickQuantity <= 0){
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }
        // 库存小于拣选数量
        if(materialStock.getTotality() < pickQuantity){
            throw exception(MATERIAL_STOCK_NOT_ENOUGH_STOCK);
        }else if(materialStock.getTotality() == pickQuantity){
            // 库存等于拣选数量
            if(StringUtils.isNotBlank(locationId)){
                materialStock.setLocationId(locationId);
            }else if(StringUtils.isNotBlank(storageId)){
                materialStock.setStorageId(storageId);
            }else {
                throw exception(MATERIAL_STOCK_LOCATION_OR_STORAGE_NOT_EXISTS);
            }
            this.updateById(materialStock);
            return materialStock;
        }else {
            // 库存大于 拣选数量  - 生成新的物料库存 -- 新的库存复制原库存信息，
            materialStock.setTotality(materialStock.getTotality() - pickQuantity);
            this.updateById(materialStock);

            MaterialStockDO newMaterialStock = new MaterialStockDO();
            // 新物料的库存 等于 原物料的库存 - 拣选数量
            newMaterialStock.setTotality(pickQuantity);
            newMaterialStock.setBatchNumber(materialStock.getBatchNumber());
            newMaterialStock.setMaterialConfigId(materialStock.getMaterialConfigId());
            if(StringUtils.isNotBlank(locationId)){
                newMaterialStock.setLocationId(locationId);
            }else if(StringUtils.isNotBlank(storageId)){
                newMaterialStock.setStorageId(storageId);
            }else {
                throw exception(MATERIAL_STOCK_LOCATION_OR_STORAGE_NOT_EXISTS);
            }

            String sourceId =  this.getSourceIdByMaterialStock(materialStock);
            newMaterialStock.setSourceId(sourceId);

            // 新的物料将替代原来的 物料库存被出库
            this.insert(newMaterialStock);
            materialMaintenanceService.generateMaterialMaintenance(newMaterialStock.getId(),
                    pickQuantity,
                    DictConstants.WMS_INVENTORY_MAINTENANCE_TYPE_SORTING,
                    "物料拣选出库，生成新的物料库存");
            return newMaterialStock;
        }
    }

    @Override
    public List<MaterialStockDO> getMaterialStockByIds(Collection<String> ids) {
        if(CollectionUtils.isAnyEmpty(ids)){
            return Collections.emptyList();
        }
        return materialStockMapper.selectMaterialStockAndDefaultWarehouseIdByIds(ids);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockIncludeDeletedByIds(Collection<String> ids) {
        if(CollectionUtils.isAnyEmpty(ids)){
            return Collections.emptyList();
        }
        return materialStockMapper.selectBatchIncludeDeletedByIds(ids);
    }

    @Override
    public PageResult<MaterialStockDO> getMaterialStockPage(MaterialStockPageReqVO pageReqVO) {
        return materialStockMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockList() {
        return materialStockMapper.selectList();
    }

    @Override
    public List<MaterialStockDO> getExistsMaterialConfigStockList() {
        return materialStockMapper.selectExistsMaterialConfigStockList();
    }

    /**
     * 新增或更新前的校验 TODO QianJY 新增或更新重写不允许 直接存入数据库 必须经过校验 后续使用时有需求自行扩展
     *
     * @param entity
     */
    @Override
    public int insert(MaterialStockDO entity) {
        this.saveHandle(entity);
        return materialStockMapper.insert(entity);
    }

    /**
     * 收货 生成物料库存信息
     *
     * @param createReqVO 收货信息
     * @return 编号
     */
    @Override
    public String createMaterialStock(TakeDeliverySaveReqVO createReqVO) {
        MaterialStockSaveReqVO materialStockSaveReqVO = new MaterialStockSaveReqVO();
        materialStockSaveReqVO.setMaterialConfigId(createReqVO.getMaterialConfigId());
        materialStockSaveReqVO.setLocationId(createReqVO.getLocationId());
        materialStockSaveReqVO.setStorageId(createReqVO.getStorageId());
//        materialStockSaveReqVO.setMaterialId(createReqVO.getMaterialId());
        materialStockSaveReqVO.setTotality(createReqVO.getTdQuantity());

        List<InWarehouseDetailDO> inWarehouseDetailList = inWarehouseDetailService.getInWarehouseDetailListByOrderNumberAndMaterialConfigId(createReqVO.getOrderNumber(), createReqVO.getMaterialConfigId());
        if(inWarehouseDetailList.size() > 0){
            materialStockSaveReqVO.setBatchNumber(inWarehouseDetailList.get(0).getBatchNumber());
        }
        return this.createMaterialStock(materialStockSaveReqVO);
    }


    @Override
    public Boolean insertOrUpdateBatch(Collection<MaterialStockDO> materialStockDOS){
        return materialStockMapper.insertOrUpdateBatch(materialStockDOS);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockContainerList(Integer isContainer) {
        return materialStockMapper.getMaterialStockContainerList(isContainer);
    }

    @Override
    @Cacheable(value = "wms:material_stock:all_list#2s", key = "#materialStockId", unless = "#result == null")
    public List<MaterialStockDO> getAllMaterialStockListByMaterialStockId(String materialStockId) {
        List<MaterialStockDO> materialStockListOnContainers = new ArrayList<>();
        this.getMaterialStockListByContainerIds(Collections.singletonList(materialStockId),materialStockListOnContainers);
       /* materialStockListOnContainers.addAll(materialStockList);
        if(!CollectionUtils.isAnyEmpty(materialStockListOnContainers)){
            // 因为托盘上有工装 工装上有 物料 所以还得再查一遍
            List<String> materialStockListOnContainerIds = materialStockListOnContainers.stream().map(materialStockDO -> materialStockDO.getId()).collect(Collectors.toList());
            List<MaterialStockDO> materialStockListByContainerIds = this.getMaterialStockListByContainerIds(materialStockListOnContainerIds);
            if(!CollectionUtils.isAnyEmpty(materialStockListByContainerIds)){
                materialStockListOnContainers.addAll(materialStockListByContainerIds);
            }
        }*/
        return materialStockListOnContainers;
    }

    @Override
    public List<MaterialStockDO> getMaterialStockListIncludingStorageByTrayId(String trayId) {
        return materialStockMapper.getMaterialStockListIncludingStorageByTrayId(trayId);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockListByContainerId(String materialStockId) {
        return materialStockMapper.getMaterialStockListByContainerId(materialStockId);
    }


    @Override
    public List<MaterialStockDO> getMaterialStockListByContainerIds(Collection<String> materialStockIds) {
        return materialStockMapper.getMaterialStockListByContainerIds(materialStockIds);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockListByLocationId(String locationId) {
        return materialStockMapper.getMaterialStockListByLocationId(locationId);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockListByLocationIds(Collection<String> locationIds) {
        return materialStockMapper.getMaterialStockListByLocationIds(locationIds);
    }

    @Override
    public Map<String, List<MaterialStockDO>> getRootLocationIdMaterialStockMap() {
        List<MaterialStockDO> materialStockList =  this.getExistsMaterialConfigStockList();
        // 库位 - 物料
        return CollectionUtils.convertMap(
                materialStockList,
                MaterialStockDO::getRootLocationId,
                m -> {
                    List<MaterialStockDO> ms = new ArrayList<>();
                    ms.add(m);
                    return ms;
                },
                (v1, v2) -> {
                    v1.addAll(v2);
                    return v1;
                });
    }

    @Override
    public List<MaterialStockDO> getSimpleMaterialStockListByLocationIds(Collection<String> locationIds) {
        return materialStockMapper.selectSimpleMaterialStockListByLocationIds(locationIds);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockListAndContainerByLocationId(String locationId) {
        return materialStockMapper.getMaterialStockListAndContainerByLocationId(locationId);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockByLocationId(String locationId) {
        return materialStockMapper.getMaterialStockByLocationId(locationId);
    }

    @Override
    public boolean updateMaterialStock(String id, String locationId) {
        MaterialStockDO materialStockDO = this.getMaterialStock(id);
        // 校验存在
        validateMaterialStockExists(materialStockDO.getId());
        materialStockDO.setLocationId(locationId);
        materialStockDO.setStorageId("");
        // 更新
        return this.updateById(materialStockDO) > 0;
    }

    @Override
    public Boolean updateBatchMaterialStock(Collection<String> ids, String locationId) {
        List<MaterialStockDO> materialStockDOS = this.getMaterialStockByIds(ids);
        for (MaterialStockDO materialStockDO : materialStockDOS) {
            // 校验存在
            materialStockDO.setLocationId(locationId);
            materialStockDO.setStorageId("");
            saveHandle(materialStockDO);
        }

        // 更新
        return materialStockMapper.updateBatch(materialStockDOS);
    }

    @Override
    public boolean updateMaterialStorage(String id, String storageId) {
        MaterialStockDO materialStockDO = this.getMaterialStock(id);
        // 校验存在
        validateMaterialStockExists(materialStockDO.getId());
        materialStockDO.setLocationId("");
        materialStockDO.setStorageId(storageId);
        // 更新
        return this.updateById(materialStockDO) > 0;
    }

    /**
     * 新增或更新前的校验
     *
     * @param entity
     */
    @Override
    public int updateById(MaterialStockDO entity) {
        this.saveHandle(entity);
        return materialStockMapper.updateById(entity);
    }

    /**
     * 物料的创建规则：
     * 1. 物料条码唯一
     * 2. 物料只能 有库位或者储位其一，不能同时有两个
     * 3. 非容器类物料 总库存仅为1
     *
     * 物料类型的规则： 只有单件管理的物料 才能赋予容器属性（否则无法自动生成）
     * 物料类型的规则： 单储位物料生成的储位编码为物料条码
     */
    @Override
    @LogRecord(type = WMS_MATERIAL_STOCK, subType = "{{#type}}", bizNo = "{{#entity.id}}",
            success = "{{#massage}}")
    public void saveHandle(MaterialStockDO entity) {
        this.LogRecordMessage(entity);
        // 1. 条码唯一
        if (StringUtils.isBlank(entity.getBarCode())) {
            entity.setBarCode(codeGeneratorService.getMaterialBarCode());
        }else {
            List<MaterialStockDO> materialStockListByBarCode = this.getMaterialStockListByBarCodes(Collections.singletonList(entity.getBarCode()));
            if(!CollectionUtils.isAnyEmpty(materialStockListByBarCode) && StringUtils.isBlank(entity.getId())){
                throw exception(MATERIAL_STOCK_BARCODE_EXISTS);
            }
        }

        if(entity.getTotality() == null || entity.getTotality() <= 0){
            entity.setLocationId("");
            entity.setStorageId("");
            entity.setIsExists(false);
        }

        // 2. 库位或者储位只能有一个
        int flag = 0;
       /* if(StringUtils.isNotBlank(entity.getMaterialId())){
            flag++;
            entity.setBindType(DictConstants.WMS_BIND_TYPE_MATERIAL);
        }*/
        if(entity.getIsExists() == null){
            entity.setIsExists(false);
        }
        if (StringUtils.isNotBlank(entity.getStorageId())) {
            flag++;
            entity.setBindType(DictConstants.WMS_BIND_TYPE_STORAGE);
            entity.setLocationId("");
            entity.setIsExists(true);
            MaterialStorageDO materialStorage = materialStorageService.getMaterialStorage(entity.getStorageId());
            if(materialStorage.getMaterialStockId() != null && materialStorage.getMaterialStockId().equals(entity.getId())){
                throw exception(MATERIAL_STOCK_CANNOT_BIND_SELF);
            }
            String atLocationId = this.getLocationIdByMaterialStock(entity);
            entity.setRootLocationId(atLocationId);
        }
        if (StringUtils.isNotBlank(entity.getLocationId())) {
            flag++;
            entity.setBindType(DictConstants.WMS_BIND_TYPE_LOCATION);
            entity.setStorageId("");
            entity.setIsExists(true);
            entity.setRootLocationId(entity.getLocationId());
        }
        if (flag != 1 && entity.getIsExists()) {
            throw exception(MATERIAL_STOCK_BIND_POSITION_ERROR);
        }else {
            List<MaterialStockDO> allMaterialStockList = this.getAllMaterialStockListByMaterialStockId(entity.getId());
            if(!allMaterialStockList.isEmpty()){
                allMaterialStockList = allMaterialStockList.stream().peek(materialStockDO -> materialStockDO.setRootLocationId(entity.getRootLocationId())).collect(Collectors.toList());
                this.materialStockMapper.updateBatch(allMaterialStockList);
            }
        }
        if(!entity.getIsExists()){
            entity.setBindType(DictConstants.WMS_BIND_TYPE_UNBIND);
            entity.setRootLocationId("");
        }


        Optional<MaterialConfigDO> materialConfig = Optional.ofNullable(materialConfigService.getMaterialConfig(entity.getMaterialConfigId()));
        if (!materialConfig.isPresent()) {
            throw exception(MATERIAL_TYPE_NOT_EXISTS);
        }
        // 单件1 false \ 批量2 true
        boolean isMaterialManage = materialConfig.get().getMaterialManage() == 2;

        // 物料管理模式为单件的物料， 数量只能为1
        if (!isMaterialManage && entity.getTotality() != 1) {
            throw exception(MATERIAL_MANAGE_MODE_SINGLE_MATERIAL_TOTALITY_ERROR);
        }

        // 物料管理模式为批量的物料， 数量必须大于0
        if (isMaterialManage && entity.getIsExists() && entity.getTotality() <= 0) {
            throw exception(MATERIAL_MANAGE_MODE_BATCH_MATERIAL_TOTALITY_ERROR);
        }

        // 设定锁定数量默认值
       /* if (entity.getLocked() == null) entity.setLocked(0);
        // 库存数量不能小于锁定数量
        if (entity.getTotality() < entity.getLocked()) {
            throw exception(MATERIAL_STOCK_TOTAL_LESS_THAN_LOCKED);
        } else {
            // 设置可以数量默认值
            entity.setAvailable(entity.getTotality() - entity.getLocked());
        }*/


        // 生成批次号
        if (StringUtils.isBlank(entity.getBatchNumber())) {
            entity.setBatchNumber(codeGeneratorService.getMaterialBatchNumber());
        }

        {
            // 将物料赋值到根库位
            String locationId = entity.getLocationId();
            if(StringUtils.isNotBlank(locationId)){
//                entity.setRootLocationId(locationId);
                // 查询绑定在此物料上的所有物料
                List<MaterialStockDO> materialStockList = this.getMaterialStockListByContainerId(entity.getId());
                if(!CollectionUtils.isAnyEmpty(materialStockList)){
                    // 将此物料上的所有物料赋值到根库位
                    materialStockList=  materialStockList.stream().peek(materialStock -> {
//                        materialStock.setRootLocationId(locationId);
                    }).collect(Collectors.toList());
                    materialStockMapper.updateBatch(materialStockList);
                }

            }
        }

    }

    public void LogRecordMessage(MaterialStockDO entity) {
        String type = "创建物料库存";
        String massage = "物料条码：【" + entity.getBarCode() + "】";
        if(StringUtils.isNotBlank(entity.getId())){
            type = "更新物料库存";
            MaterialStockDO materialStock = this.getMaterialStock(entity.getId());

            if(StringUtils.isNotBlank(materialStock.getLocationId())){
                massage += " ;原库位：【" + materialStock.getLocationId() + "】";
            }else if(StringUtils.isNotBlank(materialStock.getStorageId())){
                massage += " ;原储位：【" + materialStock.getStorageId() + "】";
            }else {
                massage += " ;原库位：库外";
            }

            if(StringUtils.isNotBlank(entity.getLocationId())){
                massage += " >> 变更为，新库位：【" + entity.getLocationId() + "】";
            }else if(StringUtils.isNotBlank(entity.getStorageId())){
                massage += " >> 变更为，新储位：【" + entity.getStorageId() + "】";
            }else {
                massage += " >> 变更为库外";
            }
        }else {
            if(StringUtils.isNotBlank(entity.getLocationId())){
                massage += " ;绑定库位：【" + entity.getLocationId() + "】";
            }else if(StringUtils.isNotBlank(entity.getStorageId())){
                massage += " ;绑定储位：【" + entity.getStorageId() + "】";
            }
        }

        LogRecordContext.putVariable("entity", entity);
        LogRecordContext.putVariable("type", type);
        LogRecordContext.putVariable("massage", massage);
    }


    /**
     * 判定托盘上是否绑定其他物料 todo：此方法将会被循环调用 待优化  但这个可能影响不大，还好
     *
     * @param containerId 容器物料id
     * @return
     */
    @Override
    public boolean hasNonContainerMaterial(String containerId) {
        if (StringUtils.isBlank(containerId)) return false;
        // 查询容器上的物料库存集合
        List<MaterialStockDO> materialStockList = this.getAllMaterialStockListByMaterialStockId(containerId);
        if (CollectionUtils.isAnyEmpty(materialStockList)) return false;
        for (MaterialStockDO materialStock : materialStockList) {
            // 非托盘 直接返回 true
            if (!DictConstants.WMS_MATERIAL_TYPE_TP.equals(materialStock.getMaterialType())) return true;
            // 容器类物料 继续判断是否存在非容器类物料 存在则返回 true
            if (hasNonContainerMaterial(materialStock.getId())) return true;
        }
        return false;
    }

    // 判定库位是否空闲
    @Override
    public boolean checkLocationIsVacant(String locationId) {
        if (StringUtils.isBlank(locationId)) return false;
        List<MaterialStockDO> materialStockList = this.getMaterialStockListByLocationId(locationId);
        return !CollectionUtils.isAnyEmpty(materialStockList);
    }

    @Override
    public MaterialStockDO getMaterialStockByStorageId(String storageId) {
        return this.materialStockMapper.selectMaterialStockByStorageId(storageId);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockListByStorageIds(Collection<String> storageIds) {
        return this.materialStockMapper.selectMaterialStockByStorageIds(storageIds);
    }

    @Override
    public String getLocationIdByMaterialStock(MaterialStockDO materialStock) {
        // 看他是否在库位上 在库位上 就赋值库位id
        if(StringUtils.isNotBlank(materialStock.getLocationId())){
            return materialStock.getLocationId();
        }else if(StringUtils.isNotBlank(materialStock.getStorageId())){
            // 没在库位那就是在储位上了 在储位上就得继续往下查咯
            // 获取呼叫物料绑定的容器
            MaterialStockDO callMaterialBindContainer = this.getMaterialStockByStorageId(materialStock.getStorageId());
            return this.getLocationIdByMaterialStock(callMaterialBindContainer);
        }else {
            // 都没在就得报错了
            throw exception(MATERIAL_STOCK_NOT_BIND_POSITION);
        }
    }

    @Override
    public MaterialStockDO getContainerStockByMaterialStock(MaterialStockDO materialStock) throws ServiceException {
        // 看他是否在库位上 在库位上 就赋值库位id
        if(StringUtils.isNotBlank(materialStock.getLocationId())){
            if(!DictConstants.WMS_MATERIAL_TYPE_TP.equals(materialStock.getMaterialType())
                    && !DictConstants.WMS_MATERIAL_TYPE_GZ.equals(materialStock.getMaterialType())){
                throw exception(MATERIAL_STOCK_NOT_CONTAINER_MATERIAL_CANNOT_BIND_BIN);
            }
            return materialStock;
        }else if(StringUtils.isNotBlank(materialStock.getStorageId())){
            // 没在库位那就是在储位上了 在储位上就得继续往下查咯
            // 获取呼叫物料绑定的容器
            MaterialStockDO callMaterialBindContainer = this.getMaterialStockByStorageId(materialStock.getStorageId());
            return this.getContainerStockByMaterialStock(callMaterialBindContainer);
        }else {
            // 都没在就得报错了
            throw exception(MATERIAL_STOCK_NOT_BIND_POSITION);
        }
    }

    @Override
    public MaterialStockDO getMaterialAtLocationByMaterialStock(MaterialStockDO materialStock) {
        // 看他是否在库位上 在库位上 就赋值库位id
        if(StringUtils.isNotBlank(materialStock.getLocationId())){
            return materialStock;
        }else if(StringUtils.isNotBlank(materialStock.getStorageId())){
            // 没在库位那就是在储位上了 在储位上就得继续往下查咯
            // 获取呼叫物料绑定的容器
            MaterialStockDO callMaterialBindContainer = this.getMaterialStockByStorageId(materialStock.getStorageId());
            return this.getContainerStockByMaterialStock(callMaterialBindContainer);
        }else {
            // 都没在就得报错了
            throw exception(MATERIAL_STOCK_NOT_BIND_POSITION);
        }
    }

    @Override
    public MaterialStockDO getContainerStockByLocationId(String locationId) {
        // 查询库位上绑定的物料库存
        List<MaterialStockDO> containerStockList = this.getMaterialStockByLocationId(locationId);
        if(containerStockList.size() != 1){
            // 此物料所在库位绑定多个容器 不能呼叫
            throw exception(CARRYING_TASK_LOCATION_HAS_MULTIPLE_MATERIAL_CALL);
        }
        MaterialStockDO containerStock = containerStockList.get(0);
        if(!DictConstants.WMS_MATERIAL_TYPE_TP.equals(containerStock.getMaterialType())
                && !DictConstants.WMS_MATERIAL_TYPE_GZ.equals(containerStock.getMaterialType())){
            throw exception(MATERIAL_STOCK_NOT_CONTAINER_MATERIAL_CANNOT_BIND_BIN);
        }
        return containerStock;
    }

    @Override
    public String getLocationIdByMaterialStockId(String materialStockId) {
        MaterialStockDO materialStock = this.getMaterialStock(materialStockId);
        return this.getLocationIdByMaterialStock(materialStock);
    }

    @Override
    public MaterialStockDO getMaterialStockById(String materialStockId) {
        return this.materialStockMapper.selectMaterialStockById(materialStockId);
    }

    @Override
    public MaterialStockDO getMaterialStockAndMaterialTypeById(String materialStockId) {
        return this.materialStockMapper.selectMaterialStockAndMaterialTypeById(materialStockId);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockListByMaterialConfigIds(Collection<String> materialConfigIds) {
        return this.materialStockMapper.selectMaterialStockListByMaterialConfigIds(materialConfigIds);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockListByMaterialConfigIdsAndLockedZero(Collection<String> materialConfigIds) {
        return this.materialStockMapper.selectMaterialStockListByMaterialConfigIdsAndLockedZero(materialConfigIds);
    }

    @Override
    public List<MaterialStockDO> getFreeTrayStockListInAutoStockAreaByMaterialStockIds(List<String> trayIds) {
        // 根据托盘ids 筛选可被呼叫的托盘库存
        List<MaterialStockDO> trayList = this.materialStockMapper.selectTrayStockListInAutoStockAreaByMaterialStockIds(trayIds);
        if(CollectionUtils.isAnyEmpty(trayList)){
            return Collections.emptyList();
        }
        // 根据托盘ids 查询所有可用储位
        List<MaterialStorageDO> trayAllAvailableStorageList = materialStorageService.getMaterialStorageListByContainerStockIds(trayIds);
        // 根据ids 查询被占用的储位
        List<MaterialStorageDO> materialStorageList = materialStorageService.getOccupyMaterialStockListByTrayIds(trayList.stream().map(MaterialStockDO::getId).collect(Collectors.toList()));
         // 筛选出空闲的托盘库存
        return trayList.stream().filter(tray -> {
            for (MaterialStorageDO materialStorageDO : materialStorageList) {
                if(materialStorageDO.getMaterialStockId().equals(tray.getId())){
                    // 此托盘的储位已被占用 跳过
                    return false;
                }
            }
            for (MaterialStorageDO trayAllAvailableStorage : trayAllAvailableStorageList) {
                if(trayAllAvailableStorage.getMaterialStockId().equals(tray.getId())){
                    // 此托盘有可用储位 存起来
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MaterialStockDO> getEmptyTrayStockListByMaterialConfigIds(Collection<String> trayConfigIds, String callAreaId) {
        List<MaterialStockDO> trayList = this.materialStockMapper.selectEmptyTrayStockListByMaterialConfigIds(trayConfigIds, callAreaId);
        if(CollectionUtils.isAnyEmpty(trayList)){
            return Collections.emptyList();
        }
        List<String> trayIds = trayList.stream().map(MaterialStockDO::getId).distinct().collect(Collectors.toList());
        // 根据托盘ids 查询所有可用储位
        List<MaterialStorageDO> trayAllAvailableStorageList = materialStorageService.getMaterialStorageListByContainerStockIds(trayIds);
        // 根据ids 查询被占用的储位
        List<MaterialStorageDO> materialStorageList = materialStorageService.getOccupyMaterialStockListByTrayIds(trayIds);
        // 手动排个序，把在接驳库位的托盘放在queue前面
        LinkedList<MaterialStockDO> queueList = new LinkedList<>();
        // 可以的空闲托盘id集合
        Set<String> trayIdSet = new HashSet<>();
        // 筛选出空闲的托盘库存
        one: for(MaterialStockDO tray : trayList){
            for (MaterialStorageDO materialStorageDO : materialStorageList) {
                if(materialStorageDO.getMaterialStockId().equals(tray.getId())){
                    // 此托盘的储位已被占用 跳过
                    continue one;
                }
            }
            for (MaterialStorageDO trayAllAvailableStorage : trayAllAvailableStorageList) {
                if(trayIdSet.contains(tray.getId())){
                    continue ;
                }
                if(trayAllAvailableStorage.getMaterialStockId().equals(tray.getId())){
                    // 此托盘有可用储位 存起来
                    // 在接驳库位的托盘放在queue前面
                    if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(tray.getAtAreaType())){
                        queueList.addFirst(tray);
                    }else {
                        // 在其他库位的托盘放在queue后面
                        queueList.addLast(tray);
                    }
                    trayIdSet.add(tray.getId());
                }
            }
        }

        return queueList;
    }

    @Override
    public List<MaterialStockDO> getMaterialStockListAndLocationByIds(List<String> materialStockIds) {
        return this.materialStockMapper.selectMaterialStockListAndLocationByIds(materialStockIds);
    }

    @Override
    public List<MaterialStockDO> getOrderMaterialStockListByMaterialConfigIds(Collection<String> materialConfigIds, Supplier<List<WarehouseAreaDO>> canOutWarehouseAreaSupplier) {

        // 根据物料类型id 查询物料库存
        List<MaterialStockDO> selectMaterialStockList = this.getMaterialStockListByMaterialConfigIdsAndLockedZero(materialConfigIds);
        if(CollectionUtils.isAnyEmpty(selectMaterialStockList)){
            return Collections.emptyList();
        }
        Map<String, MaterialStockDO> selectMaterialStockMap = CollectionUtils.convertMap(selectMaterialStockList, MaterialStockDO::getId);

        // 根据物料获取 物料所在库位上的物料库存（物料绑定→托盘或工装→库位）
        Map<String, MaterialStockDO> materialStockMapOnLocation = this.getWarehouseLocationListByMaterialStockList(selectMaterialStockList);
        List<String> locationIds = materialStockMapOnLocation.values().stream().map(MaterialStockDO::getLocationId).collect(Collectors.toList());

        // 根据库位id 获取库位信息
        List<WarehouseLocationDO> materialLocationList = warehouseLocationService.getWarehouseLocationListByIds(locationIds);
        Map<String, String> materialLocationMap = CollectionUtils.convertMap(materialLocationList, WarehouseLocationDO::getId, WarehouseLocationDO::getWarehouseAreaId);

        // 获取所有可出库的库区
//        List<WarehouseAreaDO> canOutWarehouseAreaList = warehouseAreaService.getOutWarehouseOrderSelectAreaList();
        List<WarehouseAreaDO> canOutWarehouseAreaList = canOutWarehouseAreaSupplier.get();
        Set<String> canOutAreaIds = CollectionUtils.convertSet(canOutWarehouseAreaList, WarehouseAreaDO::getId);

        List<MaterialStockDO> resultMaterialStock = new ArrayList<>();
        // 可出库的库区 筛选出库符合条件的物料库存
        for (String stockId : materialStockMapOnLocation.keySet()) {
            String stockAtLocationId = materialStockMapOnLocation.get(stockId).getLocationId();
            // 物料所在库位
            if(StringUtils.isNotBlank(stockAtLocationId)
                    // 根据库位找到库区
                    && materialLocationMap.containsKey(stockAtLocationId)
                    // 库区是否为 可出库的库区
                    && canOutAreaIds.contains(materialLocationMap.get(stockAtLocationId))){
                if(!selectMaterialStockMap.containsKey(stockId)) throw exception(BUG);
                // 存在就是
                resultMaterialStock.add(selectMaterialStockMap.get(stockId));
            }
        }

        return resultMaterialStock;
    }


    @Override
    public Map<String,MaterialStockDO> getWarehouseLocationListByMaterialStockList(List<MaterialStockDO> materialStockList) {
        Map<String, MaterialStockDO> resultMaterialStockMap = new HashMap<>();
        // 物料在容器上的集合
        List<MaterialStockDO> materialStockListOnContainer = new ArrayList<>();
        for (MaterialStockDO materialStockDO : materialStockList) {
            if(StringUtils.isNotBlank(materialStockDO.getLocationId())){
                // 物料在库位上 直接放在map中
                resultMaterialStockMap.put(materialStockDO.getId(),materialStockDO);
            } else if (StringUtils.isNotBlank(materialStockDO.getStorageId())) {
                // 物料在储位上 先放到容器上集合中
                materialStockListOnContainer.add(materialStockDO);
            }
        }

        List<MaterialStockDO> finallyMaterialStockList = new ArrayList<>();
        // 在储位上的物料
        if(!CollectionUtils.isAnyEmpty(materialStockListOnContainer)){
            List<String> storageIds = materialStockListOnContainer.stream().map(MaterialStockDO::getStorageId).collect(Collectors.toList());
            // 物料绑定在储位容器上 对应的所有储位容器库存 并包含OwnStorageId 容器储位的储位id
            List<MaterialStockDO> containerStockList = this.getMaterialStockListByStorageIds(storageIds);
            for (MaterialStockDO containerStock : containerStockList) {
                for (MaterialStockDO stockDO : materialStockListOnContainer) {
                    if(StringUtils.isNotBlank(containerStock.getLocationId())){
                        // 物料绑定在容器上 并且容器自身的储位id 等于 物料绑定的储位id  放到map中
                        if(containerStock.getOwnStorageId().equals(stockDO.getStorageId())){
                            resultMaterialStockMap.put(stockDO.getId(),containerStock);
                        }
                    } else if (StringUtils.isNotBlank(containerStock.getStorageId())) {
                        // 如果这个容器没在库位上 说明这个容器是 工装 ，那继续往下找工装绑定的托盘
                        finallyMaterialStockList.add(containerStock);
                    }
                }
            }
        }

        // 绑定在 托盘上的 工装 物料库存
        if(!CollectionUtils.isAnyEmpty(finallyMaterialStockList)){
            List<String> storageIds = finallyMaterialStockList.stream().map(MaterialStockDO::getStorageId).collect(Collectors.toList());
            // 工装绑定在托盘上 对应的所有托盘库存 并包含OwnStorageId 容器储位的储位id
            List<MaterialStockDO> trayStockList = this.getMaterialStockListByStorageIds(storageIds);
            for (MaterialStockDO trayStockDO : trayStockList) {
                // 先看托盘是不是绑定在库位上，没在库位上 就不用扯了
                if(StringUtils.isNotBlank(trayStockDO.getLocationId())){
                    for (MaterialStockDO finallyMaterialStock : finallyMaterialStockList) {
                        // 如果 托盘的储位id 等于 工装的绑定的储位id
                        if(trayStockDO.getOwnStorageId().equals(finallyMaterialStock.getStorageId())){
                            for (MaterialStockDO stockDO : materialStockListOnContainer) {
                                // 继续找这个 工装自己的库位上绑定物料
                                if(finallyMaterialStock.getOwnStorageId().equals(stockDO.getStorageId())){
                                    // 放到map中
                                    resultMaterialStockMap.put(stockDO.getId(),trayStockDO);
                                }
                            }
                        }
                    }
                }
            }
        }

        return resultMaterialStockMap;
    }

    @Override
    public List<MaterialStockDO> checkMaterialStockOrderExists(Collection<String> materialStockIdSet) {
        if(CollectionUtils.isAnyEmpty(materialStockIdSet)){
            return Collections.emptyList();
        }
        return this.materialStockMapper.selectMaterialStockListByMaterialStockIds(materialStockIdSet);
    }

    @Override
    public List<MaterialStockDO> checkChooseStockOrderExists(Collection<String> materialStockIdSet) {
        if(CollectionUtils.isAnyEmpty(materialStockIdSet)){
            return Collections.emptyList();
        }
        return this.materialStockMapper.selectChooseStockListByMaterialStockIds(materialStockIdSet);
    }


    /**
     * 物料编码查库存
     * @param barCodes
     * @return
     */
    @Override
    public List<MaterialStockDO> getMaterialStockListByBarCodes(Collection<String> barCodes) {
        return materialStockMapper.selectMaterialStockListByBarCodes(barCodes);
    }

    @Override
    public MaterialStockDO getMaterialStockByBarCode(String barCode){
        return materialStockMapper.selectMaterialStockByBarCode(barCode);
    }

    @Override
    public List<MaterialStockDO> getAllMaterialStockListByBarCodes(Collection<String> barCodes) {
        if(CollectionUtils.isAnyEmpty(barCodes)){
            return Collections.emptyList();
        }
        return materialStockMapper.selectAllMaterialStockListByBarCodes(barCodes);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockListByCheckAreaIdAndMaterialConfigIds(String checkAreaId, List<String> strings) {
        return materialStockMapper.selectMaterialStockListByCheckAreaIdAndMaterialConfigIds(checkAreaId, strings);
    }

    @Override
    public void increaseMaterialStock(MaterialStockDO materialStockDO, CheckDetailDO checkDetailDO) {
        if(Objects.equals(checkDetailDO.getCheckType(), DictConstants.WMS_CHECK_TYPE_PROFIT)){
            if(checkDetailDO.getCheckTotality() == 0){
                throw exception(CHECK_CONTAINER_COUNT_ERROR);
            }
            // 盘盈新增
            // 看看原来在哪 如果原来所在库区的仓库与现在不一致 则需要生成盘亏出库单和盘盈入库单
            // 根据物料获取其所在库区
            WarehouseAreaDO materialStockAtWarehouseArea = warehouseAreaService.getWarehouseAreaByMaterialStock(materialStockDO);
            // 查看盘点的容器所在库区
            String checkContainerId = checkDetailDO.getCheckContainerId();
            MaterialStockDO checkContainer = this.getMaterialStockByCheckContainerId(checkContainerId);
            WarehouseAreaDO containerAtWarehouseArea = warehouseAreaService.getWarehouseAreaByMaterialStock(checkContainer);
            // 看看所在仓库是否一致
            if(!Objects.equals(containerAtWarehouseArea.getWarehouseId(), materialStockAtWarehouseArea.getWarehouseId())){
                // 不一致 则生成盘亏出库单和盘盈入库单
                // 盘亏出库单
                int lossTotality = checkDetailDO.getCheckTotality();
                LocalDateTime now = LocalDateTime.now();
                LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
                assert loginUser != null;
                outWarehouseDetailService.createLossOutWarehouseDetail(lossTotality, materialStockDO, materialStockAtWarehouseArea.getWarehouseId(), String.valueOf(loginUser.getId()), now);
                // 盘盈入库单
                inWarehouseDetailService.createProfitInWarehouseDetail(lossTotality, materialStockDO, containerAtWarehouseArea.getWarehouseId(), String.valueOf(loginUser.getId()), now);
            }
            MaterialStorageDO materialStorage = materialStorageService.getMaterialStorageByStorageCode(checkDetailDO.getStorageCode());
            if(materialStorage == null){
                throw exception(MATERIAL_STORAGE_NOT_EXISTS);
            }
            materialStockDO.setLocationId("");
            materialStockDO.setStorageId(materialStorage.getId());
        }
        materialStockDO.setTotality(checkDetailDO.getCheckTotality());
        this.updateMaterialStock(BeanUtils.toBean(materialStockDO, MaterialStockSaveReqVO.class));
    }


    @Override
    public void decreaseMaterialStockt(MaterialStockDO materialStockDO, CheckDetailDO checkDetailDO) {
        // 盘亏出库单
        int lossTotality = checkDetailDO.getRealTotality() - checkDetailDO.getCheckTotality();
        LocalDateTime now = LocalDateTime.now();
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        assert loginUser != null;
        WarehouseAreaDO materialStockAtWarehouseArea = warehouseAreaService.getWarehouseAreaByMaterialStock(materialStockDO);
        outWarehouseDetailService.createLossOutWarehouseDetail(lossTotality, materialStockDO, materialStockAtWarehouseArea.getWarehouseId(), String.valueOf(loginUser.getId()), now);
        if(checkDetailDO.getCheckTotality() == 0){
            materialStockDO.setLocationId("");
            materialStockDO.setStorageId("");
            materialStockDO.setIsExists(false);
        }else{
            materialStockDO.setTotality(checkDetailDO.getCheckTotality());
        }
        this.updateMaterialStock(BeanUtils.toBean(materialStockDO, MaterialStockSaveReqVO.class));
    }

    @Override
    public boolean validateMaterialInLocked(MaterialStockDO materialStockDO, String ...warehouseIds) {
        WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByMaterialStock(materialStockDO);
        List<CheckPlanDO> notFinishedCheckPlan = checkPlanService.getNotFinishedCheckPlanAndLocked();
        Set<String> warehouseAreaIdSet = CollectionUtils.convertSet(notFinishedCheckPlan, CheckPlanDO::getCheckAreaId);
        if(warehouseAreaIdSet.contains(warehouseAreaDO.getId())){
            return true;
        }
        List<WarehouseAreaDO> warehouseAreaDOS = warehouseAreaService.getWarehouseAreaByIds(warehouseAreaIdSet);
        Set<String> warehouseIdSet = CollectionUtils.convertSet(warehouseAreaDOS, WarehouseAreaDO::getWarehouseId);
        for (String warehouseId : warehouseIds) {
            if(warehouseIdSet.contains(warehouseId)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateMaterialInLocked(String materialStockId, String ...warehouseIds) {
        return validateMaterialInLocked(this.getMaterialStockById(materialStockId), warehouseIds);
    }

    @Override
    public Boolean updateMaterialStockConfig(String materialStockId, String materialConfigId) {
        MaterialConfigDO mainConfig = materialConfigService.getMaterialConfig(materialConfigId);
        if(mainConfig == null){
            throw exception(MATERIAL_TYPE_NOT_EXISTS);
        }
        materialMaintenanceService.generateMaterialMaintenance(materialStockId,
                DictConstants.WMS_INVENTORY_MAINTENANCE_TYPE_PROCESSING,
                "生产订单加工更新物料类型");
        return materialStockMapper.update(new LambdaUpdateWrapper<MaterialStockDO>()
                .set(MaterialStockDO::getMaterialConfigId, mainConfig.getId())
                .eq(MaterialStockDO::getId, materialStockId))>0;
    }


    @Override
    public MaterialStockDO getMaterialStockByCheckContainerId(String checkContainerId) {
        return materialStockMapper.selectMaterialStockByCheckContainerId(checkContainerId);
    }


    @Override
    public MaterialStockDO getMaterialStorageByStorageCode(String storageCode) {
        return materialStockMapper.selectMaterialStockByStorageCode(storageCode);
    }

    @Override
    public List<MaterialStockDO> getAllMaterialStockByLocationId(String locationId) {
        // 初始化物料库存集合
        List<MaterialStockDO> allMaterialStockList = new ArrayList<>();
        // 根据库位获取库位上的所有物料库存
        List<MaterialStockDO> materialStockList = materialStockMapper.selectAllMaterialStockByLocationId(locationId);
        if(!CollectionUtils.isAnyEmpty(materialStockList)){
            // 将物料库存集合加入到 所有物料库存集合中
            allMaterialStockList.addAll(materialStockList);
            List<String> containerStockIds = materialStockList.stream().filter(m -> DictConstants.WMS_MATERIAL_TYPE_TP.equals(m.getMaterialType())
                    || DictConstants.WMS_MATERIAL_TYPE_GZ.equals(m.getMaterialType()))
                    .map(MaterialStockDO::getId)
                    .collect(Collectors.toList());
            // 如果存在 托盘和工装
            if(!CollectionUtils.isAnyEmpty(containerStockIds)){
                // 获取其上的物料库存 并加入到 所有物料库存集合中
               this.getMaterialStockListByContainerIds(containerStockIds,allMaterialStockList);
            }
        }
        return allMaterialStockList;
    }


    @Override
    public CommonResult<List<MaterialStockLocationTypeDTO>> getMaterialStockLocationTypeByMaterialStockList(List<MaterialStockDO> materialStockList) {
        if(CollectionUtils.isAnyEmpty(materialStockList)){
            return CommonResult.success(Collections.emptyList());
        }
        Map<String, WarehouseAreaDO> materialIdAndAreaMap = warehouseAreaService.getWarehouseAreaByMaterialStockList(materialStockList);
        Set<String> warehouseIdSet = new HashSet<>();
        materialIdAndAreaMap.forEach((materialId,warehouseAreaDO)->{
            warehouseIdSet.add(warehouseAreaDO.getWarehouseId());
        });
        List<WarehouseDO> warehouseList = warehouseService.getWarehouseByIds(warehouseIdSet);
        Map<String, WarehouseDO> stringWarehouseDOMap = CollectionUtils.convertMap(warehouseList, WarehouseDO::getId);

        List<MaterialStockLocationTypeDTO> result = new ArrayList<>();
        for (MaterialStockDO materialStock : materialStockList) {
            String materialId = materialStock.getId();
            if(materialIdAndAreaMap.containsKey(materialId)){
                MaterialStockLocationTypeDTO materialStockLocationTypeDTO = BeanUtils.toBean(materialStock, MaterialStockLocationTypeDTO.class);
                WarehouseAreaDO warehouseAreaDO = materialIdAndAreaMap.get(materialId);

                materialStockLocationTypeDTO.setAreaType(warehouseAreaDO.getAreaType());
                materialStockLocationTypeDTO.setAreaProperty(warehouseAreaDO.getAreaProperty());
                if(stringWarehouseDOMap.containsKey(warehouseAreaDO.getWarehouseId())){
                    WarehouseDO warehouseDO = stringWarehouseDOMap.get(warehouseAreaDO.getWarehouseId());
                    materialStockLocationTypeDTO.setWarehouseType(warehouseDO.getWarehouseType());
                    materialStockLocationTypeDTO.setWarehouseId(warehouseDO.getId());
                }
                result.add(materialStockLocationTypeDTO);
            }
        }

        return CommonResult.success(result);
    }

    @Override
    public List<MaterialStockDO> getMaterialStockByLocationCode(String locationCode) {
        return materialStockMapper.selectMaterialStockByLocationCode(locationCode);
    }

    // 根据物料id和其实体对照表获取  其所在库位上的 容器物料  （原始物料id和容器物料实体对照表）
    @Override
    public void getMaterialStockByStorageIds(Map<String, MaterialStockDO> materialIdAndMaterialStockDOMap,Map<String, MaterialStockDO> result) {
        Map<String, String> materialIdAndStorageIdMap = new HashMap<>();
        materialIdAndMaterialStockDOMap.forEach((materialId,materialStockDO)->{
            if(StringUtils.isNotBlank(materialStockDO.getLocationId())){
                result.put(materialId, materialStockDO);
            }else if(StringUtils.isNotBlank(materialStockDO.getStorageId())){
                materialIdAndStorageIdMap.put(materialId, materialStockDO.getStorageId());
            }
        });
        if(!materialIdAndStorageIdMap.isEmpty()){
            List<MaterialStockDO> materialStockList = this.getMaterialStockListByStorageIds(materialIdAndStorageIdMap.values());
            Map<String, MaterialStockDO> storageIdAndMaterialStockDOMap = CollectionUtils.convertMap(materialStockList, MaterialStockDO::getOwnStorageId);
            if(!CollectionUtils.isAnyEmpty(materialStockList)){
                materialIdAndStorageIdMap.forEach((materialId,storageId)->{
                    if(storageIdAndMaterialStockDOMap.containsKey(storageId)){
                        materialIdAndMaterialStockDOMap.put(materialId, storageIdAndMaterialStockDOMap.get(storageId));
                    }
                });
                this.getMaterialStockByStorageIds(materialIdAndMaterialStockDOMap,result);
            }
        }
    }

    @Override
    public String getSourceIdByMaterialStock(MaterialStockDO materialStock) {
        if(StringUtils.isBlank(materialStock.getSourceId())){
            return materialStock.getId();
        }
        return getSourceIdByMaterialStock(this.getMaterialStock(materialStock.getSourceId()));
    }

    @Override
    public PageResult<MaterialStockDO> getMaterialStockAtStorageAreaByMaterialNumber(MaterialStockPageReqVO pageReqVO) {
        return materialStockMapper.selectMaterialStockAtStorageAreaByMaterialNumber(pageReqVO);
    }

    @Override
    public MaterialStockDO getMaterialStockIncludeNotExistById(String id) {
        return materialStockMapper.selectOne(MaterialStockDO::getId, id);
    }

    @Override
    public List<MaterialStockDO> getMaterialsAndLocationByIds(Collection<String> ids) {
        if(CollectionUtils.isAnyEmpty(ids)){
            return Collections.emptyList();
        }
        return materialStockMapper.selectMaterialStockListLocationByIds(ids);
    }

    @Override
    public List<MaterialStockDO> getMaterialsByIds(Collection<String> ids) {
        if(CollectionUtils.isAnyEmpty(ids)){
            return Collections.emptyList();
        }
        return materialStockMapper.selectMaterialStockListByIds(ids);
    }

    @Override
    public List<MaterialStockDO> getMaterialsAndConfigByIds(Collection<String> ids) {
        if(CollectionUtils.isAnyEmpty(ids)){
            return Collections.emptyList();
        }
        return materialStockMapper.selectMaterialsAndConfigByIds(ids);
    }


    @Override
    public String generateProductTool(ProductToolReqDTO productToolReqDTO) {
        // 分解成品刀
        if(StringUtils.isNotBlank(productToolReqDTO.getMaterialStockId())){
            MaterialStockDO materialStockDO = this.getMaterialStockIncludeNotExistById(productToolReqDTO.getMaterialStockId());
            if(materialStockDO == null){
                throw exception(MATERIAL_STOCK_NOT_EXISTS);
            }
            materialStockDO.setTotality(0);
            materialStockDO.setLocationId("");
            materialStockDO.setStorageId("");
            materialStockDO.setIsExists(false);
            this.updateById(materialStockDO);
            return null;
        }
        // 生成 成品刀
        if(StringUtils.isNotBlank(productToolReqDTO.getStorageId())
                && StringUtils.isNotBlank(productToolReqDTO.getMaterialConfigId())){
            MaterialStockDO materialStock = new MaterialStockDO();
            materialStock.setStorageId(productToolReqDTO.getStorageId());
            materialStock.setMaterialConfigId(productToolReqDTO.getMaterialConfigId());
            materialStock.setTotality(1);
            this.insert(materialStock);
            return materialStock.getId();
        }
        throw exception(PARAM_NOT_NULL);
    }

    @Override
    public Boolean assembleOrRecoveryMaterial(List<AssembleToolReqDTO> assembleToolReqDTOList) {
        assembleToolReqDTOList.forEach(assembleToolReqDTO -> {
            // 原料刀 拆卸入库
            MaterialStockDO materialStockDO = this.getMaterialStockIncludeNotExistById(assembleToolReqDTO.getMaterialStockId());
            if(materialStockDO == null){
                throw exception(MATERIAL_STOCK_NOT_EXISTS);
            }
            // 如果传递了 储位参数  -- 拆卸原料刀
            if(StringUtils.isNotBlank(assembleToolReqDTO.getStorageId())){
                String storageId = assembleToolReqDTO.getStorageId();
                // 如果物料存在
                if(materialStockDO.getIsExists()){
                    // 判断是否存放至相同的储位
                    if(storageId.equals(materialStockDO.getStorageId())){
                        //如果放在原储位 则直接更新数量
                        materialStockDO.setTotality(materialStockDO.getTotality() + assembleToolReqDTO.getQuantity());
                        this.updateById(materialStockDO);
                    }else{
                        // 如果物料存在 但不在原储位 则拆掉的物料新生成
                        ProductToolReqDTO productToolReqDTO = new ProductToolReqDTO();
                        productToolReqDTO.setMaterialConfigId(materialStockDO.getMaterialConfigId());
                        productToolReqDTO.setStorageId(storageId);
                        productToolReqDTO.setQuantity(assembleToolReqDTO.getQuantity());
                        this.generateProductTool(productToolReqDTO);
                    }

                }else{
                    // 如果物料不存在 直接用原来的物料改位置就行了
                    materialStockDO.setStorageId(storageId);
                    materialStockDO.setLocationId("");
                    materialStockDO.setIsExists(true);
                    materialStockDO.setTotality(assembleToolReqDTO.getQuantity());
                    this.updateById(materialStockDO);
                }
            }else {
                // 装配的刀必须存在
                if(!materialStockDO.getIsExists()){
                    throw exception(MATERIAL_STOCK_NOT_EXISTS);
                }
                // 原料刀装配
                // 装配数量
                Integer quantity = assembleToolReqDTO.getQuantity();
                // 库存数量
                Integer totality = materialStockDO.getTotality();
                if(totality>quantity){
                    materialStockDO.setTotality(totality - quantity);
                    this.updateById(materialStockDO);
                }else if(totality == quantity){
                    materialStockDO.setTotality(0);
                    materialStockDO.setLocationId("");
                    materialStockDO.setStorageId("");
                    materialStockDO.setIsExists(false);
                    this.updateById(materialStockDO);
                }else {
                    throw exception(MATERIAL_STOCK_NOT_ENOUGH_STOCK);
                }
            }
        });
        return null;
    }


    // 获取 容器物料id获取其上所有物料库存集合
    private void getMaterialStockListByContainerIds(List<String> containerStockIds, List<MaterialStockDO> resultMaterialStockList) {
        if(!CollectionUtils.isAnyEmpty(containerStockIds)){
            // 获取 托盘或工装上的物料库存
            List<MaterialStockDO> materialStockList = this.getMaterialStockListByContainerIds(containerStockIds);
            if(!CollectionUtils.isAnyEmpty(materialStockList)){
                resultMaterialStockList.addAll(materialStockList);
                // 获得为 托盘或工装的物料
                List<String> containerStockIds2 = materialStockList.stream().filter(m ->
                                DictConstants.WMS_MATERIAL_TYPE_TP.equals(m.getMaterialType())
                                || DictConstants.WMS_MATERIAL_TYPE_GZ.equals(m.getMaterialType()))
                        .map(MaterialStockDO::getId)
                        .collect(Collectors.toList());
                // 如果存在托盘和工装上还存在 托盘或工装
                if(!CollectionUtils.isAnyEmpty(containerStockIds2)){
                    // 递归调用
                    this.getMaterialStockListByContainerIds(containerStockIds2,resultMaterialStockList);
                }
            }
        }
    }
}
