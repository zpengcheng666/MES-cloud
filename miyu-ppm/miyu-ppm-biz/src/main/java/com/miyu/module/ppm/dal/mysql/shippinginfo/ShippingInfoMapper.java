package com.miyu.module.ppm.dal.mysql.shippinginfo;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.ppm.controller.admin.home.vo.ConsignmentCompanyNumberRespVO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.shippinginfo.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 销售发货产品 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface ShippingInfoMapper extends BaseMapperX<ShippingInfoDO> {

    default PageResult<ShippingInfoDO> selectPage(ShippingInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShippingInfoDO>()
                .eqIfPresent(ShippingInfoDO::getShippingId, reqVO.getShippingId())
                .eqIfPresent(ShippingInfoDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(ShippingInfoDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(ShippingInfoDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ShippingInfoDO::getConsignedAmount, reqVO.getConsignedAmount())
                .eqIfPresent(ShippingInfoDO::getOutboundAmount, reqVO.getOutboundAmount())
                .eqIfPresent(ShippingInfoDO::getOutboundBy, reqVO.getOutboundBy())
                .betweenIfPresent(ShippingInfoDO::getOutboundTime, reqVO.getOutboundTime())
                .eqIfPresent(ShippingInfoDO::getSignedAmount, reqVO.getSignedAmount())
                .eqIfPresent(ShippingInfoDO::getSignedBy, reqVO.getSignedBy())
                .betweenIfPresent(ShippingInfoDO::getSignedTime, reqVO.getSignedTime())
                .betweenIfPresent(ShippingInfoDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ShippingInfoDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(ShippingInfoDO::getShippingStatus, reqVO.getStatus())
                .orderByDesc(ShippingInfoDO::getId));
    }



    @Delete("delete from  dm_shipping_info  where  shipping_id = #{shippingId}")
    void deleteAllByShippingId(String shippingId);


    @Select("SELECT sum(di.consigned_amount) as signedAmount ,cy.id as companyId,cy.`name`  as companyName  from  dm_shipping_info di\n" +
            "LEFT JOIN  dm_shipping ds  on ds.id = di.shipping_id\n" +
            " left JOIN ppm_consignment pc on pc.id = di.consignment_id\n" +
            "LEFT JOIN pd_contract c on c.id = pc.contract_id\n" +
            "LEFT JOIN pd_company cy on cy.id = c.party\n" +
            "where ds.`status`  = 2 and ds.shipping_type =3  and di.consignment_id is not null and (ds.create_time BETWEEN #{beginTimeRange}  and #{endTimeRange})  GROUP BY c.party")
    List<ConsignmentCompanyNumberRespVO> getCompanyConsignmentReturnNumber(@Param("beginTimeRange")LocalDateTime beginTimeRange,@Param("endTimeRange") LocalDateTime endTimeRange);
}