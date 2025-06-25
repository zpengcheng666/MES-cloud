package com.miyu.cloud.dms.dal.mysql.sparepart;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.dms.dal.dataobject.sparepart.SparePartDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.dms.controller.admin.sparepart.vo.*;

/**
 * 备品/备件 Mapper
 *
 * @author 王正浩
 */
@Mapper
public interface SparePartMapper extends BaseMapperX<SparePartDO> {

    default PageResult<SparePartDO> selectPage(SparePartPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SparePartDO>()
                .likeIfPresent(SparePartDO::getCode, reqVO.getCode())
                .likeIfPresent(SparePartDO::getName, reqVO.getName())
                .eqIfPresent(SparePartDO::getNumber, reqVO.getNumber())
                .eqIfPresent(SparePartDO::getType, reqVO.getType())
                .likeIfPresent(SparePartDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(SparePartDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SparePartDO::getId));
    }

}