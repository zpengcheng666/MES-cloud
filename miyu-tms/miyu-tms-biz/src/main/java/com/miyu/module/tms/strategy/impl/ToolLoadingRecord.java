package com.miyu.module.tms.strategy.impl;


import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServerException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.tms.controller.admin.toolgroupdetail.vo.ToolGroupDetailPageReqVO;
import com.miyu.module.tms.controller.admin.toolinfo.vo.AssembleRecordVO;
import com.miyu.module.tms.controller.admin.toolinfo.vo.ToolInfoSaveReqVO;
import com.miyu.module.tms.controller.admin.toolrecord.vo.ToolRecordSaveReqVO;
import com.miyu.module.tms.dal.dataobject.assembletask.AssembleTaskDO;
import com.miyu.module.tms.dal.dataobject.toolgroupdetail.ToolGroupDetailDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
import com.miyu.module.tms.dal.mysql.assembletask.AssembleTaskMapper;
import com.miyu.module.tms.dal.mysql.toolinfo.AssembleRecordMapper;
import com.miyu.module.tms.dal.mysql.toolinfo.ToolInfoMapper;
import com.miyu.module.tms.enums.DictConstants;
import com.miyu.module.tms.enums.ErrorCodeConstants;
import com.miyu.module.tms.service.toolgroup.ToolGroupService;
import com.miyu.module.tms.service.toolinfo.ToolInfoService;
import com.miyu.module.tms.service.toolrecord.ToolRecordService;
import com.miyu.module.tms.strategy.IAssembleRecordStrategy;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.AssembleToolReqDTO;
import com.miyu.module.wms.api.mateiral.dto.ProductToolReqDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/***
 * 装刀
 */
@Service
public class ToolLoadingRecord implements IAssembleRecordStrategy {
    @Resource
    private ToolInfoMapper toolInfoMapper;
    @Resource
    private ToolGroupService toolGroupService;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private AssembleRecordMapper assembleRecordMapper;
    @Resource
    private ToolRecordService toolRecordService;
    @Resource
    private AssembleTaskMapper assembleTaskMapper;
    @Override
    public void saveRecord(ToolInfoSaveReqVO saveReqVO) {


        ToolInfoDO toolInfoDO = toolInfoMapper.selectById(saveReqVO.getId());
        List<AssembleRecordVO> recordReqVOS = new ArrayList<>();
        recordReqVOS.addAll(saveReqVO.getToolHeadList());
        recordReqVOS.addAll(saveReqVO.getToolAccessoryList());
        recordReqVOS.addAll(saveReqVO.getToolHandleList());
        //获取刀具组装信息
        List<ToolGroupDetailDO> groupDOS = toolGroupService.getToolGroupDetailList(new ToolGroupDetailPageReqVO().setMainConfigId(toolInfoDO.getMaterialConfigId()));

        //装刀提交的时候验证
        if (saveReqVO.getSaveType().equals(2)){
            for (ToolGroupDetailDO groupDO: groupDOS){
                //根据刀具组装参数 筛选出装配记录
                if (groupDO.getSite() != null){ //如果有刀位的话  验证类型+ 刀位
                    List<AssembleRecordVO> recordReqVOList = recordReqVOS.stream().filter(assembleRecordVO -> assembleRecordVO.getAppendageMaterialNumber().equals(groupDO.getMaterialNumber())
                            && groupDO.getSite().equals(assembleRecordVO.getSite())).collect(Collectors.toList());
                    if (CollectionUtils.isAnyEmpty(recordReqVOList)){
                        throw exception(new ErrorCode(2_002_009_001, groupDO.getMaterialNumber()+"物料"+groupDO.getSite()+"刀位缺失"));
                    }
                }else {//如果没有刀位 则是配件  验证数量
                    List<AssembleRecordVO> recordReqVOList = recordReqVOS.stream().filter(assembleRecordVO -> assembleRecordVO.getAppendageMaterialNumber().equals(groupDO.getMaterialNumber())).collect(Collectors.toList());
                    if (CollectionUtils.isAnyEmpty(recordReqVOList)){
                        throw exception(new ErrorCode(2_002_009_002, groupDO.getMaterialNumber()+"配件缺失"));
                    }
                    Integer count = 0;
                    for (AssembleRecordVO recordReqVO : recordReqVOList){
                        count = count + recordReqVO.getCount();
                    }
                    if (!count.equals(groupDO.getCount())){
                        throw exception(new ErrorCode(2_002_009_003, groupDO.getMaterialNumber()+"配件数量不对"));
                    }
                }
            }
        }

        List<AssembleRecordDO> assembleRecordDOS = new ArrayList<>();
        List<AssembleToolReqDTO> assembleToolReqDTOS = new ArrayList<>();
        String userId = getLoginUserId().toString();
        for (AssembleRecordVO recordReqVO :recordReqVOS){
            AssembleRecordDO assembleRecordDO = BeanUtils.toBean(recordReqVO,AssembleRecordDO.class);
            assembleRecordDO.setToolInfoId(saveReqVO.getId());
            assembleRecordDO.setType(DictConstants.ASSEMBLE_RECORD_TYPE_CURRENT_ASSEMBLE);//1装刀  2卸刀 3 当前装
            assembleRecordDO.setOperator(userId);
            assembleRecordDOS.add(assembleRecordDO);

            AssembleToolReqDTO assembleToolReqDTO = new AssembleToolReqDTO();
            assembleToolReqDTO.setMaterialStockId(recordReqVO.getMaterialStockId());
            assembleToolReqDTO.setQuantity(recordReqVO.getCount());
            assembleToolReqDTO.setStorageId(recordReqVO.getStorageId());
            assembleToolReqDTOS.add(assembleToolReqDTO);
        }

        //提交的时候需要
        if (saveReqVO.getSaveType().equals(2)){
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
                throw exception(ErrorCodeConstants.BUG);
            }
            if (!result.getCheckedData()){
                throw exception(new ErrorCode(2_002_009_004, "配件库存删除失败"));
            }
            toolInfoDO.setStatus(DictConstants.PARTS_STATUS_COMPLETED);//装配完成

        }

