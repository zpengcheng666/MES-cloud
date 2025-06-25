package com.miyu.module.tms.service.assembletask;

import java.util.*;
import javax.validation.*;
import com.miyu.module.tms.controller.admin.assembletask.vo.*;
import com.miyu.module.tms.dal.dataobject.assembletask.AssembleTaskDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;

/**
 * 刀具装配任务 Service 接口
 *
 * @author QianJy
 */
public interface AssembleTaskService {

    /**
     * 创建刀具装配任务
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createAssembleTask(@Valid AssembleTaskSaveReqVO createReqVO);

    /**
     * 更新刀具装配任务
     *
     * @param updateReqVO 更新信息
     */
    void updateAssembleTask(@Valid AssembleTaskSaveReqVO updateReqVO);

    /**
     * 删除刀具装配任务
     *
     * @param id 编号
     */
    void deleteAssembleTask(String id);

    /**
     * 获得刀具装配任务
     *
     * @param id 编号
     * @return 刀具装配任务
     */
    AssembleTaskDO getAssembleTask(String id);

    /**
     * 获得刀具装配任务分页
     *
     * @param pageReqVO 分页查询
     * @return 刀具装配任务分页
     */
    PageResult<AssembleTaskDO> getAssembleTaskPage(AssembleTaskPageReqVO pageReqVO);

    /**
     * 根据订单编码获得刀具装配任务列表
     * @param orderNumber
     * @return
     */
    List<AssembleTaskDO> getAssembleTaskListByOrderNumbers(Collection<String> orderNumber);
}