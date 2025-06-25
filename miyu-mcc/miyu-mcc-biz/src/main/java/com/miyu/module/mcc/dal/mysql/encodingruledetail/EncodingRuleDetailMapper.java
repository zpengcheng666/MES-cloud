package com.miyu.module.mcc.dal.mysql.encodingruledetail;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.mcc.controller.admin.encodingruledetail.vo.*;

/**
 * 编码规则配置详情 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface EncodingRuleDetailMapper extends BaseMapperX<EncodingRuleDetailDO> {

    default PageResult<EncodingRuleDetailDO> selectPage(EncodingRuleDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<EncodingRuleDetailDO>()
                .betweenIfPresent(EncodingRuleDetailDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(EncodingRuleDetailDO::getType, reqVO.getType())
                .eqIfPresent(EncodingRuleDetailDO::getEncodingRuleId, reqVO.getEncodingRuleId())
                .eqIfPresent(EncodingRuleDetailDO::getSort, reqVO.getSort())
                .eqIfPresent(EncodingRuleDetailDO::getBitNumber, reqVO.getBitNumber())
                .likeIfPresent(EncodingRuleDetailDO::getName, reqVO.getName())
                .eqIfPresent(EncodingRuleDetailDO::getDefalutValue, reqVO.getDefalutValue())
                .eqIfPresent(EncodingRuleDetailDO::getRuleType, reqVO.getRuleType())
                .eqIfPresent(EncodingRuleDetailDO::getEncodingAttribute, reqVO.getEncodingAttribute())
                .eqIfPresent(EncodingRuleDetailDO::getSourceRuleId, reqVO.getSourceRuleId())
                .orderByDesc(EncodingRuleDetailDO::getId));
    }


    default List<EncodingRuleDetailDO> selectListByEncodingRuleId(String encodingRuleId) {
        return selectList(EncodingRuleDetailDO::getEncodingRuleId, encodingRuleId);
    }

    @Delete("delete from  mcc_encoding_rule_detail  where  encoding_rule_id = #{encodingRuleId}")
     int deleteByEncodingRuleId(String encodingRuleId);


}