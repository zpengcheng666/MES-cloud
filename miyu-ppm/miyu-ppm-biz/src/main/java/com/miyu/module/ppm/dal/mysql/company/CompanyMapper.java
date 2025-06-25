package com.miyu.module.ppm.dal.mysql.company;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.enums.common.CompanyAuditStatusEnum;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.company.vo.*;

/**
 * 企业基本信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CompanyMapper extends BaseMapperX<CompanyDO> {

    default PageResult<CompanyDO> selectPage(CompanyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CompanyDO>()
                .likeIfPresent(CompanyDO::getName, reqVO.getName())
                .eqIfPresent(CompanyDO::getCompanyStatus, reqVO.getCompanyStatus())
                .eqIfPresent(CompanyDO::getType, reqVO.getType())
                .eqIfPresent(CompanyDO::getIndustryClassification, reqVO.getIndustryClassification())
                .inIfPresent(CompanyDO::getSupplyType, reqVO.getSupplyType())
                .orderByDesc(CompanyDO::getId));
    }

    default List<CompanyDO> selectListByType(Collection<String> types){
        return selectList(new LambdaQueryWrapperX<CompanyDO>()
                .inIfPresent(CompanyDO::getSupplyType, types)
                .eqIfPresent(CompanyDO::getStatus, CompanyAuditStatusEnum.APPROVE.getStatus()));
    }

    default List<CompanyDO> selectListByIds(Collection<String> ids) {
        return selectList(CompanyDO::getId, ids);
    }
}