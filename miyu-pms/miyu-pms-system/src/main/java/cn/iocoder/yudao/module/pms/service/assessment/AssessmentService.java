package cn.iocoder.yudao.module.pms.service.assessment;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.AssessmentPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.AssessmentSaveReqVO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentReplenishDO;

/**
 * 项目评审 Service 接口
 *
 * @author 芋道源码
 */
public interface AssessmentService {

    /**
     * 创建项目评审
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createAssessment(@Valid AssessmentSaveReqVO createReqVO);

    /**
     * 更新项目评审
     *
     * @param updateReqVO 更新信息
     */
    void updateAssessment(@Valid AssessmentSaveReqVO updateReqVO);

    /**
     * 删除项目评审
     *
     * @param id 编号
     */
    void deleteAssessment(String id);

    /**
     * 获得项目评审
     *
     * @param id 编号
     * @return 项目评审
     */
    AssessmentDO getAssessment(String id);

    /**
     * 更新状态
     *
     * @param id 编号
     * @param status 结果
     */
    void updateAssessmentStatus(String id, Integer status);

    /**
     * 获得项目评审分页
     *
     * @param pageReqVO 分页查询
     * @return 项目评审分页
     */
    PageResult<AssessmentDO> getAssessmentPage(AssessmentPageReqVO pageReqVO);

    /**
     * 更新审批人
     * @param map
     */
    void updateAuditor(Map<String,String> map);

    // ==================== 子表（评审子表，评审补充） ====================

    /**
     * 获得评审子表，评审补充列表
     *
     * @param assessmentId 评审表id
     * @return 评审子表，评审补充列表
     */
    List<AssessmentReplenishDO> getAssessmentReplenishListByAssessmentId(String assessmentId);

    /**
     * 创建子评审
     * @param map
     * @return
     */
    public String createAssessmentReplenish(Map<String,String> map);
}
