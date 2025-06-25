package com.miyu.module.wms.dal.mysql.checkdetail;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.wms.dal.dataobject.checkdetail.CheckDetailDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.checkdetail.vo.*;

/**
 * 库存盘点详情 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface CheckDetailMapper extends BaseMapperX<CheckDetailDO> {

    default PageResult<CheckDetailDO> selectPage(CheckDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CheckDetailDO>()
                .betweenIfPresent(CheckDetailDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CheckDetailDO::getId));
    }

    default List<CheckDetailDO> selectByCheckContainerIds(Collection<String> checkContainerIds){
        MPJLambdaWrapperX<CheckDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStockDO.class, MaterialStockDO::getId, CheckDetailDO::getMaterialStockId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
//                .eq(MaterialStockDO::getIsExists, true).or().isNull(CheckDetailDO::getMaterialStockId) // 仅查询存在的库存
                .in(CheckDetailDO::getCheckContainerId, checkContainerIds)
                .select(MaterialConfigDO::getMaterialManage)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialStockDO::getBarCode)
                .select(MaterialStockDO::getBatchNumber)
                .selectAll(CheckDetailDO.class);
        return selectList(wrapperX);
    }
}