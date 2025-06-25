package com.miyu.cloud.dms.service.inspectionplan;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.inspectionplan.vo.InspectionPlanPageReqVO;
import com.miyu.cloud.dms.controller.admin.inspectionplan.vo.InspectionPlanSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.inspectionplan.InspectionPlanDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 设备检查计划 Service 接口
 *
 * @author miyu
 */
public interface InspectionPlanService {

    /**
     * 创建设备检查计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionPlan(@Valid InspectionPlanSaveReqVO createReqVO);

    /**
     * 更新设备检查计划
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionPlan(@Valid InspectionPlanSaveReqVO updateReqVO);

    /**
     * 删除设备检查计划
     *
     * @param id 编号
     */
    void deleteInspectionPlan(String id);

    /**
     * 获得设备检查计划
     *
     * @param id 编号
     * @return 设备检查计划
     */
    InspectionPlanDO getInspectionPlan(String id);

    /**
     * 获得设备检查计划分页
     *
     * @param pageReqVO 分页查询
     * @return 设备检查计划分页
     */
    PageResult<InspectionPlanDO> getInspectionPlanPage(InspectionPlanPageReqVO pageReqVO);

    /**
     * 检查此树是否存在计划
     *
     * @param treeId
     * @return
     */
    Boolean checkTree(String treeId);

    /**
     * 进行计划提醒
     *
     * @param id 计划id
     */
    void reminderInspectionPlan(String id);

    /**
     * 获得检查计划列表
     *
     * @return
     */
    List<InspectionPlanDO> getInspectionPlanList();

}
