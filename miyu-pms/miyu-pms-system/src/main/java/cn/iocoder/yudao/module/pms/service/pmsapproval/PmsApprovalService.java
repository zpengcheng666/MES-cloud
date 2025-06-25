package cn.iocoder.yudao.module.pms.service.pmsapproval;

import javax.validation.*;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.OrderProcessVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalProcessVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;

import java.util.List;
import java.util.Map;

/**
 * pms 立项表,项目立项相关 Service 接口
 *
 * @author 芋道源码
 */
public interface PmsApprovalService {

    /**
     * 创建pms 立项表,项目立项相关
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createApproval(@Valid PmsApprovalSaveReqVO createReqVO);

    String createApprovalWithOrder(@Valid PmsApprovalSaveReqVO createReqVO);

    /**
     * 更新pms 立项表,项目立项相关
     *
     * @param updateReqVO 更新信息
     */
    void updateApproval(@Valid PmsApprovalSaveReqVO updateReqVO);

    /**
     * 更新pms 立项表,项目立项相关
     *
     * @param updateReqVO 更新信息
     */
    void closeApproval(@Valid PmsApprovalSaveReqVO updateReqVO);

    /**
     * 删除pms 立项表,项目立项相关
     *
     * @param id 编号
     */
    void deleteApproval(String id);

    /**
     * 获得pms 立项表,项目立项相关
     *
     * @param id 编号
     * @return pms 立项表,项目立项相关
     */
    PmsApprovalDO getApproval(String id);


    /**
     * 获得pms 立项表,项目立项相关分页
     *
     * @param pageReqVO 分页查询
     * @return pms 立项表,项目立项相关分页
     */
    PageResult<PmsApprovalDO> getApprovalPage(PmsApprovalPageReqVO pageReqVO);

    /**
     * 几乎和正常分页一样，只是多了一个评审通过的条件
     * @param pageReqVO
     * @return
     */
    PageResult<PmsApprovalDO> selectPageWithPass(PmsApprovalPageReqVO pageReqVO);

    /**
     * 项目查询,项目通过的,评审也通过的,
     * @param pageReqVO
     * @return
     */
    List<PmsApprovalDO> selectListWithPass(PmsApprovalPageReqVO pageReqVO);
    List<PmsApprovalDO> selectListWithPass2(PmsApprovalReqVO reqVO);
    List<PmsApprovalDO> selectListWithCondition(PmsApprovalReqVO reqVO);

    PageResult<PmsApprovalDO> selectPageWithStatus(PmsApprovalPageReqVO pageReqVO);

    /**
     * 更新状态
     *
     * @param id 编号
     * @param status 结果
     */
    void updateStatus(String id, Integer status);

    /**
     * 更新负责人
     * @param map
     */
    public String apponit(Map<String,String> map);

    /**
     * 发起立项流程
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createApprovalBpm(@Valid PmsApprovalSaveReqVO createReqVO);
    String createApprovalBpm2(@Valid PmsApprovalSaveReqVO createReqVO);

    /**
     * 查询所有项目
     * @return
     */
    public List<PmsApprovalDO> getApprovalAll();

    /**
     * 根据项目编码查询项目
     * @param code
     * @return
     */
    public PmsApprovalDO getByProjectCode(String code);
    /**
     * 根据审批状态查项目
     * @param status
     * @return
     */
    public List<PmsApprovalDO> getByStatus(List<Integer> status);
    /**
     * 根据审项目状态查询项目
     * @param status
     * @return
     */
    public List<PmsApprovalDO> getByProjectStatus(List<Integer> status);
    /**
     * 根据项目编码查询项目
     * @param codes
     * @return
     */
    public List<PmsApprovalDO> getByProjectCodes(List<String> codes);

    /**
     * 根据项目id获取项目订单
     * @param ids
     * @return
     */
    public List<PmsOrderDO> getPmsOrderList(List<String> ids);

    /**
     * 根据项目id获取项目计划
     * @param ids
     * @return
     */
    public List<PmsPlanDO> getPmsPlanList(List<String> ids);

    public List<PmsApprovalProcessVO> projectProgress();

    public List<OrderProcessVO> orderProgress();

    // ==================== 子表（项目评审） ====================

    /**
     * 获得项目评审列表
     *
     * @param projectId 项目id
     * @return 项目评审列表
     */
    List<AssessmentDO> getAssessmentListByProjectId(String projectId);

    // ==================== 子表（项目订单） ====================

    /**
     * 获得项目评审列表
     *
     * @param projectId 项目id
     * @return 项目评审列表
     */
    List<PmsOrderDO> getOrderListByProjectId(String projectId);

    /**
     * 根据项目id获取订单,现在是一对一
     * @param projectId
     * @return
     */
    PmsOrderDO getOrderByProjectId(String projectId);

    List<PmsOrderDO> getOrderListByProjectIds(List<String> projectIds);


}
