package com.miyu.module.tms.service.toolinfo;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServerException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.tms.controller.admin.assembletask.vo.AssembleTaskPageReqVO;
import com.miyu.module.tms.controller.admin.toolgroupdetail.vo.ToolGroupDetailPageReqVO;
import com.miyu.module.tms.dal.dataobject.toolgroupdetail.ToolGroupDetailDO;
import com.miyu.module.tms.enums.DictConstants;
import com.miyu.module.tms.enums.ErrorCodeConstants;
import com.miyu.module.tms.service.toolgroup.ToolGroupService;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.AssembleToolReqDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigRespDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.mateiral.dto.ProductToolReqDTO;
import com.miyu.module.wms.api.warehouse.dto.WarehouseLocationRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.tms.controller.admin.toolinfo.vo.*;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolBalanceDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolBaseDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.tms.dal.mysql.toolinfo.ToolInfoMapper;
import com.miyu.module.tms.dal.mysql.toolinfo.ToolBalanceMapper;
import com.miyu.module.tms.dal.mysql.toolinfo.ToolBaseMapper;
import com.miyu.module.tms.dal.mysql.toolinfo.AssembleRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.miyu.module.tms.enums.ErrorCodeConstants.*;

/**
 * 刀组信息 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class ToolInfoServiceImpl implements ToolInfoService {

    @Resource
    private ToolInfoMapper toolInfoMapper;
    @Resource
    private ToolBalanceMapper toolBalanceMapper;
    @Resource
    private ToolBaseMapper toolBaseMapper;
    @Resource
    private AssembleRecordMapper assembleRecordMapper;
    @Resource
    private ToolGroupService groupService;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private MaterialConfigApi materialConfigApi;

    @Override
    public String createToolInfo(ToolInfoSaveReqVO createReqVO) {
        // 插入
        ToolInfoDO toolInfo = BeanUtils.toBean(createReqVO, ToolInfoDO.class);
        toolInfoMapper.insert(toolInfo);
        // 返回
        return toolInfo.getId();
    }

    @Override
    public void updateToolInfo(ToolInfoSaveReqVO updateReqVO) {
        // 校验存在
        validateToolInfoExists(updateReqVO.getId());
        // 更新
        ToolInfoDO updateObj = BeanUtils.toBean(updateReqVO, ToolInfoDO.class);
        toolInfoMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteToolInfo(String id) {
        // 校验存在
        validateToolInfoExists(id);
        // 删除
        toolInfoMapper.deleteById(id);

        // 删除子表
        deleteToolBalanceByToolInfoId(id);
        deleteToolBaseByToolInfoId(id);
        deleteAssembleRecordByToolInfoId(id);
    }

    private void validateToolInfoExists(String id) {
        if (toolInfoMapper.selectById(id) == null) {
            throw exception(TOOL_INFO_NOT_EXISTS);
        }
    }

    @Override
    public ToolInfoDO getToolInfo(String id) {
        return toolInfoMapper.selectById(id);
    }

    @Override
    public PageResult<ToolInfoDO> getToolInfoPage(ToolInfoPageReqVO pageReqVO) {
        PageResult<ToolInfoDO> pageResult = toolInfoMapper.selectPage(pageReqVO);
        if(pageResult.getTotal() > 0){
            HashSet<String> materialConfigIds = new HashSet<>();
            HashSet<String> materialStockIds = new HashSet<>();
            pageResult.getList().forEach(item -> {
                materialConfigIds.add(item.getMaterialConfigId());
                materialStockIds.add(item.getMaterialStockId());
            });
            //获取物料类型信息
            Map<String, MaterialConfigRespDTO> materialConfigMap = materialConfigApi.getMaterialConfigMap(materialConfigIds);
            //获取物料库存信息
            CommonResult<List<MaterialStockRespDTO>> materialResult = materialStockApi.getMaterialsAndLocationByIds(materialStockIds);
            Map<String, MaterialStockRespDTO> materialMap;
            if(materialResult.isSuccess() && materialResult.getData() != null){
                materialMap = CollectionUtils.convertMap(materialResult.getData(), MaterialStockRespDTO::getId);
            } else {
                materialMap = null;
            }

            pageResult.getList().forEach(item -> {
                if(materialConfigMap != null && materialConfigMap.containsKey(item.getMaterialConfigId())){
                    item.setMaterialNumber(materialConfigMap.get(item.getMaterialConfigId()).getMaterialNumber());
                    item.setMaterialName(materialConfigMap.get(item.getMaterialConfigId()).getMaterialName());
                }
                if(materialMap != null && materialMap.containsKey(item.getMaterialStockId())){
                    item.setBarCode(materialMap.get(item.getMaterialStockId()).getBarCode());
                    item.setStorageCode(materialMap.get(item.getMaterialStockId()).getStorageCode());
                    item.setRootLocationCode(materialMap.get(item.getMaterialStockId()).getRootLocationCode());
                }

            });

        }
        return pageResult;
    }

    // ==================== 子表（刀具动平衡） ====================

    @Override
    public PageResult<ToolBalanceDO> getToolBalancePage(PageParam pageReqVO, String toolInfoId) {
        return toolBalanceMapper.selectPage(pageReqVO, toolInfoId);
    }

    @Override
    public String createToolBalance(ToolBalanceDO toolBalance) {
        // 校验是否已经存在
        if (toolBalanceMapper.selectByToolInfoId(toolBalance.getToolInfoId()) != null) {
            throw exception(TOOL_BALANCE_EXISTS);
        }
        // 插入
        toolBalanceMapper.insert(toolBalance);
        return toolBalance.getId();
    }

    @Override
    public void updateToolBalance(ToolBalanceDO toolBalance) {
        // 校验存在
        validateToolBalanceExists(toolBalance.getId());
        // 更新
        toolBalanceMapper.updateById(toolBalance);
    }

    @Override
    public void deleteToolBalance(String id) {
        // 校验存在
        validateToolBalanceExists(id);
        // 删除
        toolBalanceMapper.deleteById(id);
    }

    @Override
    public ToolBalanceDO getToolBalance(String id) {
        return toolBalanceMapper.selectById(id);
    }

    private void validateToolBalanceExists(String id) {
        if (toolBalanceMapper.selectById(id) == null) {
            throw exception(TOOL_BALANCE_NOT_EXISTS);
        }
    }

    private void deleteToolBalanceByToolInfoId(String toolInfoId) {
        toolBalanceMapper.deleteByToolInfoId(toolInfoId);
    }

    // ==================== 子表（对刀数据） ====================

    @Override
    public PageResult<ToolBaseDO> getToolBasePage(PageParam pageReqVO, String toolInfoId) {
        return toolBaseMapper.selectPage(pageReqVO, toolInfoId);
    }

    @Override
    public String createToolBase(ToolBaseDO toolBase) {
        // 校验是否已经存在
        if (toolBaseMapper.selectByToolInfoId(toolBase.getToolInfoId()) != null) {
            throw exception(TOOL_BASE_EXISTS);
        }
        // 插入
        toolBaseMapper.insert(toolBase);
        return toolBase.getId();
    }

    @Override
    public void updateToolBase(ToolBaseDO toolBase) {
        // 校验存在
        validateToolBaseExists(toolBase.getId());
        // 更新
        toolBaseMapper.updateById(toolBase);
    }

    @Override
    public void deleteToolBase(String id) {
        // 校验存在
        validateToolBaseExists(id);
        // 删除
        toolBaseMapper.deleteById(id);
    }

    @Override
    public ToolBaseDO getToolBase(String id) {
        return toolBaseMapper.selectById(id);
    }

    private void validateToolBaseExists(String id) {
        if (toolBaseMapper.selectById(id) == null) {
            throw exception(TOOL_BASE_NOT_EXISTS);
        }
    }

    private void deleteToolBaseByToolInfoId(String toolInfoId) {
        toolBaseMapper.deleteByToolInfoId(toolInfoId);
    }

    // ==================== 子表（刀具装配记录） ====================

    @Override
    public PageResult<AssembleRecordDO> getAssembleRecordPage(PageParam pageReqVO, String toolInfoId) {
        return assembleRecordMapper.selectPage(pageReqVO, toolInfoId);
    }

    @Override
    public String createAssembleRecord(AssembleRecordDO assembleRecord) {
        assembleRecordMapper.insert(assembleRecord);
        return assembleRecord.getId();
    }

    @Override
    public void updateAssembleRecord(AssembleRecordDO assembleRecord) {
        // 校验存在
        validateAssembleRecordExists(assembleRecord.getId());
        // 更新
        assembleRecordMapper.updateById(assembleRecord);
    }

    @Override
    public void deleteAssembleRecord(String id) {
        // 校验存在
        validateAssembleRecordExists(id);
        // 删除
        assembleRecordMapper.deleteById(id);
    }

    @Override
    public AssembleRecordDO getAssembleRecord(String id) {
        return assembleRecordMapper.selectById(id);
    }

    private void validateAssembleRecordExists(String id) {
        if (assembleRecordMapper.selectById(id) == null) {
            throw exception(ASSEMBLE_RECORD_NOT_EXISTS);
        }
    }

    private void deleteAssembleRecordByToolInfoId(String toolInfoId) {
        assembleRecordMapper.deleteByToolInfoId(toolInfoId);
    }

    @Override
    public PageResult<ToolInfoDO> getAssembleTaskRecordPage(AssembleTaskPageReqVO pageReqVO) {
        return toolInfoMapper.selectAssembleTaskRecordPage(pageReqVO);
    }
//    @Override
//    public List<ToolInfoDO> getAssembleTaskRecordPage2(AssembleTaskPageReqVO pageReqVO) {
//        List<ToolInfoDO> toolInfoDOS = toolInfoMapper.selectAssembleTaskRecordPage2(pageReqVO);
//        List<ToolInfoDO> result = new ArrayList<>();
//        Set<String> toolInfoIdSet = new HashSet<>();
//        // 将数据根据 id 分组
//        toolInfoDOS.forEach(toolInfoDO -> {
//            if (!toolInfoIdSet.contains(toolInfoDO.getId())) {
//                toolInfoIdSet.add(toolInfoDO.getId());
//                result.add(toolInfoDO);
//            }
//        });
//
//        return result;
//    }

    @Override
    public List<AssembleRecordDO> getAssembleRecordListByAssembleTaskId(String assembleTaskId) {
        return assembleRecordMapper.selectAssembleRecordListByAssembleTaskId(assembleTaskId);
    }

    @Override
    public List<ToolInfoDO> getToolInfoById(String id) {
        return toolInfoMapper.getToolInfoById(id);
    }

    @Override
    public void saveUpdateAssembleRecord(ToolInfoSaveReqVO saveReqVO) {
        if (CollectionUtils.isAnyEmpty(saveReqVO.getToolAccessoryList()) && CollectionUtils.isAnyEmpty(saveReqVO.getToolHeadList()) && CollectionUtils.isAnyEmpty(saveReqVO.getToolHandleList())){
            //return error(ErrorCodeConstants.PARAM_NOT_NULL);
            throw new ServerException(ErrorCodeConstants.PARAM_NOT_NULL);
        }


        ToolInfoDO toolInfoDO = this.getToolInfo(saveReqVO.getId());

//        if (StringUtils.isEmpty(toolInfoDO.getMaterialStockId()) && saveReqVO.getSaveType().equals(2)){
//            //TODO 如果没有创建成品刀的库存 需要创建成品刀的库存
//
//        }
        List<AssembleRecordVO> recordReqVOS = new ArrayList<>();
        recordReqVOS.addAll(saveReqVO.getToolHeadList());
        recordReqVOS.addAll(saveReqVO.getToolAccessoryList());
        recordReqVOS.addAll(saveReqVO.getToolHandleList());

        //获取刀具组装信息
        List<ToolGroupDetailDO> groupDOS = groupService.getToolGroupDetailList(new ToolGroupDetailPageReqVO().setMainConfigId(toolInfoDO.getMaterialConfigId()));

        //装刀提交的时候验证
        if (saveReqVO.getSaveType().equals(2) && saveReqVO.getType().equals(1)){
            for (ToolGroupDetailDO groupDO: groupDOS){
                //根据刀具组装参数 筛选出装配记录
                if (groupDO.getSite() != null){ //如果有刀位的话  验证类型+ 刀位
                    List<AssembleRecordVO> recordReqVOList = recordReqVOS.stream().filter(assembleRecordVO -> assembleRecordVO.getAppendageMaterialNumber().equals(groupDO.getMaterialNumber())
                            && groupDO.getSite().equals(assembleRecordVO.getSite())).collect(Collectors.toList());
                    if (CollectionUtils.isAnyEmpty(recordReqVOList)){
                        throw new ServerException(new ErrorCode(2_002_009_001, groupDO.getMaterialNumber()+"物料"+groupDO.getSite()+"刀位缺失"));
                    }
                }else {//如果没有刀位 则是配件  验证数量
                    List<AssembleRecordVO> recordReqVOList = recordReqVOS.stream().filter(assembleRecordVO -> assembleRecordVO.getAppendageMaterialNumber().equals(groupDO.getMaterialNumber())).collect(Collectors.toList());
                    if (CollectionUtils.isAnyEmpty(recordReqVOList)){
                        throw new ServerException(new ErrorCode(2_002_009_002, groupDO.getMaterialNumber()+"配件缺失"));
                    }
                    Integer count = 0;
                    for (AssembleRecordVO recordReqVO : recordReqVOList){
                        count = count + recordReqVO.getCount();
                    }
                    if (!count.equals(groupDO.getCount())){
                        throw new ServerException(new ErrorCode(2_002_009_003, groupDO.getMaterialNumber()+"配件数量不对"));
                    }
                }
            }
        }

        List<AssembleRecordDO> assembleRecordDOS = new ArrayList<>();
        List<AssembleToolReqDTO> assembleToolReqDTOS = new ArrayList<>();
        for (AssembleRecordVO recordReqVO :recordReqVOS){
            AssembleRecordDO assembleRecordDO = BeanUtils.toBean(recordReqVO,AssembleRecordDO.class);
            assembleRecordDO.setToolInfoId(saveReqVO.getId());
            assembleRecordDO.setType(saveReqVO.getType());//1装刀  2卸刀
            assembleRecordDO.setOperator(getLoginUserId().toString());
            assembleRecordDOS.add(assembleRecordDO);

            AssembleToolReqDTO assembleToolReqDTO = new AssembleToolReqDTO();
            assembleToolReqDTO.setMaterialStockId(recordReqVO.getMaterialStockId());
            assembleToolReqDTO.setQuantity(recordReqVO.getCount());
            assembleToolReqDTO.setStorageId(recordReqVO.getStorageId());
            assembleToolReqDTOS.add(assembleToolReqDTO);
        }

        //提交的时候需要
        if (saveReqVO.getSaveType().equals(2)){

            if (saveReqVO.getType().equals(1)){//如果是装刀  需要删除库存
                //装刀 1 生成成品刀
                ProductToolReqDTO reqDTO = new ProductToolReqDTO();
                reqDTO.setMaterialConfigId(toolInfoDO.getMaterialConfigId());
                reqDTO.setStorageId(saveReqVO.getStorageId());
                try {
                    CommonResult<String> result =  materialStockApi.generateOrDisassembleProductTool(reqDTO);
                    toolInfoDO.setMaterialStockId(result.getCheckedData());
                } catch (Exception e) {
                    throw new ServerException(ErrorCodeConstants.BUG);
                }

                //2配件删除
                CommonResult<Boolean> result = null;
                try {
                   result =  materialStockApi.assembleOrRecoveryMaterial(assembleToolReqDTOS);
                } catch (Exception e) {
                    throw new ServerException(ErrorCodeConstants.BUG);
                }
                if (!result.getCheckedData()){
                    throw new ServerException(new ErrorCode(2_002_009_004, "配件库存删除失败"));
                }
                toolInfoDO.setStatus(2);//装配完成
            }else {//卸刀
                //1.成品刀删除
                ProductToolReqDTO reqDTO = new ProductToolReqDTO();
                reqDTO.setMaterialStockId(toolInfoDO.getMaterialStockId());
                reqDTO.setQuantity(1);
                try {
                    CommonResult<String> result =  materialStockApi.generateOrDisassembleProductTool(reqDTO);
                } catch (Exception e) {
                    throw new ServerException(ErrorCodeConstants.BUG);
                }

                //2.配件恢复
                CommonResult<Boolean> result = null;
                try {
                    result =  materialStockApi.assembleOrRecoveryMaterial(assembleToolReqDTOS);
                } catch (Exception e) {
                    throw new ServerException(ErrorCodeConstants.BUG);
                }
                if (!result.getCheckedData()){
                    throw new ServerException(new ErrorCode(2_002_009_004, "配件库存恢复失败"));
                }
            }


        }


        //装刀  先删除装刀记录
        if (saveReqVO.getType().equals(1)){
            assembleRecordMapper.deleteByToolInfo(saveReqVO.getId(),saveReqVO.getType());
        }

        //保存
        assembleRecordMapper.insertBatch(assembleRecordDOS);
        if (saveReqVO.getSaveType().equals(2)){
            toolInfoMapper.updateById(toolInfoDO);
        }
    }

    @Override
    public List<AssembleRecordDO> getAssembleRecordByIds(Collection<String> ids, Integer type) {
        return assembleRecordMapper.selectAssembleRecordByIds(ids, type);
    }


    @Override
    public List<AssembleRecordDO> getAssembleRecordListByToolInfoId(String toolInfoId,Integer type) {
        return assembleRecordMapper.selectAssembleRecordByToolInfoId(toolInfoId, type);
    }

    @Override
    public List<AssembleRecordDO> getCurrentAssembleRecordByToolInfoId(String toolInfoId) {
        return assembleRecordMapper.selectCurrentAssembleRecordByToolInfoId(toolInfoId);
    }


    @Override
    public Boolean batchCreateAssembleRecord(Collection<AssembleRecordDO> assembleRecordDOS){
        List<AssembleRecordDO> assembleRecordList = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        assembleRecordDOS.forEach(item -> {
            ids.add(item.getId());
            item.setId(null);
            assembleRecordList.add(item);
        });
        // 卸刀
        assembleRecordMapper.insertBatch(assembleRecordList);
        //更新装刀记录 状态  当前装→转
        if(!CollectionUtils.isAnyEmpty(assembleRecordDOS)){
            return assembleRecordMapper.updateStatusBatch(ids, DictConstants.ASSEMBLE_RECORD_TYPE_CURRENT_ASSEMBLE,DictConstants.ASSEMBLE_RECORD_TYPE_ASSEMBLE)>0;
        }
        return true;
    }


}
