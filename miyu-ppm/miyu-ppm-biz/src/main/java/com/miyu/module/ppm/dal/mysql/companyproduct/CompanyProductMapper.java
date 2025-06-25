package com.miyu.module.ppm.dal.mysql.companyproduct;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.companyproduct.CompanyProductDO;
import com.miyu.module.ppm.dal.dataobject.material.MaterialDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.companyproduct.vo.*;

import java.util.Collection;
import java.util.List;

/**
 * 企业产品表，用于销售和采购 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface CompanyProductMapper extends BaseMapperX<CompanyProductDO> {

    default PageResult<CompanyProductDO> selectPage(CompanyProductPageReqVO reqVO) {

        MPJLambdaWrapperX<CompanyProductDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(CompanyDO.class, CompanyDO::getId, CompanyProductDO::getCompanyId)
                .leftJoin(MaterialDO.class, MaterialDO::getId, CompanyProductDO::getMaterialId)
                .like(reqVO.getProductName() != null, MaterialDO::getName, reqVO.getProductName())
                .selectAs(CompanyDO::getName, CompanyProductDO::getCompanyName)
                .selectAs(MaterialDO::getName, CompanyProductDO::getProductName)
                .selectAll(CompanyProductDO.class);

        return selectPage(reqVO, wrapper
                .eqIfPresent(CompanyProductDO::getCompanyId, reqVO.getCompanyId())
                .eqIfPresent(CompanyProductDO::getMaterialId, reqVO.getMaterialId())
                .eqIfPresent(CompanyProductDO::getInitPrice, reqVO.getInitPrice())
                .eqIfPresent(CompanyProductDO::getLeadTime, reqVO.getLeadTime())
                .betweenIfPresent(CompanyProductDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CompanyProductDO::getId));

    }


    default List<CompanyProductDO> getProductListByCompanyId(String companyId) {

        MPJLambdaWrapperX<CompanyProductDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(CompanyDO.class, CompanyDO::getId, CompanyProductDO::getCompanyId)
                .selectAll(CompanyProductDO.class)
                .eq(CompanyProductDO::getCompanyId, companyId);

        return selectList(wrapper);
    }

    default List<CompanyProductDO> selectBatchByMaterialIds(Collection<String> ids){
        return selectList(CompanyProductDO::getMaterialId, ids);
    }

    default List<CompanyProductDO> queryCompanyProductByParty(String partyId, List<String> materialIds){
        MPJLambdaWrapperX<CompanyProductDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(CompanyProductDO::getCompanyId,partyId)
                .in(CompanyProductDO::getMaterialId, materialIds)
                .selectAll(CompanyProductDO.class);
        return selectList(wrapperX);
    }

}
