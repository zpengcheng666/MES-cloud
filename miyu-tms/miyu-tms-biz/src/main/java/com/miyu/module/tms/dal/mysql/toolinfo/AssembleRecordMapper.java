package com.miyu.module.tms.dal.mysql.toolinfo;

import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.module.tms.controller.admin.toolinfo.vo.AssembleRecordVO;
import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
import com.miyu.module.tms.enums.DictConstants;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

/**
 * 刀具装配记录 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface AssembleRecordMapper extends BaseMapperX<AssembleRecordDO> {

    default PageResult<AssembleRecordDO> selectPage(PageParam reqVO, String toolInfoId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AssembleRecordDO>()
            .eq(AssembleRecordDO::getToolInfoId, toolInfoId)
            .orderByDesc(AssembleRecordDO::getId));
    }

    default int deleteByToolInfoId(String toolInfoId) {
        return delete(AssembleRecordDO::getToolInfoId, toolInfoId);
    }

    default int deleteByToolInfo(String toolInfoId,Integer type) {
        MPJLambdaWrapperX<AssembleRecordDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(AssembleRecordDO::getToolInfoId, toolInfoId)
                .eq(AssembleRecordDO::getType ,type);
        return delete(wrapperX);
    }


    @Delete("delete from tms_assemble_record where tool_info_id = #{toolInfoId} && type = 3")
    void deleteByToolInfo1(String toolInfoId);


    default List<AssembleRecordDO> selectAssembleRecordListByAssembleTaskId(String assembleTaskId){
        MPJLambdaWrapperX<AssembleRecordDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ToolInfoDO.class, ToolInfoDO::getId, AssembleRecordDO::getToolInfoId)
                .eq(ToolInfoDO::getAssembleTaskId, assembleTaskId);
        return selectList(wrapperX);
    }

    default List<AssembleRecordDO> selectAssembleRecordByIds(Collection<String> ids,Integer type){
        MPJLambdaWrapperX<AssembleRecordDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(AssembleRecordDO::getId, ids)
                .eq(AssembleRecordDO::getType, type);
        return selectList(wrapperX);
    }

    default List<AssembleRecordDO> selectAssembleRecordByToolInfoId(String toolInfoId,Integer type){
        MPJLambdaWrapperX<AssembleRecordDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX .eq(AssembleRecordDO::getToolInfoId, toolInfoId)
                .eq(AssembleRecordDO::getType, type);
        return selectList(wrapperX);
    }

    default List<AssembleRecordDO> selectCurrentAssembleRecordByToolInfoId(String toolInfoId){
        MPJLambdaWrapperX<AssembleRecordDO> wrapperX = new MPJLambdaWrapperX<>();
            wrapperX.eq(AssembleRecordDO::getToolInfoId, toolInfoId)
                    .eq(AssembleRecordDO::getType, DictConstants.ASSEMBLE_RECORD_TYPE_CURRENT_ASSEMBLE);
        return selectList(wrapperX);
    }

    /**
     * 批量更新装配记录的状态
     */
    default int updateStatusBatch(Collection<String> ids ,Integer befortType, Integer aftertType){
        LambdaUpdateWrapper<AssembleRecordDO> wrapperX = new LambdaUpdateWrapper<>();
        wrapperX.in(AssembleRecordDO::getId, ids)
                .eq(AssembleRecordDO::getType, befortType)
                .set(AssembleRecordDO::getType, aftertType);
        return update(wrapperX);
    }
}