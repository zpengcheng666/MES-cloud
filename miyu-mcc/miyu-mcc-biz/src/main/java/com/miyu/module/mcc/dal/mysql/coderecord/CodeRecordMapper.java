package com.miyu.module.mcc.dal.mysql.coderecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.mcc.dal.dataobject.coderecord.CodeRecordDO;
import com.miyu.module.mcc.dal.dataobject.encodingclassification.EncodingClassificationDO;
import com.miyu.module.mcc.dal.dataobject.encodingrule.EncodingRuleDO;
import com.miyu.module.mcc.dal.dataobject.materialtype.MaterialTypeDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.mcc.controller.admin.coderecord.vo.*;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 编码记录 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface CodeRecordMapper extends BaseMapperX<CodeRecordDO> {

    default PageResult<CodeRecordDO> selectPage(CodeRecordPageReqVO reqVO) {

        MPJLambdaWrapperX<CodeRecordDO> wrapper = new MPJLambdaWrapperX<CodeRecordDO>();
        wrapper.leftJoin(EncodingClassificationDO.class,EncodingClassificationDO::getId,CodeRecordDO::getClassificationId)
                .leftJoin(EncodingRuleDO.class,EncodingRuleDO::getId,CodeRecordDO::getEncodingRuleId)
                .selectAs(EncodingClassificationDO::getName,CodeRecordDO::getClassificationName)
                .selectAs(EncodingClassificationDO::getCode,CodeRecordDO::getClassificationCode)
                .selectAs(EncodingRuleDO::getName,CodeRecordDO::getEncodingRuleName)
                .selectAll(CodeRecordDO.class);


        return selectPage(reqVO, wrapper
                .betweenIfPresent(CodeRecordDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CodeRecordDO::getCode, reqVO.getCode())
                .likeIfPresent(CodeRecordDO::getName, reqVO.getName())
                .eqIfPresent(CodeRecordDO::getParentId, reqVO.getParentId())
                .eqIfPresent(CodeRecordDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CodeRecordDO::getEncodingRuleId, reqVO.getEncodingRuleId())
                .eqIfPresent(CodeRecordDO::getClassificationId, reqVO.getClassificationId())
                .orderByDesc(CodeRecordDO::getId));
    }


    @Select("SELECT * FROM mcc_code_record  where encoding_rule_id = #{encodingRuleId}  and deleted =0 and  status in (1,2) order by create_time desc limit 1")
    @Options(useCache = false)
    List<CodeRecordDO> getCodeRecordList(@Param("encodingRuleId")String encodingRuleId);

}