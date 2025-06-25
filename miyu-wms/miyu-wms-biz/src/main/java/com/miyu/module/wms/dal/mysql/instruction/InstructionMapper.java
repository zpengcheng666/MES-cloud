package com.miyu.module.wms.dal.mysql.instruction;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.miyu.module.wms.dal.dataobject.instruction.InstructionDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.instruction.vo.*;

/**
 * 指令 Mapper
 *
 * @author 王正浩
 */
@Mapper
public interface InstructionMapper extends BaseMapperX<InstructionDO> {

    default PageResult<InstructionDO> selectPage(InstructionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InstructionDO>()
                .betweenIfPresent(InstructionDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(InstructionDO::getInsCode, reqVO.getInsCode())
                .eqIfPresent(InstructionDO::getMaterialStockId, reqVO.getMaterialStockId())
                .eqIfPresent(InstructionDO::getInsType, reqVO.getInsType())
                .eqIfPresent(InstructionDO::getInsStatus, reqVO.getInsStatus())
                .eqIfPresent(InstructionDO::getStartLocationId, reqVO.getStartLocationId())
                .eqIfPresent(InstructionDO::getTargetLocationId, reqVO.getTargetLocationId())
                .eqIfPresent(InstructionDO::getInsContent, reqVO.getInsContent())
                .eqIfPresent(InstructionDO::getInsDescription, reqVO.getInsDescription())
                .orderByDesc(InstructionDO::getId));
    }

    default InstructionDO selectInstructionByInsCode(String insCode){
        return selectOne(InstructionDO::getInsCode, insCode);
    }

    default InstructionDO selectNotFinishedInstructionByMaterialStockId(String containerStockId){
        return selectOne(new LambdaQueryWrapperX<InstructionDO>()
                .eq(InstructionDO::getMaterialStockId, containerStockId)
                .and(wrapper -> wrapper
                        .ne(InstructionDO::getInsStatus, DictConstants.WMS_INSTRUCTION_STATUS_FINISHED)
                        .ne(InstructionDO::getInsStatus, DictConstants.WMS_INSTRUCTION_STATUS_CANCEL)));
    }

    default List<InstructionDO> selectNotFinishedInstruction(){
        return selectList(new LambdaQueryWrapperX<InstructionDO>()
                .ne(InstructionDO::getInsStatus, DictConstants.WMS_INSTRUCTION_STATUS_FINISHED)
                .ne(InstructionDO::getInsStatus, DictConstants.WMS_INSTRUCTION_STATUS_CANCEL));
    }
}