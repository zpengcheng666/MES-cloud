package com.miyu.module.pdm.dal.mysql.feasibilityDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.FeasibilityResultReqVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.FeasibilityResultDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeasibilityResultMapper extends BaseMapperX<FeasibilityResultDO> {

    default FeasibilityResultDO selectFeasibilityResult(FeasibilityResultReqVO reqVO) {
        return selectOne(new LambdaQueryWrapper<FeasibilityResultDO>()
                .eq(FeasibilityResultDO::getProjectCode, reqVO.getProjectCode())
                .eq(FeasibilityResultDO::getPartVersionId, reqVO.getPartVersionId()));
    }
}
