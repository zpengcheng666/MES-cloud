package com.miyu.module.ppm.dal.mysql.material;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.ppm.dal.dataobject.material.MaterialDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.material.vo.*;

/**
 * 物料基本信息 Mapper
 *
 * @author zhangyunfei
 */
@Mapper
public interface MaterialMapper extends BaseMapperX<MaterialDO> {

    default PageResult<MaterialDO> selectPage(MaterialPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MaterialDO>()
                .eqIfPresent(MaterialDO::getType, reqVO.getType())
                .likeIfPresent(MaterialDO::getName, reqVO.getName())
                .betweenIfPresent(MaterialDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialDO::getCreationIp, reqVO.getCreationIp())
                .eqIfPresent(MaterialDO::getUpdatedIp, reqVO.getUpdatedIp())
                .orderByDesc(MaterialDO::getId));
    }

}