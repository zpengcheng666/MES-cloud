package com.miyu.module.ppm.dal.mysql.companyfinance;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.companyfinance.CompanyFinanceDO;
import com.miyu.module.ppm.dal.dataobject.companyqualitycontrol.CompanyQualityControlDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.companyfinance.vo.*;

/**
 * 企业税务信息 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface CompanyFinanceMapper extends BaseMapperX<CompanyFinanceDO> {

    default PageResult<CompanyFinanceDO> selectPage(CompanyFinancePageReqVO reqVO) {
        MPJLambdaWrapperX<CompanyFinanceDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(CompanyDO.class, CompanyDO::getId, CompanyFinanceDO::getCompanyId)
                .selectAs(CompanyDO::getName, CompanyFinanceDO::getCompanyName)
                .selectAll(CompanyFinanceDO.class);

        return selectPage(reqVO, wrapper
                .eqIfPresent(CompanyFinanceDO::getCompanyId, reqVO.getCompanyId())
                .eqIfPresent(CompanyFinanceDO::getType, reqVO.getType())
                .likeIfPresent(CompanyFinanceDO::getAccountNumber, reqVO.getAccountNumber())
                .likeIfPresent(CompanyFinanceDO::getBank, reqVO.getBank())
                .eqIfPresent(CompanyFinanceDO::getAddress, reqVO.getAddress())
                .eqIfPresent(CompanyFinanceDO::getTelephone, reqVO.getTelephone())
                .eqIfPresent(CompanyFinanceDO::getBankAddress, reqVO.getBankAddress())
                .betweenIfPresent(CompanyFinanceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CompanyFinanceDO::getId));
    }

    default List<CompanyFinanceDO> selectByCompanyId(String companyId) {
        MPJLambdaWrapperX<CompanyFinanceDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(CompanyDO.class, CompanyDO::getId, CompanyFinanceDO::getCompanyId)
                .selectAs(CompanyDO::getName, CompanyFinanceDO::getCompanyName)
                .selectAll(CompanyFinanceDO.class);
        return selectList(wrapper.eq(CompanyQualityControlDO::getCompanyId, companyId));
    }

}