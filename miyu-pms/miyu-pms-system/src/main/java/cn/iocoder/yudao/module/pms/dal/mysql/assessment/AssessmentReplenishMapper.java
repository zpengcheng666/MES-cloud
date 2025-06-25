package cn.iocoder.yudao.module.pms.dal.mysql.assessment;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentReplenishDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评审子表，评审补充 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AssessmentReplenishMapper extends BaseMapperX<AssessmentReplenishDO> {

    default List<AssessmentReplenishDO> selectListByAssessmentId(String assessmentId) {
        return selectList(AssessmentReplenishDO::getAssessmentId, assessmentId);
    }

    default int deleteByAssessmentId(String assessmentId) {
        return delete(AssessmentReplenishDO::getAssessmentId, assessmentId);
    }

}
