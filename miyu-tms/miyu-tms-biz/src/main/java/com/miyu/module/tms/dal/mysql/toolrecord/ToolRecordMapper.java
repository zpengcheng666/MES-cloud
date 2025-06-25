package com.miyu.module.tms.dal.mysql.toolrecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.miyu.module.tms.controller.admin.assembletask.vo.AssembleTaskPageReqVO;
import com.miyu.module.tms.dal.dataobject.assembletask.AssembleTaskDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolBaseDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
import com.miyu.module.tms.dal.dataobject.toolrecord.ToolRecordDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.tms.controller.admin.toolrecord.vo.*;

/**
 * 刀具使用记录 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface ToolRecordMapper extends BaseMapperX<ToolRecordDO> {

    default PageResult<ToolRecordDO> selectPage(ToolRecordPageReqVO reqVO) {
        MPJLambdaWrapperX<ToolRecordDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ToolInfoDO.class, ToolInfoDO::getId, ToolRecordDO::getToolInfoId)
                .selectAs(ToolInfoDO::getMaterialStockId,ToolRecordDO::getMainStockId)
                .selectAll(ToolRecordDO.class);
        return selectPage(reqVO, wrapperX
                .betweenIfPresent(ToolRecordDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ToolRecordDO::getToolInfoId, reqVO.getToolInfoId())
                .betweenIfPresent(ToolRecordDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(ToolRecordDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(ToolRecordDO::getType, reqVO.getType())
                .orderByDesc(ToolRecordDO::getId));
    }

}
