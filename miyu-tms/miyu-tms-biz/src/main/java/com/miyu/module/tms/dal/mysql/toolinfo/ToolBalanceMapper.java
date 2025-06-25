package com.miyu.module.tms.dal.mysql.toolinfo;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolBalanceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 刀具动平衡 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface ToolBalanceMapper extends BaseMapperX<ToolBalanceDO> {

    default PageResult<ToolBalanceDO> selectPage(PageParam reqVO, String toolInfoId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ToolBalanceDO>()
            .eq(ToolBalanceDO::getToolInfoId, toolInfoId)
            .orderByDesc(ToolBalanceDO::getId));
    }

    default int deleteByToolInfoId(String toolInfoId) {
        return delete(ToolBalanceDO::getToolInfoId, toolInfoId);
    }


    default ToolBalanceDO selectByToolInfoId(String toolInfoId){
        return selectOne(ToolBalanceDO::getToolInfoId, toolInfoId);
    }
}