package com.miyu.module.ppm.dal.mysql.companyqualitycontrol;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.companyqualitycontrol.CompanyQualityControlDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.companyqualitycontrol.vo.*;

import java.util.List;

/**
 * 企业质量控制信息 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface CompanyQualityControlMapper extends BaseMapperX<CompanyQualityControlDO> {

    default PageResult<CompanyQualityControlDO> selectPage(CompanyQualityControlPageReqVO reqVO) {
        MPJLambdaWrapperX<CompanyQualityControlDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(CompanyDO.class, CompanyDO::getId, CompanyQualityControlDO::getCompanyId)
                .selectAs(CompanyDO::getName, CompanyQualityControlDO::getCompanyName)
                .selectAll(CompanyQualityControlDO.class);

        return selectPage(reqVO, wrapper
                .eqIfPresent(CompanyQualityControlDO::getCompanyId, reqVO.getCompanyId())
                .eqIfPresent(CompanyQualityControlDO::getQmsc, reqVO.getQmsc())
                .eqIfPresent(CompanyQualityControlDO::getInspection, reqVO.getInspection())
                .eqIfPresent(CompanyQualityControlDO::getNonconformingControl, reqVO.getNonconformingControl())
                .eqIfPresent(CompanyQualityControlDO::getProductionTraceability, reqVO.getProductionTraceability())
                .eqIfPresent(CompanyQualityControlDO::getPurchasingControl, reqVO.getPurchasingControl())
                .eqIfPresent(CompanyQualityControlDO::getOqc, reqVO.getOqc())
                .betweenIfPresent(CompanyQualityControlDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CompanyQualityControlDO::getId));
    }

    default CompanyQualityControlDO selectByCompanyId(String companyId) {
        MPJLambdaWrapperX<CompanyQualityControlDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(CompanyDO.class, CompanyDO::getId, CompanyQualityControlDO::getCompanyId)
                .selectAs(CompanyDO::getName, CompanyQualityControlDO::getCompanyName)
                .selectAll(CompanyQualityControlDO.class);
        return selectOne(wrapper.eq(CompanyQualityControlDO::getCompanyId, companyId));
    }
}