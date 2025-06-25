package com.miyu.module.tms.strategy.impl;


import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServerException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.tms.controller.admin.toolinfo.vo.AssembleRecordVO;
import com.miyu.module.tms.controller.admin.toolinfo.vo.ToolInfoSaveReqVO;
import com.miyu.module.tms.controller.admin.toolrecord.vo.ToolRecordSaveReqVO;
import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
import com.miyu.module.tms.dal.mysql.toolinfo.AssembleRecordMapper;
import com.miyu.module.tms.dal.mysql.toolinfo.ToolInfoMapper;
import com.miyu.module.tms.enums.DictConstants;
import com.miyu.module.tms.enums.ErrorCodeConstants;
import com.miyu.module.tms.service.toolinfo.ToolInfoService;
import com.miyu.module.tms.service.toolrecord.ToolRecordService;
import com.miyu.module.tms.strategy.IAssembleRecordStrategy;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.AssembleToolReqDTO;
import com.miyu.module.wms.api.mateiral.dto.ProductToolReqDTO;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.miyu.module.tms.enums.ErrorCodeConstants.PART_STOCK_RESTORE_FAIL;
import static com.miyu.module.tms.enums.ErrorCodeConstants.TOOL_DELETE_FAIL;

/****
 * 卸刀
 */
@Service
@GlobalTransactional(rollbackFor = Exception.class)
public class ToolUnLoadingRecord implements IAssembleRecordStrategy {
    @Resource
    private ToolInfoMapper toolInfoMapper;

    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private AssembleRecordMapper assembleRecordMapper;
    @Resource
    private ToolInfoService toolInfoService;
    @Resource
    private ToolRecordService toolRecordService;
    @Override
    public void saveRecord(ToolInfoSaveReqVO saveReqVO) {

        ToolInfoDO toolInfoDO = toolInfoMapper.selectById(saveReqVO.getId());

        List<AssembleRecordVO> removeList = saveReqVO.getRemoveList();
        List<AssembleRecordDO> assembleRecordDOS = new ArrayList<>();
        List<AssembleToolReqDTO> assembleToolReqDTOS = new ArrayList<>();
        String userId = String.valueOf(getLoginUserId());
        // 是否全部拆卸
        boolean allDisassemble = true;
        for (AssembleRecordVO recordReqVO :removeList){
            // 位置信息不存在 并且 不是作废
            if (StringUtils.isEmpty(recordReqVO.getStorageId()) &&!recordReqVO.getType().equals(DictConstants.ASSEMBLE_RECORD_TYPE_INVALID)){
                allDisassemble = false;
                continue;
            }

            // 如果状态是 当前装 则跳过
            if(recordReqVO.getType().equals(DictConstants.ASSEMBLE_RECORD_TYPE_CURRENT_ASSEMBLE)){
                continue;
            }
            AssembleRecordDO assembleRecordDO = BeanUtils.toBean(recordReqVO,AssembleRecordDO.class);
            assembleRecordDO.setToolInfoId(saveReqVO.getId());
            assembleRecordDO.setType(recordReqVO.getType());//1装刀  2卸刀 3 当前装 4 报废
            assembleRecordDO.setOperator(userId);
            assembleRecordDOS.add(assembleRecordDO);

            if (!recordReqVO.getType().equals(DictConstants.ASSEMBLE_RECORD_TYPE_INVALID)){//如果不是作废的  需要恢复库存
                AssembleToolReqDTO assembleToolReqDTO = new AssembleToolReqDTO();
                assembleToolReqDTO.setMaterialStockId(recordReqVO.getMaterialStockId());
                assembleToolReqDTO.setQuantity(recordReqVO.getCount());
                assembleToolReqDTO.setStorageId(recordReqVO.getStorageId());
                assembleToolReqDTOS.add(assembleToolReqDTO);
            }

        }
        //提交的时候需要
        // 如果全部拆卸完成 则删除成品刀
        if(allDisassemble){
            //1.成品刀删除
            ProductToolReqDTO reqDTO = new ProductToolReqDTO();
            reqDTO.setMaterialStockId(toolInfoDO.getMaterialStockId());
            reqDTO.setQuantity(1);
            CommonResult<String> result = null;
            try {
                result =  materialStockApi.generateOrDisassembleProductTool(reqDTO);
            } catch (Exception e) {
                throw exception(ErrorCodeConstants.BUG);
            }
            if (!result.isSuccess()){
                throw exception(TOOL_DELETE_FAIL);
            }

            //提交时保存刀具使用记录
            ToolRecordSaveReqVO toolRecordSaveReqVO = new ToolRecordSaveReqVO();
            toolRecordSaveReqVO.setToolInfoId(toolInfoDO.getId());
//            toolRecordSaveReqVO.setField(saveReqVO.getStorageId());
//            toolRecordSaveReqVO.setStartTime(toolInfoDO.getCreateTime());
//            toolRecordSaveReqVO.setEndTime(LocalDateTime.now());
            toolRecordSaveReqVO.setType(2);//拆卸
            toolRecordService.createToolRecord(toolRecordSaveReqVO);


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
        toolInfoService.batchCreateAssembleRecord(assembleRecordDOS);

        if(allDisassemble){
            toolInfoDO.setStatus(DictConstants.PARTS_STATUS_INVALID);//5无效
        }else {
            toolInfoDO.setStatus(DictConstants.PARTS_STATUS_DISASSEMBLING);//拆卸中
        }
        toolInfoMapper.updateById(toolInfoDO);
    }
}
