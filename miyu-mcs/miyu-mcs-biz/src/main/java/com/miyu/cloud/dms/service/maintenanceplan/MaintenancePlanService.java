package com.miyu.cloud.dms.service.maintenanceplan;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.maintenanceplan.vo.MaintenancePlanPageReqVO;
import com.miyu.cloud.dms.controller.admin.maintenanceplan.vo.MaintenancePlanSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.maintenanceplan.MaintenancePlanDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 设备保养维护计划 Service 接口
 *
 * @author miyu
 */
public interface MaintenancePlanService {

    /**
     * 创建设备保养维护计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMaintenancePlan(@Valid MaintenancePlanSaveReqVO createReqVO);

    /**
     * 更新设备保养维护计划
     *
     * @param updateReqVO 更新信息
     */
    void updateMaintenancePlan(@Valid MaintenancePlanSaveReqVO updateReqVO);

    /**
     * 删除设备保养维护计划
     *
     * @param id 编号
     */
    void deleteMaintenancePlan(String id);

    /**
     * 获得设备保养维护计划
     *
     * @param id 编号
     * @return 设备保养维护计划
     */
    MaintenancePlanDO getMaintenancePlan(String id);

    /**
     * 获得设备保养维护计划分页
     *
     * @param pageReqVO 分页查询
     * @return 设备保养维护计划分页
     */
    PageResult<MaintenancePlanDO> getMaintenancePlanPage(MaintenancePlanPageReqVO pageReqVO);

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
     * @param id 计划ID
     */
    void reminderPlan(String id);

    /**
     * 获得设备保养维护计划列表
     *
     * @return
     */
    List<MaintenancePlanDO> getList();
}
