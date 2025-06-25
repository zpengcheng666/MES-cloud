package com.miyu.module.pdm.service.toolingTask;


import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingApplyReqVO;
import com.miyu.module.pdm.controller.admin.toolingTask.vo.ToolingTaskReqVO;
import com.miyu.module.pdm.controller.admin.toolingTask.vo.ToolingTaskRespVO;
import com.miyu.module.pdm.dal.dataobject.toolingTask.ToolingTaskDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 工装申请信息 Service 接口
 *
 * @author 上海弥彧
 */
public interface ToolingTaskService {

    /**
     * 创建工装申请信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */


    /**
     * 更新工装申请信息
     *
     * @param updateReqVO 更新信息
     */


    /**
     * 获得工装申请信息
     * @return 工装申请信息
     */
    List<ToolingTaskDO> getToolingTaskList(ToolingTaskReqVO reqVO);

    /**
     * 获得工装申请记录
     */
    ToolingTaskDO  getToolingTask(String id);


    /**
     * 通过选中项目获取工装列表
     */
    List<ToolingTaskRespVO> getToolingTaskDataList(ToolingTaskReqVO reqVO);

    List<ToolingTaskRespVO> getToolingDetailList(ToolingTaskReqVO reqVO);

    ToolingTaskRespVO getToolingDetailById(String id);

    //为评估任务添加负责人和评估截止日期
    String addToolingTask(@Valid ToolingTaskReqVO addReqVO);

    //为评估任务修改负责人和评估截止日期
    void updateToolingTask(@Valid ToolingTaskReqVO updateReqVO);

    void startDetailInstance(@Valid ToolingTaskReqVO updateReqVO);

    void updateApplyInstanceStatus(String id,Integer status);

    /**
     * 点击进度查看工装设计详情
     * @param id
     * @return
     */
    ToolingTaskReqVO getToolingDetail(String id);

    List<ToolingTaskRespVO>  getFileByCustomizedIndex(String customizedIndex);
}