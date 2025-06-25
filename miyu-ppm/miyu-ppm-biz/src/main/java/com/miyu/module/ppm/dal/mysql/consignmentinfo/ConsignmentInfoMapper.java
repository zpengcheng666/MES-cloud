package com.miyu.module.ppm.dal.mysql.consignmentinfo;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.controller.admin.home.vo.ConsignmentCompanyNumberRespVO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.consignmentinfo.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 收货产品 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface ConsignmentInfoMapper extends BaseMapperX<ConsignmentInfoDO> {

    default PageResult<ConsignmentInfoDO> selectPage(ConsignmentInfoPageReqVO reqVO) {
//        MPJLambdaWrapperX<ConsignmentInfoDO> wrapperX = new MPJLambdaWrapperX<>();
//        wrapperX.leftJoin(ConsignmentDO.class,ConsignmentDO::getId,ConsignmentInfoDO::getConsignmentId)
//                .leftJoin(ContractDO.class,ContractDO::getId,ConsignmentInfoDO::getContractId)


        return selectPage(reqVO, new LambdaQueryWrapperX<ConsignmentInfoDO>()
                .eqIfPresent(ConsignmentInfoDO::getConsignmentId, reqVO.getConsignmentId())
                .eqIfPresent(ConsignmentInfoDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ConsignmentInfoDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(ConsignmentInfoDO::getMaterialConfigId, reqVO.getMaterialId())
                .eqIfPresent(ConsignmentInfoDO::getConsignedAmount, reqVO.getConsignedAmount())
                .eqIfPresent(ConsignmentInfoDO::getSignedAmount, reqVO.getSignedAmount())
                .eqIfPresent(ConsignmentInfoDO::getSignedBy, reqVO.getSignedBy())
                .betweenIfPresent(ConsignmentInfoDO::getSignedTime, reqVO.getSignedTime())
                .betweenIfPresent(ConsignmentInfoDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ConsignmentInfoDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(ConsignmentInfoDO::getProjectOrderId, reqVO.getProjectOrderId())
                .eqIfPresent(ConsignmentInfoDO::getProjectPlanId, reqVO.getProjectPlanId())
                .eqIfPresent(ConsignmentInfoDO::getProjectPlanItemId, reqVO.getProjectPlanItemId())
                .eqIfPresent(ConsignmentInfoDO::getConsignmentStatus, ConsignmentStatusEnum.SINGING.getStatus())
                .eqIfPresent(ConsignmentInfoDO::getConsignmentType, reqVO.getConsignmentType())
                .orderByDesc(ConsignmentInfoDO::getId));
    }


    @Delete("delete from  ppm_consignment_info  where  consignment_id = #{consignmentId}")
    void deletePurchaseConsignmentInfo(@Param("consignmentId") String consignmentId);


    default List<ConsignmentInfoDO> selectListByConsignmentId(String consignmentId) {
        return selectList(ConsignmentInfoDO::getConsignmentId, consignmentId);
    }


    default List<ConsignmentInfoDO> selectListBySheet() {
        MPJLambdaWrapperX<ConsignmentInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ConsignmentDO.class, ConsignmentDO::getId, ConsignmentInfoDO::getConsignmentId)
                .selectAs(ConsignmentDO::getNo, ConsignmentInfoDO::getNo)
                .selectAs(ConsignmentDO::getName, ConsignmentInfoDO::getName)
                .selectAll(ConsignmentInfoDO.class);
        wrapperX.eq(ConsignmentInfoDO::getConsignmentStatus, ConsignmentStatusEnum.FINISH.getStatus())
                .eq(ConsignmentInfoDO::getSchemeResult, 0);
        return selectList(wrapperX);
    }


    default List<ConsignmentInfoDO> getConsignmentInfos(ConsignmentInfoQueryReqVO reqVO){
        MPJLambdaWrapperX<ConsignmentInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ConsignmentDO.class, ConsignmentDO::getId, ConsignmentInfoDO::getConsignmentId)
                .leftJoin(ContractDO.class, ContractDO::getId, ConsignmentDO::getContractId)
                .selectAs(ConsignmentDO::getNo, ConsignmentInfoDO::getNo)
                .selectAs(ConsignmentDO::getName, ConsignmentInfoDO::getName)
                .selectAs(ContractDO::getParty, ConsignmentInfoDO::getCompanyId)
                .selectAll(ConsignmentInfoDO.class);
        wrapperX.eqIfPresent(ConsignmentInfoDO::getConsignmentStatus, reqVO.getConsignmentStatus())
                .inIfPresent(ConsignmentInfoDO::getConsignmentType, reqVO.getConsignmentType())
                .eqIfPresent(ConsignmentInfoDO::getCreator,reqVO.getCreator())
        .betweenIfPresent(ConsignmentInfoDO::getCreateTime, reqVO.getBeginTime(),reqVO.getEndTime());
        return selectList(wrapperX);
    }
    @Select("SELECT sum(pi.signed_amount) as signedAmount,cy.id as companyId,cy.`name` as companyName from ppm_consignment_info pi left JOIN ppm_consignment pc on pc.id = pi.consignment_id\n" +
            "LEFT JOIN pd_contract c on c.id = pc.contract_id\n" +
            "LEFT JOIN pd_company cy on cy.id = c.party\n" +
            "where pi.consignment_status  = 6 and pi.consignment_type in (1,2) and c.create_time BETWEEN #{beginTimeRange}  and #{endTimeRange} GROUP BY c.party")
     List<ConsignmentCompanyNumberRespVO> getCompanyConsignmentAmount(@Param("beginTimeRange") LocalDateTime beginTimeRange,@Param("endTimeRange")LocalDateTime endTimeRange);
}