package com.miyu.module.wms.service.checkplan;

import java.util.*;
import javax.validation.*;
import com.miyu.module.wms.controller.admin.checkplan.vo.*;
import com.miyu.module.wms.dal.dataobject.checkplan.CheckPlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;

/**
 * 库存盘点计划 Service 接口
 *
 * @author QianJy
 */
public interface CheckPlanService {

    /**
     * 创建库存盘点计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCheckPlan(@Valid CheckPlanSaveReqVO createReqVO);

    /**
     * 更新库存盘点计划
     *
     * @param updateReqVO 更新信息
     */
    void updateCheckPlan(@Valid CheckPlanSaveReqVO updateReqVO);

    /**
     * 删除库存盘点计划
     *
     * @param id 编号
     */
    void deleteCheckPlan(String id);

    /**
     * 获得库存盘点计划
     *
     * @param id 编号
     * @return 库存盘点计划
     */
    CheckPlanDO getCheckPlan(String id);

    /**
     * 获得库存盘点计划分页
     *
     * @param pageReqVO 分页查询
     * @return 库存盘点计划分页
     */
    PageResult<CheckPlanDO> getCheckPlanPage(CheckPlanPageReqVO pageReqVO);
    PageResult<CheckPlanDO> getCheckTaskPage(CheckPlanPageReqVO pageReqVO);


    boolean updateCheckPlanStatus(String checkPlanId, Integer checkPlanStatus);

    /**
     * 提交库存盘点计划
     * @param checkPlanId
     */
    void submitCheckPlan(String checkPlanId);

    /**
     * 根据盘点容器ID更新盘点计划状态 为进行中
     * @param checkContainerId
     * @return
     */
    boolean updateCheckPlanStatusByCheckContainerId(String checkContainerId);

    /**
     * 根据盘点容器ID获取盘点计划
     * @return
     */
    CheckPlanDO getCheckPlanByCheckContainerId(String checkContainerId);

    /**
     * 校验是否可以创建盘点计划
     * @param checkPlan
     */
    void validateCanCreateCheckPlan(CheckPlanDO checkPlan);

    /**
     * 根据盘点区域ID获取未完成的盘点计划
     * @param checkAreaId
     * @return
     */
    List<CheckPlanDO> getNotFinishedCheckPlanByCheckAreaId(String checkAreaId);

    /**
     * 未完成的盘点计划 锁盘的
     * @return
     */
    List<CheckPlanDO> getNotFinishedCheckPlanAndLocked();


}