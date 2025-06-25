package cn.iocoder.yudao.module.pms.dal.mysql.pmsapproval;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * pms 立项表,项目立项相关 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PmsApprovalMapper extends BaseMapperX<PmsApprovalDO> {

    default PageResult<PmsApprovalDO> selectPage2(PmsApprovalPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PmsApprovalDO>()
                .likeIfPresent(PmsApprovalDO::getProjectCode, reqVO.getProjectCode())
                .likeIfPresent(PmsApprovalDO::getProjectName, reqVO.getProjectName())
                .eqIfPresent(PmsApprovalDO::getProjectType, reqVO.getProjectType())
                .eqIfPresent(PmsApprovalDO::getProjectStatus, reqVO.getProjectStatus())
                .betweenIfPresent(PmsApprovalDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(PmsApprovalDO::getStatus, reqVO.getStatus())
                .orderByDesc(PmsApprovalDO::getId));
    }

    default PageResult<PmsApprovalDO> selectPage(PmsApprovalPageReqVO reqVO) {
        MPJLambdaWrapper<PmsApprovalDO> wrapper = JoinWrappers.lambda(PmsApprovalDO.class)
                .selectAll(PmsApprovalDO.class)
//                .selectCollection(PmsOrderDO.class,PmsApprovalDO::getOrderList)
//                .leftJoin(PmsOrderDO.class,PmsOrderDO::getProjectId,PmsApprovalDO::getId)
                .selectCollection(AssessmentDO.class,PmsApprovalDO::getAssessmentList)
                .leftJoin(AssessmentDO.class,AssessmentDO::getProjectId,PmsApprovalDO::getId);

        wrapper.likeIfExists(PmsApprovalDO::getProjectCode, reqVO.getProjectCode())
                .likeIfExists(PmsApprovalDO::getProjectName, reqVO.getProjectName())
                .eqIfExists(PmsApprovalDO::getProjectType, reqVO.getProjectType())
                .eqIfExists(PmsApprovalDO::getProjectStatus, reqVO.getProjectStatus())
                .eqIfExists(PmsApprovalDO::getStatus, reqVO.getStatus())
                .orderByDesc(PmsApprovalDO::getId);

        if(ObjectUtil.isNotNull(reqVO.getCreateTime())){
            wrapper.between(PmsApprovalDO::getCreateTime,reqVO.getCreateTime()[0],reqVO.getCreateTime()[1]);
        }
        PageResult<PmsApprovalDO> pmsApprovalDOPageResult = selectJoinPage(reqVO, PmsApprovalDO.class, wrapper);
        return pmsApprovalDOPageResult;
    }

    //需要项目审批通过,前端已经传了,还需要评审也通过,直接在这加条件
    default PageResult<PmsApprovalDO> selectPageWithPass(PmsApprovalPageReqVO reqVO) {
        MPJLambdaWrapper<PmsApprovalDO> wrapper = JoinWrappers.lambda(PmsApprovalDO.class)
                .selectAll(PmsApprovalDO.class)
                .selectCollection(AssessmentDO.class,PmsApprovalDO::getAssessmentList)
                .leftJoin(AssessmentDO.class,AssessmentDO::getProjectId,PmsApprovalDO::getId);


        wrapper.likeIfExists(PmsApprovalDO::getProjectCode, reqVO.getProjectCode())
                .likeIfExists(PmsApprovalDO::getProjectName, reqVO.getProjectName())
                .eqIfExists(PmsApprovalDO::getProjectType, reqVO.getProjectType())
                .eqIfExists(PmsApprovalDO::getProjectStatus, reqVO.getProjectStatus())
                .eqIfExists(PmsApprovalDO::getStatus, reqVO.getStatus())
                .eqIfExists(AssessmentDO::getStatus,2)
                .orderByDesc(PmsApprovalDO::getId);

        if(ObjectUtil.isNotNull(reqVO.getCreateTime())){
            wrapper.between(PmsApprovalDO::getCreateTime,reqVO.getCreateTime()[0],reqVO.getCreateTime()[1]);
        }
        return selectJoinPage(reqVO,PmsApprovalDO.class, wrapper);
    }

    //需要项目审批通过,前端已经传了,还需要评审也通过,直接在这加条件
    default PageResult<PmsApprovalDO> selectPageWithStatus(PmsApprovalPageReqVO reqVO) {
        LambdaQueryWrapperX<PmsApprovalDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.inIfPresent(PmsApprovalDO::getProjectStatus,reqVO.getProjectStatusList());
        return selectPage(reqVO,wrapperX);
    }

    //和上面那个一样，只不过是列表
    default List<PmsApprovalDO> selectListWithPass(PmsApprovalPageReqVO reqVO) {
        MPJLambdaWrapper<PmsApprovalDO> wrapper = JoinWrappers.lambda(PmsApprovalDO.class)
                .selectAll(PmsApprovalDO.class)
                .selectCollection(AssessmentDO.class,PmsApprovalDO::getAssessmentList)
                .leftJoin(AssessmentDO.class,AssessmentDO::getProjectId,PmsApprovalDO::getId);


        wrapper.likeIfExists(PmsApprovalDO::getProjectCode, reqVO.getProjectCode())
                .likeIfExists(PmsApprovalDO::getProjectName, reqVO.getProjectName())
                .eqIfExists(PmsApprovalDO::getProjectType, reqVO.getProjectType())
                .eqIfExists(PmsApprovalDO::getProjectStatus, reqVO.getProjectStatus())
                .eqIfExists(PmsApprovalDO::getStatus, reqVO.getStatus())
                .eqIfExists(AssessmentDO::getStatus,2)
                .orderByDesc(PmsApprovalDO::getId);

        if(ObjectUtil.isNotNull(reqVO.getCreateTime())){
            wrapper.between(PmsApprovalDO::getCreateTime,reqVO.getCreateTime()[0],reqVO.getCreateTime()[1]);
        }
        return selectJoinList(PmsApprovalDO.class, wrapper);
    }

    //和上面那个一样，只不过是列表
    default List<PmsApprovalDO> selectListWithPass2(PmsApprovalReqVO reqVO) {
        MPJLambdaWrapper<PmsApprovalDO> wrapper = JoinWrappers.lambda(PmsApprovalDO.class)
                .selectAll(PmsApprovalDO.class)
                .selectCollection(AssessmentDO.class,PmsApprovalDO::getAssessmentList)
                .leftJoin(AssessmentDO.class,AssessmentDO::getProjectId,PmsApprovalDO::getId);


        wrapper.likeIfExists(PmsApprovalDO::getProjectCode, reqVO.getProjectCode())
                .likeIfExists(PmsApprovalDO::getProjectName, reqVO.getProjectName())
                .eqIfExists(PmsApprovalDO::getProjectType, reqVO.getProjectType())
                .eqIfExists(PmsApprovalDO::getProjectStatus, reqVO.getProjectStatus())
                .eqIfExists(PmsApprovalDO::getStatus, reqVO.getStatus())
                .eqIfExists(AssessmentDO::getStatus,2)
                .orderByDesc(PmsApprovalDO::getId);

        if(ObjectUtil.isNotNull(reqVO.getCreateTime())){
            wrapper.between(PmsApprovalDO::getCreateTime,reqVO.getCreateTime()[0],reqVO.getCreateTime()[1]);
        }
        return selectJoinList(PmsApprovalDO.class, wrapper);
    }

    //普通的条件查询
    default List<PmsApprovalDO> selectListWithCondition(PmsApprovalReqVO reqVO) {
        LambdaQueryWrapperX<PmsApprovalDO> wrapper = new LambdaQueryWrapperX<>();
                wrapper.likeIfPresent(PmsApprovalDO::getProjectCode, reqVO.getProjectCode())
                .likeIfPresent(PmsApprovalDO::getProjectName, reqVO.getProjectName())
                .eqIfPresent(PmsApprovalDO::getProjectType, reqVO.getProjectType())
                .eqIfPresent(PmsApprovalDO::getProjectStatus, reqVO.getProjectStatus())
                .eqIfPresent(PmsApprovalDO::getStatus, reqVO.getStatus())
                .orderByDesc(PmsApprovalDO::getId);

        if(ObjectUtil.isNotNull(reqVO.getCreateTime())){
            wrapper.between(PmsApprovalDO::getCreateTime,reqVO.getCreateTime()[0],reqVO.getCreateTime()[1]);
        }
        return selectList(wrapper);
    }

    default  List<PmsApprovalDO> selectByIds(Collection<String> ids) {

        return selectList(PmsApprovalDO::getId,ids);
    }

}
