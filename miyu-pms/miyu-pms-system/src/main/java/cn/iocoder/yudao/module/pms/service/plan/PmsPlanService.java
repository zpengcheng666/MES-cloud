package cn.iocoder.yudao.module.pms.service.plan;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.pms.controller.admin.materialPurchasePlan.vo.ResourceRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.plan.vo.*;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalPageReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.*;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomTreeRespDTO;

/**
 * 项目计划 Service 接口
 *
 * @author 芋道源码
 */
public interface PmsPlanService {

    /**
     * 创建项目计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createPlan(@Valid PmsPlanSaveReqVO createReqVO);

    /**
     * 创建项目计划审批
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createPlanBpm(@Valid PmsPlanSaveReqVO createReqVO);
    String createPlanBpm2(String id);

    /**
     * 更新状态
     *
     * @param id 编号
     * @param status 结果
     */
    void updatePlanStatus(String id, Integer status);
    /**
     * 更新项目计划
     *
     * @param updateReqVO 更新信息
     */
    void updatePlan(@Valid PmsPlanSaveReqVO updateReqVO);

    /**
     * 删除项目计划
     *
     * @param id 编号
     */
    void deletePlan(String id);

    /**
     * 获得项目计划
     *
     * @param id 编号
     * @return 项目计划
     */
    PmsPlanDO getPlan(String id);

    /**
     * 获得项目计划分页
     *
     * @param pageReqVO 分页查询
     * @return 项目计划分页
     */
    PageResult<PmsPlanDO> getPlanPage(PmsPlanPageReqVO pageReqVO);

    List<PmsPlanDO> selectListWith(PmsPlanSaveReqVO req);

    PmsPlanHandleVO batchSelect(List<PmsPlanHandleReqVO> data);

//    String batchPurchaseAndOrder(List<PmsPlanHandleReqVO> data);
    public String batchPurchase(List<PmsPlanHandleReqVO> data);
    //batchPurchase的替代,运用所有资源，更合理一点
    public String batchPurchase2(List<PmsPlanHandleReqVO> data);

    /**
     * 获得项目计划分页
     *
     * @param pageReqVO 分页查询
     * @return 项目计划分页
     */
    PageResult<OrderWithPlan> getPPOPage(PmsPlanPageReqVO pageReqVO);
    List<OrderWithPlan> getPlanToDo();

    /**
     * 物料采购计划，资源列表
     */
    ResourceRespVO getResource(String projectPlanItemId);

    ResourceRespVO getResourceByProjectIds(List<String> projectIds);

    ResourceRespVO getResourceAll();

    /**
     * 通过项目id,获得项目计划
     *
     * @param id 编号
     * @return 项目计划
     */
    public PmsPlanDO getByProjectId(String id);

    /**
     * 选择工艺方案
     * @param createReqVO
     */
    public void selectProcessScheme(PmsPlanSaveReqVO createReqVO);

    public List<PmsPlanDO> showProcessScheme();
    /**
     * 按项目编码查工艺
     * @param projectCodes
     * @return
     */
    public List<ProjPartBomTreeRespDTO> selectProcessSchemeList(List<String> projectCodes);

    // ==================== 子表（项目计划子表，产品计划完善） ====================

    public String createPlanItem(@Valid PmsPlanItemReqVO req) throws InterruptedException;
    public String createPlanItemByIds(List<String> ids) throws Exception;
    public String createPlanItemWithAmount(String id,Integer amount) throws Exception;
    public void deletePlanItem(String id);

    public void createMaterialPurchasePlan(MaterialPurchsePlanReqVO req);

    public void creatStepOutsourcePurchsePlan(StepOutsourcePurchseReqVO req);

    /**
     * 获得项目计划子表，产品计划完善列表
     *
     * @param projectPlanId 项目计划id
     * @return 项目计划子表，产品计划完善列表
     */
    List<PlanItemDO> getPlanItemListByProjectPlanId(String projectPlanId);

    /**
     * 直接通过项目id拿到子计划
     * @param projectId
     * @return
     */
    List<PlanItemDO> getPlanItemListByProjectId(String projectId);

