package com.miyu.module.ppm.dal.mysql.companycontact;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.companycontact.CompanyContactDO;
import com.miyu.module.ppm.dal.dataobject.companyqualitycontrol.CompanyQualityControlDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.companycontact.vo.*;

/**
 * 企业联系人 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface CompanyContactMapper extends BaseMapperX<CompanyContactDO> {

    default PageResult<CompanyContactDO> selectPage(CompanyContactPageReqVO reqVO) {
        MPJLambdaWrapperX<CompanyContactDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(CompanyDO.class, CompanyDO::getId, CompanyQualityControlDO::getCompanyId)
                .leftJoin(CompanyContactDO.class, "t4", CompanyContactDO::getId, CompanyContactDO::getSuperior)
                .selectAs(CompanyDO::getName, CompanyQualityControlDO::getCompanyName)
                .selectAs("IFNULL(t4.name, t.superior)", CompanyContactDO::getSuperior)
                .selectAll(CompanyContactDO.class);
        return selectPage(reqVO, wrapper
                .eqIfPresent(CompanyContactDO::getCompanyId, reqVO.getCompanyId())
                .likeIfPresent(CompanyContactDO::getName, reqVO.getName())
                .eqIfPresent(CompanyContactDO::getDepart, reqVO.getDepart())
                .eqIfPresent(CompanyContactDO::getPosition, reqVO.getPosition())
                .eqIfPresent(CompanyContactDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CompanyContactDO::getSuperior, reqVO.getSuperior())
                .eqIfPresent(CompanyContactDO::getHeader, reqVO.getHeader())
                .eqIfPresent(CompanyContactDO::getSex, reqVO.getSex())
                .eqIfPresent(CompanyContactDO::getPhone, reqVO.getPhone())
                .eqIfPresent(CompanyContactDO::getEmail, reqVO.getEmail())
                .eqIfPresent(CompanyContactDO::getAge, reqVO.getAge())
                .eqIfPresent(CompanyContactDO::getAddress, reqVO.getAddress())
                .eqIfPresent(CompanyContactDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(CompanyContactDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CompanyContactDO::getCreationIp, reqVO.getCreationIp())
                .eqIfPresent(CompanyContactDO::getUpdatedIp, reqVO.getUpdatedIp())
                .orderByDesc(CompanyContactDO::getId));
    }

}