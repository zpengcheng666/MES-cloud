package com.miyu.module.mcc.dal.mysql.encodingattribute;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.mcc.dal.dataobject.encodingattribute.EncodingAttributeDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.mcc.controller.admin.encodingattribute.vo.*;

/**
 * 编码自定义属性 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface EncodingAttributeMapper extends BaseMapperX<EncodingAttributeDO> {

    default PageResult<EncodingAttributeDO> selectPage(EncodingAttributePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<EncodingAttributeDO>()
                .betweenIfPresent(EncodingAttributeDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(EncodingAttributeDO::getName, reqVO.getName())
                .eqIfPresent(EncodingAttributeDO::getCode, reqVO.getCode())
                .orderByDesc(EncodingAttributeDO::getId));
    }

}