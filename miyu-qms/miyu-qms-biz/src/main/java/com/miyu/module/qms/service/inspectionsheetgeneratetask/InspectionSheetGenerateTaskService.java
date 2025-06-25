package com.miyu.module.qms.service.inspectionsheetgeneratetask;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.inspectionsheetgeneratetask.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetask.InspectionSheetGenerateTaskDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 检验单 Service 接口
 *
 * @author Zhangyunfei
 */
public interface InspectionSheetGenerateTaskService {

    /**
     * 创建检验单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionSheetGenerateTask(@Valid InspectionSheetGenerateTaskSaveReqVO createReqVO);

    /**
     * 更新检验单
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionSheetGenerateTask(@Valid InspectionSheetGenerateTaskSaveReqVO updateReqVO);

    /**
     * 删除检验单
     *
     * @param id 编号
     */
    void deleteInspectionSheetGenerateTask(String id);

    /**
     * 获得检验单
     *
     * @param id 编号
     * @return 检验单
     */
    InspectionSheetGenerateTaskDO getInspectionSheetGenerateTask(String id);

    /**
     * 获得检验单分页
     *
     * @param pageReqVO 分页查询
     * @return 检验单分页
     */
    PageResult<InspectionSheetGenerateTaskDO> getInspectionSheetGenerateTaskPage(InspectionSheetGenerateTaskPageReqVO pageReqVO);

    /**
     * 获取待生成的检验任务
     * @return
     */
    PageResult<InspectionSheetGenerateTaskDO> getInspectionSheetGenerateTaskListPage(InspectionSheetGenerateTaskPageReqVO pageReqVO);
}
