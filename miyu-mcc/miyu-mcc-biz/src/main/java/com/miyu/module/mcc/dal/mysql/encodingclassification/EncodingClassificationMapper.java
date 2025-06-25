package com.miyu.module.mcc.dal.mysql.encodingclassification;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.mcc.dal.dataobject.encodingclassification.EncodingClassificationDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.mcc.controller.admin.encodingclassification.vo.*;

/**
 * 编码分类 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface EncodingClassificationMapper extends BaseMapperX<EncodingClassificationDO> {

    default PageResult<EncodingClassificationDO> selectPage(EncodingClassificationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<EncodingClassificationDO>()
                .betweenIfPresent(EncodingClassificationDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(EncodingClassificationDO::getCode, reqVO.getCode())
                .likeIfPresent(EncodingClassificationDO::getName, reqVO.getName())
                .eqIfPresent(EncodingClassificationDO::getService, reqVO.getService())
                .eqIfPresent(EncodingClassificationDO::getPath, reqVO.getPath())
                .orderByDesc(EncodingClassificationDO::getId));
    }

}