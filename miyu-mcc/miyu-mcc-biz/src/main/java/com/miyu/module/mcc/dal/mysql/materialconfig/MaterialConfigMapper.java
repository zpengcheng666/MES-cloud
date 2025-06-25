package com.miyu.module.mcc.dal.mysql.materialconfig;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.mcc.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.mcc.dal.dataobject.materialtype.MaterialTypeDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.mcc.controller.admin.materialconfig.vo.*;

/**
 * 物料类型 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface MaterialConfigMapper extends BaseMapperX<MaterialConfigDO> {

    default PageResult<MaterialConfigDO> selectPage(MaterialConfigPageReqVO reqVO) {
        MPJLambdaWrapperX<MaterialConfigDO> materialConfigWrapperX = new MPJLambdaWrapperX<MaterialConfigDO>();
            materialConfigWrapperX
                    .leftJoin(MaterialConfigDO.class, "mc1",MaterialConfigDO:: getId,MaterialConfigDO :: getMaterialSourceId)
                    .leftJoin(MaterialTypeDO.class,"mt1",MaterialTypeDO:: getId,MaterialConfigDO :: getMaterialTypeId)
                    .leftJoin(MaterialTypeDO.class,"mt2",MaterialTypeDO:: getId,MaterialConfigDO :: getMaterialParentTypeId)
                    .selectAs("mc1",MaterialConfigDO::getMaterialNumber,MaterialConfigDO::getMaterialNumberSource)
                    .selectAs("mt1",MaterialTypeDO::getName,MaterialConfigDO::getMaterialTypeName)
                    .selectAs("mt2",MaterialTypeDO::getName,MaterialConfigDO::getMaterialParentTypeName)
                    .selectAs("mt2",MaterialTypeDO::getCode,MaterialConfigDO::getMaterialParentTypeCode)
                    .selectAll(MaterialConfigDO.class);

            if(StringUtils.isNotBlank(reqVO.getMaterialTypeId())){
                materialConfigWrapperX.eq(MaterialConfigDO::getMaterialTypeId, reqVO.getMaterialTypeId()).or().
                        eq(MaterialConfigDO::getMaterialParentTypeId, reqVO.getMaterialTypeId());
            }


        return selectPage(reqVO,materialConfigWrapperX
                .betweenIfPresent(MaterialConfigDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(MaterialConfigDO::getMaterialNumber, reqVO.getMaterialNumber())
                .likeIfPresent(MaterialConfigDO::getMaterialName, reqVO.getMaterialName())
                .eqIfPresent(MaterialConfigDO::getMaterialParentTypeId, reqVO.getMaterialParentTypeId())
                .eqIfPresent(MaterialConfigDO::getMaterialSpecification, reqVO.getMaterialSpecification())
                .eqIfPresent(MaterialConfigDO::getMaterialBrand, reqVO.getMaterialBrand())
                .eqIfPresent(MaterialConfigDO::getMaterialUnit, reqVO.getMaterialUnit())
                .eqIfPresent(MaterialConfigDO::getMaterialSourceId, reqVO.getMaterialSourceId())
                .eqIfPresent(MaterialConfigDO::getMaterialTypeCode, reqVO.getMaterialTypeCode())
                .orderByDesc(MaterialConfigDO::getId));
    }

    default List<MaterialConfigDO> getMaterialConfigListByTypeId(String materialTypeId) {
        MPJLambdaWrapperX<MaterialConfigDO> materialConfigWrapperX = new MPJLambdaWrapperX<MaterialConfigDO>();
        if(StringUtils.isNotBlank(materialTypeId)){
            materialConfigWrapperX.eq(MaterialConfigDO::getMaterialTypeId, materialTypeId).or().
                    eq(MaterialConfigDO::getMaterialParentTypeId, materialTypeId);
        }
        return selectList(materialConfigWrapperX
                .orderByDesc(MaterialConfigDO::getId));
    }
}
