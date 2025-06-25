package cn.iocoder.yudao.module.pms.dal.mysql.assessment;

import java.util.*;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.AssessmentPageReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 项目评审 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AssessmentMapper extends BaseMapperX<AssessmentDO> {

    default PageResult<AssessmentDO> selectPage2(AssessmentPageReqVO reqVO) {

        return selectPage(reqVO, new LambdaQueryWrapperX<AssessmentDO>()
                .eqIfPresent(AssessmentDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(AssessmentDO::getAssessmentStatus, reqVO.getAssessmentStatus())
                .eqIfPresent(AssessmentDO::getConclusion, reqVO.getConclusion())
                .betweenIfPresent(AssessmentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AssessmentDO::getId)

        );
    }

    default PageResult<AssessmentDO> selectPage(AssessmentPageReqVO reqVO) {
        MPJLambdaWrapper<AssessmentDO> wrapper = JoinWrappers.lambda(AssessmentDO.class)
                .selectAll(AssessmentDO.class)
                .selectAs(PmsApprovalDO::getProjectName,AssessmentDO::getProjectName)
                .leftJoin(PmsApprovalDO.class,PmsApprovalDO::getId,AssessmentDO::getProjectId);

        wrapper.eqIfExists(AssessmentDO::getProjectId, reqVO.getProjectId())
                .eqIfExists(AssessmentDO::getReasonType,reqVO.getReasonType())
                .eqIfExists(AssessmentDO::getAssessmentStatus, reqVO.getAssessmentStatus())
                .eqIfExists(AssessmentDO::getConclusion, reqVO.getConclusion())
                .orderByDesc(AssessmentDO::getId);

        if(ObjectUtil.isNotNull(reqVO.getCreateTime())){
            wrapper.between(AssessmentDO::getCreateTime,reqVO.getCreateTime()[0],reqVO.getCreateTime()[1]);
        }

        return selectJoinPage(reqVO,AssessmentDO.class,wrapper);
    }

    default PageResult<AssessmentDO> selectPage(String id) {
        MPJLambdaWrapper<AssessmentDO> wrapper = JoinWrappers.lambda(AssessmentDO.class)
                .selectAll(AssessmentDO.class)
                .selectAs(PmsApprovalDO::getProjectName,AssessmentDO::getProjectName)
                .leftJoin(PmsApprovalDO.class,PmsApprovalDO::getId,AssessmentDO::getProjectId);

        return null;
    }

    default List<AssessmentDO> selectListByProjectId(String projectId) {
        return selectList(AssessmentDO::getProjectId, projectId);
    }

    default int deleteByProjectId(String projectId) {
        return delete(AssessmentDO::getProjectId, projectId);
    }

}
