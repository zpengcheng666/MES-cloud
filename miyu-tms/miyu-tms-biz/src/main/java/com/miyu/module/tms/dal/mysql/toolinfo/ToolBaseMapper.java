package com.miyu.module.tms.dal.mysql.toolinfo;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolBaseDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 对刀数据 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface ToolBaseMapper extends BaseMapperX<ToolBaseDO> {

    default PageResult<ToolBaseDO> selectPage(PageParam reqVO, String toolInfoId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ToolBaseDO>()
            .eq(ToolBaseDO::getToolInfoId, toolInfoId)
            .orderByDesc(ToolBaseDO::getId));
    }

    default int deleteByToolInfoId(String toolInfoId) {
        return delete(ToolBaseDO::getToolInfoId, toolInfoId);
    }

    default ToolBaseDO selectByToolInfoId(String toolInfoId){
        return selectOne(ToolBaseDO::getToolInfoId, toolInfoId);
    }
}