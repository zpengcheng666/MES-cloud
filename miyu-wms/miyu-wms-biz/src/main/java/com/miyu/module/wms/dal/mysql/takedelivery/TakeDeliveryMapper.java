package com.miyu.module.wms.dal.mysql.takedelivery;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.wms.dal.dataobject.takedelivery.TakeDeliveryDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.takedelivery.vo.*;

/**
 * 物料收货 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface TakeDeliveryMapper extends BaseMapperX<TakeDeliveryDO> {

    default PageResult<TakeDeliveryDO> selectPage(TakeDeliveryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TakeDeliveryDO>()
                .betweenIfPresent(TakeDeliveryDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(TakeDeliveryDO::getOrderNumber, reqVO.getOrderNumber())
                .eqIfPresent(TakeDeliveryDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(TakeDeliveryDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(TakeDeliveryDO::getStorageId, reqVO.getStorageId())
                .eqIfPresent(TakeDeliveryDO::getMaterialId, reqVO.getMaterialId())
                .orderByDesc(TakeDeliveryDO::getId));
    }

}