    List<PlanItemDO> selectListMaterialUse(Collection<String> projectIds);
    //更新子订单,物料采购计划用
    void updatePlanItem(@Valid PmsPlanItemReqVO updateReqVO);
    void updatePlanItem2(@Valid PmsPlanItemReqVO updateReqVO);

    List<PlanItemDO> selectByMaterialId(String materialId);

    List<PlanItemDO> selectByProjectCodes(List<String> projectCodes);

    // ==================== 子表（项目计划子表，物料采购计划中的设备采购,已弃用） ====================

    /**
     * 获得项目计划子表，物料采购计划中的设备采购列表
     *
     * @param projectPlanId 项目计划id
     * @return 项目计划子表，物料采购计划中的设备采购列表
     */
    List<PlanDeviceDO> getPlanDeviceListByProjectPlanId(String projectPlanId);

    List<PlanDeviceDO> getPlanDeviceListByProjectPlanItemId(String projectPlanItemId);

    void saveDevice(String projectPlanItemId, List<PlanDeviceDO> list);

    void deleteDevice(String projectPlanItemId);

    // ==================== 子表（项目计划子表，物料采购计划中的工装采购,已弃用） ====================

    /**
     * 通过子计划id删除,因为实际上设备，工装，刀具都是子计划的子表
     * @param projectPlanItemId
     * @return
     */
    List<PlanMaterialDO> getPlanMaterialListByProjectPlanItemId(String projectPlanItemId);

    void deleteMaterial(String projectPlanItemId);

    void saveMaterial(String projectPlanItemId, List<PlanMaterialDO> list);
    // ==================== 子表（项目计划子表，物料采购计划中的刀具采购,已弃用） ====================
    List<PlanCombinationDO> getPlanCombinationListByProjectPlanItemId(String projectPlanItemId);

    void deleteCombination(String projectPlanItemId);

    void saveCombination(String projectPlanItemId, List<PlanCombinationDO> list);

    // ==================== 子表（项目计划子表，物料采购计划中的刀柄采购,这里及以下的评审采购,上面的采购已经弃用） ====================

    /**
     * 获得项目计划子表，物料采购计划中的设备采购列表
     *
     * @param projectPlanId 项目计划id
     * @return 项目计划子表，物料采购计划中的设备采购列表
     */
    List<PlanDemandHiltDO> getPlanDemandHiltListByProjectPlanId(String projectPlanId);

    // ==================== 子表（项目计划子表，物料采购计划中的工装采购） ====================

    /**
     * 获得项目计划子表，物料采购计划中的设备采购列表
     *
     * @param projectPlanId 项目计划id
     * @return 项目计划子表，物料采购计划中的设备采购列表
     */
    List<PlanDemandMaterialDO> getPlanDemandMaterialListByProjectPlanId(String projectPlanId);

    // ==================== 子表（项目计划子表，物料采购计划中的设备采购） ====================

    /**
     * 获得项目计划子表，物料采购计划中的设备采购列表
     *
     * @param projectPlanId 项目计划id
     * @return 项目计划子表，物料采购计划中的设备采购列表
     */
    List<PlanDemandDeviceDO> getPlanDemandDeviceListByProjectPlanId(String projectPlanId);

    // ==================== 子表（项目计划子表，物料采购计划中的刀具采购） ====================

    /**
     * 获得项目计划子表，物料采购计划中的设备采购列表
     *
     * @param projectPlanId 项目计划id
     * @return 项目计划子表，物料采购计划中的设备采购列表
     */
    List<PlanDemandCutterDO> getPlanDemandCutterListByProjectPlanId(String projectPlanId);

    // ==================== 子表（项目计划子表，物料采购计划中的物料采购） ====================

    /**
     * 查询物料采购，上面四个是评审得到的，这个是选择工艺方案时得到的。
     * @param projectPlanId
     * @return
     */
    List<PlanPurchaseMaterialDO> getPlanPurchaseMaterialListByProjectPlanId(String projectPlanId);

    List<PlanPurchaseMaterialDO> selectPurchaseMaterialByMaterialId(String materialId);

    List<PlanPurchaseMaterialDO> getPlanPurchaseMaterialListByProjectIds(List<String> ids);


}
