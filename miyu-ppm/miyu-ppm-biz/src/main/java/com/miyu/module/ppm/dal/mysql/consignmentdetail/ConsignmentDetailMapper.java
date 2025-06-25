package com.miyu.module.ppm.dal.mysql.consignmentdetail;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.*;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.util.CollectionUtils;



/**
 * 收货明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ConsignmentDetailMapper extends BaseMapperX<ConsignmentDetailDO> {

    default PageResult<ConsignmentDetailDO> selectPage(PurchaseConsignmentDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ConsignmentDetailDO>()
                .eqIfPresent(ConsignmentDetailDO::getConsignmentId, reqVO.getConsignmentId())
                .eqIfPresent(ConsignmentDetailDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(ConsignmentDetailDO::getConsignedAmount, reqVO.getConsignedAmount())
                .eqIfPresent(ConsignmentDetailDO::getInfoId, reqVO.getInfoId())
                .eqIfPresent(ConsignmentDetailDO::getSignedAmount, reqVO.getSignedAmount())
                .eqIfPresent(ConsignmentDetailDO::getSignedBy, reqVO.getSignedBy())
                .betweenIfPresent(ConsignmentDetailDO::getSignedTime, reqVO.getSignedTime())
                .betweenIfPresent(ConsignmentDetailDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ConsignmentDetailDO::getId));
    }

    default List<ConsignmentDetailDO> selectListByConsignmentId(String consignmentId) {
        return selectList(ConsignmentDetailDO::getConsignmentId, consignmentId);
    }


    /**
     * 删除子表数据
     * @param consignmentId
     * @return
     */
    default int deleteByConsignmentId(String consignmentId) {
        return delete(ConsignmentDetailDO::getConsignmentId, consignmentId);
    }

    /**
     * 查询合同下 有效的出库单
     * @param contractId
     * @param detailIds
     * @return
     */
    default List<ConsignmentDetailDO> getInboundOderByContractId(String contractId, List<String> detailIds, List<Integer> status) {

        MPJLambdaWrapperX<ConsignmentDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ConsignmentDO.class, ConsignmentDO::getId, ConsignmentDetailDO::getConsignmentId)
                .eq(ConsignmentDO::getContractId,contractId)
                .in(ConsignmentDO::getConsignmentStatus, status)
                .selectAs(ConsignmentDO::getNo, ConsignmentDetailDO::getNo)
                .selectAs(ConsignmentDO::getName, ConsignmentDetailDO::getName)
                .selectAll(ConsignmentDetailDO.class);
        if (!CollectionUtils.isEmpty(detailIds)){
            wrapperX.notIn(ConsignmentDetailDO::getId,detailIds);
        }
        return selectList(wrapperX);
    }


    default ConsignmentDetailDO queryConsignmentDetailIdByConfigId(String materialConfigId, String consignmentId){
        MPJLambdaWrapperX<ConsignmentDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ContractOrderDO.class,ContractOrderDO::getContractId, ConsignmentDetailDO::getContractId)
                .eq(ContractOrderDO::getMaterialId,materialConfigId)
                .eq(ConsignmentDetailDO::getConsignmentId,consignmentId)
                .selectAs(ConsignmentDetailDO::getId, ConsignmentDetailDO::getId);
        return selectOne(wrapperX);
    }

    default List<ConsignmentDetailDO> queryConsignmentDetailIdById(String id){
        MPJLambdaWrapperX<ConsignmentDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ConsignmentDO.class, ConsignmentDO::getId, ConsignmentDetailDO::getConsignmentId)
                .leftJoin(ContractOrderDO.class,ContractOrderDO::getId, ConsignmentDetailDO::getOrderId)
                .eq(ContractOrderDO ::getContractId,id)
                .selectAs(ConsignmentDO::getNo, ConsignmentDetailDO::getNo)
                .selectAs(ConsignmentDO::getName, ConsignmentDetailDO::getName)
                .selectAs(ConsignmentDO::getConsignmentStatus, ConsignmentDetailDO::getConsignmentStatus)
                .selectAs(ContractOrderDO::getMaterialId, ConsignmentDetailDO::getMaterialConfigId)
                .selectAs("0", ConsignmentDetailDO::getConsignedAmount)
                .selectAll(ConsignmentDetailDO.class)
                .distinct();
        List<ConsignmentDetailDO> no = selectList(wrapperX);
        return selectList(wrapperX);
    }

    /**
     * 通过收货单获取收货单明细
     * @param id
     * @return
     */
    default List<ConsignmentDetailDO> queryConsignmentDetailIdByConsignmentId(String id){
        MPJLambdaWrapperX<ConsignmentDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(ConsignmentDetailDO::getConsignmentId,id)
                .selectAll(ConsignmentDetailDO.class);
        return selectList(wrapperX);
    }



    //SQL文件------------------------------------------------------------------------------------------------------

    /**
     * 采购合同子表数据 物理删除
     * @param consignmentId
     * @return
     */
    void deletePurchaseConsignmentDetail(@Param("consignmentId") String consignmentId);




    default List<ConsignmentDetailDO> getDetailsForQMS(String id){
        MPJLambdaWrapperX<ConsignmentDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(ConsignmentDetailDO::getInfoId,id)
                .eq(ConsignmentDetailDO::getConsignmentStatus, ConsignmentStatusEnum.FINISH.getStatus())
                .selectAll(ConsignmentDetailDO.class);
        return selectList(wrapperX);
    }




    @Select("SELECT c.* from  ppm_consignment c  LEFT JOIN ppm_consignment_detail d on d.consignment_id = c.id where d.bar_code = #{barcode} and d.consignment_status =6  order by d.update_time desc limit 1")
    List<ConsignmentDO> getConsignmentByBarCode(@Param("barcode")String barcode);



}