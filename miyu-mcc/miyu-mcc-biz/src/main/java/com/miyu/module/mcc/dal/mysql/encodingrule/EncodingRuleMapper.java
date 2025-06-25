package com.miyu.module.mcc.dal.mysql.encodingrule;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.mcc.dal.dataobject.encodingclassification.EncodingClassificationDO;
import com.miyu.module.mcc.dal.dataobject.encodingrule.EncodingRuleDO;
import com.miyu.module.mcc.dal.dataobject.materialtype.MaterialTypeDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.mcc.controller.admin.encodingrule.vo.*;

/**
 * 编码规则配置 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface EncodingRuleMapper extends BaseMapperX<EncodingRuleDO> {

    default PageResult<EncodingRuleDO> selectPage(EncodingRulePageReqVO reqVO) {

        MPJLambdaWrapperX<EncodingRuleDO> wrapper = new MPJLambdaWrapperX<EncodingRuleDO>();
        wrapper.leftJoin(EncodingClassificationDO.class,EncodingClassificationDO::getId,EncodingRuleDO::getClassificationId)
                .leftJoin(MaterialTypeDO.class,MaterialTypeDO::getId,EncodingRuleDO::getMaterialTypeId)
                .selectAs(EncodingClassificationDO::getName,EncodingRuleDO::getClassificationName)
                .selectAs(EncodingClassificationDO::getCode,EncodingRuleDO::getClassificationCode)
                .selectAs(MaterialTypeDO::getCode,EncodingRuleDO::getMaterialTypeCode)
                .selectAs(MaterialTypeDO::getName,EncodingRuleDO::getMaterialTypeName)
                .selectAll(EncodingRuleDO.class);
        return selectPage(reqVO, wrapper
                .betweenIfPresent(EncodingRuleDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(EncodingRuleDO::getName, reqVO.getName())
                .eqIfPresent(EncodingRuleDO::getClassificationId, reqVO.getClassificationId())
                .eqIfPresent(EncodingRuleDO::getStatus, reqVO.getStatus())
                .eqIfPresent(EncodingRuleDO::getTotalBitNumber, reqVO.getTotalBitNumber())
                .orderByDesc(EncodingRuleDO::getId));
    }


    default List<EncodingRuleDO> selectListByType(String classificationCode,Integer encodingRuleType,String materialTypeCode){
        MPJLambdaWrapperX<EncodingRuleDO> wrapper = new MPJLambdaWrapperX<EncodingRuleDO>();
        wrapper.leftJoin(EncodingClassificationDO.class,EncodingClassificationDO::getId,EncodingRuleDO::getClassificationId);
        if (!StringUtils.isEmpty(materialTypeCode)) {
            wrapper.leftJoin(MaterialTypeDO.class,MaterialTypeDO::getId,EncodingRuleDO::getMaterialTypeId)
                    .eq(MaterialTypeDO::getParentId,"0");

        }

        if (!StringUtils.isEmpty(materialTypeCode)) {
            wrapper.eq(MaterialTypeDO::getCode,materialTypeCode);
        }
        wrapper.eq(EncodingClassificationDO::getCode,classificationCode)
                .selectAll(EncodingRuleDO.class);
        return selectList(wrapper.eqIfPresent(EncodingRuleDO::getEncodingRuleType,encodingRuleType));
    }

}