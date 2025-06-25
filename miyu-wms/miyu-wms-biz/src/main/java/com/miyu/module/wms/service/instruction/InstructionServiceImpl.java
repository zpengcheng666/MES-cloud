package com.miyu.module.wms.service.instruction;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.miyu.module.wms.controller.admin.instruction.vo.InstructionPageReqVO;
import com.miyu.module.wms.controller.admin.instruction.vo.InstructionSaveReqVO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.instruction.InstructionDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.dal.mysql.instruction.InstructionMapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.framework.generator.seivice.ICodeGeneratorService;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.materialstorage.MaterialStorageService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 指令 Service 实现类
 *
 * @author 王正浩
 */
@Service
@Validated
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class InstructionServiceImpl implements InstructionService {

    @Resource
    private InstructionMapper instructionMapper;
    @Resource
    private ICodeGeneratorService codeGeneratorService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private MaterialStorageService materialStorageService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    @Lazy
    private CarryTaskService carryTaskService;


    @Override
    public String createInstruction(InstructionSaveReqVO createReqVO) {
        // 插入
        InstructionDO instruction = BeanUtils.toBean(createReqVO, InstructionDO.class);
        instructionMapper.insert(instruction);
        // 返回
        return instruction.getId();
    }

    @Override
    public void updateInstruction(InstructionSaveReqVO updateReqVO) {
        // 校验存在
        validateInstructionExists(updateReqVO.getId());
        // 更新
        InstructionDO updateObj = BeanUtils.toBean(updateReqVO, InstructionDO.class);
        instructionMapper.updateById(updateObj);
    }

    @Override
    public void deleteInstruction(String id) {
        // 校验存在
        validateInstructionExists(id);
        // 删除
        instructionMapper.deleteById(id);
    }

    private void validateInstructionExists(String id) {
        if (instructionMapper.selectById(id) == null) {
            throw exception(INSTRUCTION_NOT_EXISTS);
        }
    }

    @Override
    public InstructionDO getInstruction(String id) {
        return instructionMapper.selectById(id);
    }

    @Override
    public PageResult<InstructionDO> getInstructionPage(InstructionPageReqVO pageReqVO) {
        return instructionMapper.selectPage(pageReqVO);
    }

    private InstructionDO createOnInstruction(String carryStockId, String startLocationId, String targetLocationId){
        String insCode = codeGeneratorService.generateTimestampSerialNumber();
        String startLocationCode = warehouseLocationService.getWarehouseLocation(startLocationId).getLocationCode();
        String targetLocationCode = warehouseLocationService.getWarehouseLocation(targetLocationId).getLocationCode();
        // 用于上架指令 生成
        return new InstructionDO(insCode, carryStockId,DictConstants.WMS_INSTRUCTION_TYPE_ON,startLocationId, targetLocationId, startLocationCode, targetLocationCode,null);
    }
    private InstructionDO createOffInstruction(String carryStockId, String startLocationId, String targetLocationId){
        String insCode = codeGeneratorService.generateTimestampSerialNumber();
        String startLocationCode = warehouseLocationService.getWarehouseLocation(startLocationId).getLocationCode();
        String targetLocationCode = warehouseLocationService.getWarehouseLocation(targetLocationId).getLocationCode();
        // 用于下架指令 生成
        return new InstructionDO(insCode, carryStockId,DictConstants.WMS_INSTRUCTION_TYPE_OFF, startLocationId, targetLocationId, startLocationCode, targetLocationCode,null);
    }


    /*                                               新增内容                                                          */
    /******************************************************************************************************************/


    /**
     * 查询是否已存在未完成的指令任务  todo：此方法将会被循环调用 待优化
     *
     * @param startLocationId  起始库位
     * @param targetLocationId 目标库位
     * @return 是否存在未完成指令
     */
    @Override
    public boolean hasUnfinishedInstruction(String startLocationId, String targetLocationId) {
        List<InstructionDO> instructionList = instructionMapper.selectNotFinishedInstruction();
        for (InstructionDO instructionDO : instructionList) {
            if(
                    instructionDO.getStartLocationId().equals(startLocationId)
                    || instructionDO.getStartLocationId().equals(targetLocationId)
                    || instructionDO.getTargetLocationId().equals(startLocationId)
                    || instructionDO.getTargetLocationId().equals(targetLocationId)
            ){
                return true;
            }
        }
        return false;
    }

    /**
     * ②下架指令 只需关心指令是否已下发即可 至于位置可不可用 由调用者决定
     * 1. 查询是否存在未完成的指令任务
     * 2. 若存在 则提示用户 请先完成该任务
     * 3. 若不存在 则下发下架指令
     *
     * @param materialStockId  物料id
     * @param startLocationId  起始库位id
     * @param targetLocationId 目标库位id
     */
    @Override
    public synchronized InstructionDO offShelfInstruction(String materialStockId, String startLocationId, String targetLocationId) {
        if (hasUnfinishedInstruction(startLocationId, targetLocationId))
            throw exception(IN_WAREHOUSE_UNFINISHED_INSTRUCTION_ORDER);

        // 生成下架指令
        InstructionDO instruction = this.createOffInstruction(materialStockId,startLocationId,targetLocationId);

        // 指令下发
        CommonResult<?> commonResult = this.instructionIssue(instruction);
        if(commonResult.isSuccess()){
            instruction.setInsStatus(DictConstants.WMS_INSTRUCTION_STATUS_RUNNING);
        }


        instructionMapper.insert(instruction);
        log.info("生成下架指令：" + instruction);
        return instruction;
    }

    @Override
    public synchronized InstructionDO offShelfInstruction(String materialStockId, String targetLocationId) {
        MaterialStockDO materialStock = materialStockService.getMaterialStockById(materialStockId);
        if(materialStock == null){
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }
        if(StringUtils.isBlank(materialStock.getRootLocationId())){
            throw exception(MATERIAL_STOCK_BIN_NOT_BIND_POSITION);
        }
        MaterialStockDO containerStock = materialStockService.getContainerStockByMaterialStock(materialStock);
        return this.offShelfInstruction(containerStock.getId(), materialStock.getRootLocationId(),targetLocationId);
    }


    /**
     * 上架指令  只需关心指令是否已下发即可 至于位置可不可用 由调用者决定
     *  上架指令无目标位置 具体位置由WCS决定
     * 1. 查询是否存在未完成的指令任务
     * 2. 若存在 则提示用户 请先完成该任务
     * 3. 若不存在 则下发上架指令
     *
     * @param materialStockId 物料库存id
     * @param startLocationId
     * @param targetLocationId
     */
    @Override
    public synchronized InstructionDO onShelfInstruction(String materialStockId, String startLocationId,String targetLocationId) {
        if (this.hasUnfinishedInstruction(startLocationId, targetLocationId))
            throw exception(IN_WAREHOUSE_UNFINISHED_INSTRUCTION_ORDER);
        // 生成上架指令
        InstructionDO instruction = this.createOnInstruction(materialStockId,startLocationId,targetLocationId);

        // 指令下发
        CommonResult<?> commonResult = this.instructionIssue(instruction);
        if(commonResult.isSuccess()){
            instruction.setInsStatus(DictConstants.WMS_INSTRUCTION_STATUS_RUNNING);
        }


        instructionMapper.insert(instruction);
        log.info("生成上架指令：" + instruction);
        return instruction;
    }
    @Override
    public synchronized InstructionDO onShelfInstruction(String materialStockId,String targetLocationId) {
        MaterialStockDO materialStock = materialStockService.getMaterialStockById(materialStockId);
        if(materialStock == null){
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }
        if(StringUtils.isBlank(materialStock.getRootLocationId())){
            throw exception(MATERIAL_STOCK_BIN_NOT_BIND_POSITION);
        }
        MaterialStockDO containerStock = materialStockService.getContainerStockByMaterialStock(materialStock);
        return this.onShelfInstruction(containerStock.getId(), materialStock.getRootLocationId(),targetLocationId);
    }

    /**
     * ②-①下架完成 测试接口
     * 1. 更新指令任务状态
     * 2. 更新物料库位
     * 3. 通知AGV 开始搬运 激活任务状态
     *
     * @param insCode 指令编码
     */
    @Override
    public void stockoffshelfComplete(String insCode) {

        InstructionDO instruction = this.updateInsStatusByInsCodeAndInsStatus(insCode, DictConstants.WMS_INSTRUCTION_STATUS_FINISHED);

        log.info("下架完成，更新指令状态：" + instruction);

        String materialStockId = instruction.getMaterialStockId();
        String targetLocationId = instruction.getTargetLocationId();
        // 物料容器id 与 目标库位 必须存在
        if (StringUtils.isBlank(materialStockId) || StringUtils.isBlank(targetLocationId)) {
            throw exception(IN_WAREHOUSE_INSTRUCTION_NOT_EXISTS);
        }


        List<MaterialStockDO> containerStockList = materialStockService.getMaterialStockByLocationId(targetLocationId);

        if(CollectionUtils.isEmpty(containerStockList)){
            // 更新物料库位
            if (!materialStockService.updateMaterialStock(materialStockId, targetLocationId)) {
                // 物料库位更新失败
                throw exception(IN_WAREHOUSE_LOCATION_UPDATE_ERROR);
            }
            log.info("更新物料库位至: " + targetLocationId);

        }else {
            if(containerStockList.size() != 1){
                // 此物料所在库位绑定多个容器 不能呼叫
                throw exception(CARRYING_TASK_LOCATION_HAS_MULTIPLE_MATERIAL_CALL);
            }
            MaterialStockDO containerStock = containerStockList.get(0);
            List<MaterialStorageDO> materialStorageList = materialStorageService.getMaterialStorageListByContainerStockId(containerStock.getId());
            if(CollectionUtils.isEmpty(materialStorageList)){
                throw exception(MATERIAL_STORAGE_TRAY_NOT_FOUND);
            }
            String storageId = materialStorageList.get(0).getId();
            // 更新物料储位
            if (!materialStockService.updateMaterialStorage(materialStockId, storageId)) {
                // 物料库位更新失败
                throw exception(IN_WAREHOUSE_LOCATION_UPDATE_ERROR);
            }
            log.info("更新物料储位至: " + storageId);

        }

        carryTaskService.dispatchNextCarryTask(instruction.getId());

    }

    /**
     * 上架完成 测试接口
     * 1. 更新指令任务状态
     * 2. 更新物料库位
     * 3. 通知AGV 开始搬运 激活任务状态
     *
     * @param insCode        指令编码
     */
    @Override
    public void stockonshelfComplete(String insCode) {

        InstructionDO instruction = this.updateInsStatusByInsCodeAndInsStatus(insCode, DictConstants.WMS_INSTRUCTION_STATUS_FINISHED);

        log.info("上架完成，更新指令状态：" + instruction);

        String containerStockId = instruction.getMaterialStockId();
        String targetLocationId = instruction.getTargetLocationId();

        // 物料容器id 与 目标库位 必须存在
        if (containerStockId == null || targetLocationId == null) {
            throw exception(IN_WAREHOUSE_INSTRUCTION_NOT_EXISTS);
        }

        // 更新物料库位
        if (!materialStockService.updateMaterialStock(containerStockId, targetLocationId)) {
            // 物料库位更新失败
            throw exception(IN_WAREHOUSE_LOCATION_UPDATE_ERROR);
        }

        carryTaskService.dispatchNextCarryTask(instruction.getId());
    }

    @Override
    public InstructionDO getInstructionByInsCode(String insCode) {
        return this.instructionMapper.selectInstructionByInsCode(insCode);
    }

    @Override
    public InstructionDO updateInsStatusByInsCodeAndInsStatus(String insCode, Integer insStatus) {
        InstructionDO instruction = this.getInstructionByInsCode(insCode);
        instruction.setInsStatus(insStatus);
    /*    if(DictConstants.WMS_INSTRUCTION_TYPE_ON == instruction.getInsType() && targetLocationId != null){
            instruction.setTargetLocationId(targetLocationId);
            String targetLocationCode = warehouseLocationService.getWarehouseLocation(targetLocationId).getLocationCode();
            String insContent = instruction.getInsContent();
            insContent += "，至目标库位：" + targetLocationCode;
            instruction.setInsContent(insContent);
        }*/
        int i = instructionMapper.updateById(instruction);
        if(i != 1){
            throw exception(INSTRUCTION_STATUS_UPDATE_ERROR);
        }
        return instruction;
    }


    @Override
    public InstructionDO getNotFinishedInstructionByMaterialStockId(String containerStockId) {
        return this.instructionMapper.selectNotFinishedInstructionByMaterialStockId(containerStockId);
    }

    @Override
    public List<InstructionDO> getNotFinishedInstruction() {
        return this.instructionMapper.selectNotFinishedInstruction();
    }

    @Override
    public CommonResult<?> instructionIssue(InstructionDO instruction) {
        if(instruction == null){
            return CommonResult.error(INSTRUCTION_NOT_EXISTS);
        }
        //todo: 指令下发 待完善 目前先返回成功
        CommonResult<InstructionDO> commonResult = CommonResult.success(instruction);
        if(commonResult.isSuccess()){
            instruction.setInsStatus(DictConstants.WMS_INSTRUCTION_STATUS_RUNNING);
            instruction.setStartTime(LocalDateTime.now());
            instructionMapper.updateById(instruction);
        }else {
            instruction.setInsStatus(DictConstants.WMS_INSTRUCTION_STATUS_EXCEPTION);
            instruction.setInsDescription("指令下发失败");
            instructionMapper.updateById(instruction);
        }
        return commonResult;
    }


    /**
     * 创建托盘搬运任务前 生成上下架指令
     * @param
     */
    public CommonResult<?> createCarrySubTaskInstruction(CarrySubTaskDO currSubTask, CarrySubTaskDO nextSubTask) {
        InstructionDO instruction = null;
        // 如果是上下架 就生成上下架指令
        if(nextSubTask.getInsType() == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_ON){
            {
                String containerStockId = nextSubTask.getMaterialStockId();
                // 起始库位 是上一个任务的 结束库位/// 因为物料可能在托盘上，并且只上架物料
                String startLocationId = currSubTask.getLocationId();
                String targetLocationId = nextSubTask.getLocationId();
                // 生成上架指令
                instruction = this.onShelfInstruction(containerStockId, startLocationId, targetLocationId);

            }
        }else if(nextSubTask.getInsType() == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_OFF){
            {
                String containerStockId = nextSubTask.getMaterialStockId();
                MaterialStockDO containerStock = materialStockService.getMaterialStock(containerStockId);
                String startLocationId = containerStock.getLocationId();
                String targetLocationId = nextSubTask.getLocationId();
                // 生成下架指令
                instruction = this.offShelfInstruction(containerStockId, startLocationId, targetLocationId);
            }
        }
        return this.instructionIssue(instruction);
    }

    @Override
    public void updateInsStatusByInsCodeAndInsStatus(String taskNo, String taskStatus, LocalDateTime parse, String message) {

        InstructionDO instruction = this.getInstructionByInsCode(taskNo);
        if(instruction == null){
            throw exception(INSTRUCTION_NOT_EXISTS);
        }
        if(DictConstants.WMS_INSTRUCTION_STATUS_START.equals(taskStatus)){
            instruction.setInsStatus(DictConstants.WMS_INSTRUCTION_STATUS_RUNNING);
            instruction.setStartTime(parse);
            instruction.setInsDescription(message);
            instructionMapper.updateById(instruction);
       }else if(DictConstants.WMS_INSTRUCTION_STATUS_END.equals(taskStatus)){
            // todo: 指令完成 待完善 目前先返回成功
            instruction.setInsStatus(DictConstants.WMS_INSTRUCTION_STATUS_FINISHED);
            instruction.setEndTime(parse);
            instruction.setInsDescription(message);
            instructionMapper.updateById(instruction);
       }else if(DictConstants.WMS_INSTRUCTION_STATUS_ALARM.equals(taskStatus)){
            instruction.setInsStatus(DictConstants.WMS_INSTRUCTION_STATUS_EXCEPTION);
            instruction.setEndTime(parse);
            instruction.setInsDescription(message);
            instructionMapper.updateById(instruction);
       }else if(DictConstants.WMS_INSTRUCTION_STATUS_CANCELl.equals(taskStatus)){
            instruction.setInsStatus(DictConstants.WMS_INSTRUCTION_STATUS_CANCEL);
            instruction.setEndTime(parse);
            instruction.setInsDescription(message);
            instructionMapper.updateById(instruction);
       }else {
           throw exception(UNKNOWN_STATUS);
       }
    }

}