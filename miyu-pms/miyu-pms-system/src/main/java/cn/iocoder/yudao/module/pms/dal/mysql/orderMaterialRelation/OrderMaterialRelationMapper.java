package cn.iocoder.yudao.module.pms.dal.mysql.orderMaterialRelation;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo.OrderMaterialRelationPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo.OrderMaterialRelationSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.orderMaterialRelation.OrderMaterialRelationDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单物料关系表 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface OrderMaterialRelationMapper extends BaseMapperX<OrderMaterialRelationDO> {

    default PageResult<OrderMaterialRelationDO> selectPage(OrderMaterialRelationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrderMaterialRelationDO>()
                .eqIfPresent(OrderMaterialRelationDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(OrderMaterialRelationDO::getMaterialCode, reqVO.getMaterialCode())
                .eqIfPresent(OrderMaterialRelationDO::getMaterialStatus, reqVO.getMaterialStatus())
                .eqIfPresent(OrderMaterialRelationDO::getProductCode, reqVO.getProductCode())
                .eqIfPresent(OrderMaterialRelationDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(OrderMaterialRelationDO::getPlanId, reqVO.getPlanId())
                .eqIfPresent(OrderMaterialRelationDO::getPlanItemId, reqVO.getPlanItemId())
                .eqIfPresent(OrderMaterialRelationDO::getStep, reqVO.getStep())
                .betweenIfPresent(OrderMaterialRelationDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrderMaterialRelationDO::getId));
    }

    /**
     * 条件查询
     * @param reqVO
     * @return
     */
    default List<OrderMaterialRelationDO> getOrderMaterialRelationWith(OrderMaterialRelationSaveReqVO reqVO) {
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eq(OrderMaterialRelationDO::getPlanItemId, reqVO.getPlanItemId())
                .eqIfPresent(OrderMaterialRelationDO::getStep, reqVO.getStep())
                .eqIfPresent(OrderMaterialRelationDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(OrderMaterialRelationDO::getMaterialCode, reqVO.getMaterialCode())
                .eqIfPresent(OrderMaterialRelationDO::getVariableCode, reqVO.getVariableCode())
                .eqIfPresent(OrderMaterialRelationDO::getProductCode, reqVO.getProductCode())
                .eqIfPresent(OrderMaterialRelationDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(OrderMaterialRelationDO::getPlanId, reqVO.getPlanId())
                .orderByDesc(OrderMaterialRelationDO::getId);
        return selectList(wrapperX);
    }

    /**
     * 查询带有materialCode(barCode)的关系集合
     * @param list
     * @return
     */
    default List<OrderMaterialRelationDO> selectListByBarCode(List<String> list) {
        if(CollectionUtils.isAnyEmpty(list)){
            return Collections.emptyList();
        }
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(OrderMaterialRelationDO::getMaterialCode,list);
        return selectList(wrapperX);
    }

    /**
     * 释放关系码
     * @param id
     */
    default void releaseCode(String id){
        LambdaUpdateWrapper<OrderMaterialRelationDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(OrderMaterialRelationDO::getMaterialCode,null);
        updateWrapper.set(OrderMaterialRelationDO::getOrderNumber,null);
        updateWrapper.set(OrderMaterialRelationDO::getVariableCode,null);
        updateWrapper.set(OrderMaterialRelationDO::getPlanType,null);
        updateWrapper.set(OrderMaterialRelationDO::getProductCode,null);
        updateWrapper.set(OrderMaterialRelationDO::getPlanId,null);
        updateWrapper.set(OrderMaterialRelationDO::getPlanItemId,null);
        updateWrapper.set(OrderMaterialRelationDO::getStep,null);
        updateWrapper.set(OrderMaterialRelationDO::getContractId,null);
        updateWrapper.set(OrderMaterialRelationDO::getAidMill,null);
        updateWrapper.set(OrderMaterialRelationDO::getMaterialTypeId,null);
        updateWrapper.set(OrderMaterialRelationDO::getMaterialStatus,3);
        updateWrapper.eq(OrderMaterialRelationDO::getId,id);
        update(updateWrapper);
    }

}