        //装刀  先删除装刀记录
        assembleRecordMapper.deleteByToolInfo1(saveReqVO.getId());
        //保存
        assembleRecordMapper.insertBatch(assembleRecordDOS);
        if (saveReqVO.getSaveType().equals(2)){
            toolInfoMapper.updateById(toolInfoDO);

            //提交时保存刀具使用记录
            ToolRecordSaveReqVO toolRecordSaveReqVO = new ToolRecordSaveReqVO();
            toolRecordSaveReqVO.setToolInfoId(toolInfoDO.getId());
            toolRecordSaveReqVO.setField(saveReqVO.getStorageId());
            toolRecordSaveReqVO.setStartTime(toolInfoDO.getCreateTime());
            toolRecordSaveReqVO.setEndTime(LocalDateTime.now());
            toolRecordSaveReqVO.setType(1);//装配
            toolRecordService.createToolRecord(toolRecordSaveReqVO);
            MPJLambdaWrapperX<ToolInfoDO> wrapperX = new MPJLambdaWrapperX<>();
            wrapperX.eq(ToolInfoDO::getAssembleTaskId,toolInfoDO.getAssembleTaskId());
            List<ToolInfoDO> toolInfoDOS = toolInfoMapper.selectList(wrapperX);
            Boolean isFinish = true;
            for (ToolInfoDO infoDO :toolInfoDOS){
                if (!infoDO.getId().equals(toolInfoDO.getId())){

                    if (!infoDO.getStatus().equals(DictConstants.PARTS_STATUS_COMPLETED)){
                        isFinish =false;
                    }

                }
            }

            if (isFinish){
                AssembleTaskDO assembleTaskDO = new AssembleTaskDO();
                assembleTaskDO.setId(toolInfoDO.getAssembleTaskId());
                assembleTaskDO.setStatus(3);// 1待接单  2 装配中  3 已完成  4作废
                assembleTaskMapper.updateById(assembleTaskDO);
            }
        }
    }
}
