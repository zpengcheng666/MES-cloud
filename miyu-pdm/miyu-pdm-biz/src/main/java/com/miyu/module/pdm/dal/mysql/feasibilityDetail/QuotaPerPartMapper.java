package com.miyu.module.pdm.dal.mysql.feasibilityDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.QuotaPerPartReqVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.QuotaPerPartDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuotaPerPartMapper extends BaseMapperX<QuotaPerPartDO> {

    default QuotaPerPartDO selectQuotaPerPart(QuotaPerPartReqVO reqVO) {
        return selectOne(new LambdaQueryWrapper<QuotaPerPartDO>()
                .eq(QuotaPerPartDO::getPartVersionId, reqVO.getPartVersionId()));
    }
}
