package com.miyu.module.tms.strategy.impl;


import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServerException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.tms.controller.admin.toolgroupdetail.vo.ToolGroupDetailPageReqVO;
import com.miyu.module.tms.controller.admin.toolinfo.vo.AssembleRecordVO;
import com.miyu.module.tms.controller.admin.toolinfo.vo.ToolInfoSaveReqVO;
import com.miyu.module.tms.controller.admin.toolrecord.vo.ToolRecordSaveReqVO;
import com.miyu.module.tms.dal.dataobject.toolgroupdetail.ToolGroupDetailDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.miyu.module.tms.enums.ErrorCodeConstants.PART_STOCK_RESTORE_FAIL;

/***
 * 维修
 */
@Service
public class ToolRepairsRecord implements IAssembleRecordStrategy {
    @Resource
    private ToolInfoMapper toolInfoMapper;
    @Resource
    private ToolGroupService toolGroupService;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private ToolInfoService toolInfoService;

    @Resource
    private ToolRecordService toolRecordService;
    @Override
    public void saveRecord(ToolInfoSaveReqVO saveReqVO) {


        ToolInfoDO toolInfoDO = toolInfoMapper.selectById(saveReqVO.getId());
        List<AssembleRecordVO> recordReqVOS = new ArrayList<>();
        if (!CollectionUtils.isAnyEmpty(saveReqVO.getToolHeadList())) {
            recordReqVOS.addAll(saveReqVO.getToolHeadList());
        }
        if (!CollectionUtils.isAnyEmpty(saveReqVO.getToolAccessoryList())){
            recordReqVOS.addAll(saveReqVO.getToolAccessoryList());
        }
        if (!CollectionUtils.isAnyEmpty(saveReqVO.getToolHandleList())){
            recordReqVOS.addAll(saveReqVO.getToolHandleList());
        }

        List<AssembleRecordDO> assembleRecordDOS = new ArrayList<>();
        String userId = getLoginUserId().toString();
        for (AssembleRecordVO recordReqVO :recordReqVOS){
            //如果没有ID 说明是换的新刀
            if(StringUtils.isEmpty(recordReqVO.getId())){
                AssembleRecordDO assembleRecordDO = BeanUtils.toBean(recordReqVO,AssembleRecordDO.class);
                assembleRecordDO.setToolInfoId(saveReqVO.getId());
                assembleRecordDO.setType(DictConstants.ASSEMBLE_RECORD_TYPE_CURRENT_ASSEMBLE);//1装刀  2卸刀 3 当前装 4 报废
                assembleRecordDO.setOperator(userId);
                assembleRecordDOS.add(assembleRecordDO);
            }

        }
        List<ToolGroupDetailDO> groupDOS = toolGroupService.getToolGroupDetailList(new ToolGroupDetailPageReqVO().setMainConfigId(toolInfoDO.getMaterialConfigId()));
        Boolean isFinish = true;
        //维修提交的时候 验证是不是装完
        for (ToolGroupDetailDO groupDO: groupDOS){
            //根据刀具组装参数 筛选出装配记录
            if (groupDO.getSite() != null){ //如果有刀位的话  验证类型+ 刀位
                List<AssembleRecordVO> recordReqVOList = recordReqVOS.stream().filter(assembleRecordVO -> assembleRecordVO.getAppendageMaterialNumber().equals(groupDO.getMaterialNumber())
                        && groupDO.getSite().equals(assembleRecordVO.getSite())).collect(Collectors.toList());
                if (CollectionUtils.isAnyEmpty(recordReqVOList)){
                    isFinish = false;
                   }
            }else {//如果没有刀位 则是配件  验证数量
                List<AssembleRecordVO> recordReqVOList = recordReqVOS.stream().filter(assembleRecordVO -> assembleRecordVO.getAppendageMaterialNumber().equals(groupDO.getMaterialNumber())).collect(Collectors.toList());
                if (CollectionUtils.isAnyEmpty(recordReqVOList)){
                    isFinish = false;
                }
                Integer count = 0;
                for (AssembleRecordVO recordReqVO : recordReqVOList){
                    count = count + recordReqVO.getCount();
                }
                if (!count.equals(groupDO.getCount())){
                    isFinish = false;
                }
            }
        }

        if (!CollectionUtils.isAnyEmpty(saveReqVO.getRemoveList())){
            List<AssembleToolReqDTO> assembleToolReqDTOS = new ArrayList<>();
            List<AssembleRecordDO> recordDOS = new ArrayList<>();
            for (AssembleRecordVO recordReqVO :saveReqVO.getRemoveList()){

                AssembleRecordDO assembleRecordDO = BeanUtils.toBean(recordReqVO,AssembleRecordDO.class);
                assembleRecordDO.setToolInfoId(saveReqVO.getId());
                assembleRecordDO.setType(recordReqVO.getType());//1装刀  2卸刀 3 当前装 4 报废
                assembleRecordDO.setOperator(userId);
                recordDOS.add(assembleRecordDO);

               if (recordReqVO.getType().equals(DictConstants.ASSEMBLE_RECORD_TYPE_SELL)){ //如果是卸刀  则需要恢复库存
                   AssembleToolReqDTO assembleToolReqDTO = new AssembleToolReqDTO();
                   assembleToolReqDTO.setMaterialStockId(recordReqVO.getMaterialStockId());
                   assembleToolReqDTO.setQuantity(recordReqVO.getCount());
                   assembleToolReqDTO.setStorageId(recordReqVO.getStorageId());
                   assembleToolReqDTOS.add(assembleToolReqDTO);

               }

            }
            //2.配件恢复
            if (!CollectionUtils.isAnyEmpty(assembleToolReqDTOS)){
                CommonResult<Boolean> commonResult = null;
                try {
                    commonResult =  materialStockApi.assembleOrRecoveryMaterial(assembleToolReqDTOS);
                } catch (Exception e) {
                    throw exception(ErrorCodeConstants.BUG);
                }
                if (!commonResult.isSuccess()){
                    throw exception(PART_STOCK_RESTORE_FAIL);
                }
            }

            //更新状态
            //增加卸刀记录
            toolInfoService.batchCreateAssembleRecord(recordDOS);
        }

        if (isFinish){ //如果配件齐全  则说明维修完成    否则说明正在维修
            toolInfoDO.setStatus(DictConstants.PARTS_STATUS_COMPLETED);

            //提交时保存刀具使用记录
            ToolRecordSaveReqVO toolRecordSaveReqVO = new ToolRecordSaveReqVO();
            toolRecordSaveReqVO.setToolInfoId(toolInfoDO.getId());
//            toolRecordSaveReqVO.setField(saveReqVO.getStorageId());
//            toolRecordSaveReqVO.setStartTime(toolInfoDO.getCreateTime());
//            toolRecordSaveReqVO.setEndTime(LocalDateTime.now()); b
            toolRecordSaveReqVO.setType(10);//维修
            toolRecordService.createToolRecord(toolRecordSaveReqVO);

        }else {
            toolInfoDO.setStatus(DictConstants.PARTS_STATUS_ASSEMBLING);
        }

        toolInfoMapper.updateById(toolInfoDO);

    }
}
