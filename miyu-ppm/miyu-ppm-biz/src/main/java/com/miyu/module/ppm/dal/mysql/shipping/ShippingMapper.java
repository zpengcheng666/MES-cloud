package com.miyu.module.ppm.dal.mysql.shipping;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.controller.admin.home.vo.ContractAnalysisResp;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.shipping.vo.*;
import org.apache.ibatis.annotations.Select;

/**
 * 销售发货 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ShippingMapper extends BaseMapperX<ShippingDO> {

    default PageResult<ShippingDO> selectPage(ShippingPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShippingDO>()
                .eqIfPresent(ShippingDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ShippingDO::getCompanyId, reqVO.getCompanyId())
                .likeIfPresent(ShippingDO::getNo, reqVO.getNo())
                .likeIfPresent(ShippingDO::getName, reqVO.getName())
                .eqIfPresent(ShippingDO::getConsigner, reqVO.getConsigner())
                .betweenIfPresent(ShippingDO::getConsignerDate, reqVO.getConsignerDate())
                .eqIfPresent(ShippingDO::getDeliveryMethod, reqVO.getDeliveryMethod())
                .eqIfPresent(ShippingDO::getDeliveryBy, reqVO.getDeliveryBy())
                .eqIfPresent(ShippingDO::getDeliveryNumber, reqVO.getDeliveryNumber())
                .eqIfPresent(ShippingDO::getDeliveryContact, reqVO.getDeliveryContact())
                .eqIfPresent(ShippingDO::getConsignedBy, reqVO.getConsignedBy())
                .eqIfPresent(ShippingDO::getShippingStatus, reqVO.getShippingStatus())
                .eqIfPresent(ShippingDO::getShippingType, reqVO.getShippingType())
                .betweenIfPresent(ShippingDO::getConsignedDate, reqVO.getConsignedDate())
                .eqIfPresent(ShippingDO::getConsignedContact, reqVO.getConsignedContact())
                .inIfPresent(ShippingDO::getShippingStatus, reqVO.getShippingStatuss())
                .orderByDesc(ShippingDO::getId));
    }



    default List<ShippingDO> getShippingByContract(String contractId,List<Integer> statusList,Integer shippingType) {
        MPJLambdaWrapperX<ShippingDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(ShippingDO ::getShippingStatus, statusList)
                .eq(ShippingDO::getShippingType,shippingType)
                .eq(ShippingDO::getContractId, contractId);
        return selectList(wrapperX);
    }


    default List<ShippingDO> getShippingByShippingInfoId(String infoId) {
        MPJLambdaWrapperX<ShippingDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ShippingInfoDO.class, ShippingInfoDO::getShippingId,ShippingDO::getId)
                .eq(ShippingInfoDO::getId,infoId)
                .selectAll(ShippingDO.class);
        return selectList(wrapperX);
    }

    @Select("SELECT \n" +
            "    YEAR(co.create_time) AS year, \n" +
            "    MONTH(co.create_time) AS month, \n" +
            "    SUM(co.quantity* co.tax_price) AS price\n" +
            "FROM \n" +
            "    pd_contract_order co LEFT JOIN pd_contract c on c.id = co.contract_id\n" +
            "WHERE \n" +
            "c.contract_status in(1,4) and c.type=#{type} and  \n" +
            "    co.create_time >= CURRENT_DATE - INTERVAL 12 MONTH\n" +
            "GROUP BY \n" +
            "    YEAR(co.create_time), \n" +
            "    MONTH(co.create_time)\n" +
            "ORDER BY \n" +
            "    year ASC, \n" +
            "    month ASC;")
    List<ContractAnalysisResp> getContractAnalysis(Integer type);
}