package com.miyu.module.wms.dal.mysql.unit;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.wms.dal.dataobject.unit.UnitDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.unit.vo.*;

/**
 * 单位 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface UnitMapper extends BaseMapperX<UnitDO> {

    default PageResult<UnitDO> selectPage(UnitPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UnitDO>()
                .betweenIfPresent(UnitDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(UnitDO::getUnit, reqVO.getUnit())
                .orderByDesc(UnitDO::getId));
    }

}