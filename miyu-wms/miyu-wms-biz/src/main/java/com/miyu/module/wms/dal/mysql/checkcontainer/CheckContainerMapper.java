package com.miyu.module.wms.dal.mysql.checkcontainer;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.module.wms.dal.dataobject.checkcontainer.CheckContainerDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.checkcontainer.vo.*;

/**
 * 库存盘点容器 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface CheckContainerMapper extends BaseMapperX<CheckContainerDO> {

    default PageResult<CheckContainerDO> selectPage(CheckContainerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CheckContainerDO>()
                .betweenIfPresent(CheckContainerDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CheckContainerDO::getCheckPlanId, reqVO.getCheckPlanId())
                .eqIfPresent(CheckContainerDO::getCheckStatus, reqVO.getCheckStatus())
                .orderByDesc(CheckContainerDO::getId));
    }

    default List<CheckContainerDO> selectCheckContainerAndLocationIdByCheckPlanId(String checkPlanId){
        MPJLambdaWrapperX<CheckContainerDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStockDO.class, MaterialStockDO::getId, CheckContainerDO::getContainerStockId)
                .eq(CheckContainerDO::getCheckPlanId, checkPlanId)
                .select(MaterialStockDO::getLocationId)
                .select(MaterialStockDO::getBarCode)
                .selectAll(CheckContainerDO.class);
        return selectList(wrapperX);

    }

    default int updateCheckContainerStatus(String checkContainerId,Integer checkStatus){
        LambdaUpdateWrapper<CheckContainerDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CheckContainerDO::getCheckPlanId, checkContainerId);
        updateWrapper.ne(CheckContainerDO::getCheckStatus, DictConstants.WMS_CHECK_DETAIL_STATUS_SUBMITTED);
        updateWrapper.set(CheckContainerDO::getCheckStatus, checkStatus);
        return update(updateWrapper);
    }
}