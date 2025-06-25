package com.miyu.module.qms.service.inspectionsheetgeneratetaskdetail;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.inspectionsheetgeneratetaskdetail.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetaskdetail.InspectionSheetGenerateTaskDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 检验单生成任务明细 Service 接口
 *
 * @author Zhangyunfei
 */
public interface InspectionSheetGenerateTaskDetailService {

    /**
     * 创建检验单生成任务明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionSheetGenerateTaskDetail(@Valid InspectionSheetGenerateTaskDetailSaveReqVO createReqVO);

    /**
     * 更新检验单生成任务明细
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionSheetGenerateTaskDetail(@Valid InspectionSheetGenerateTaskDetailSaveReqVO updateReqVO);

    /**
     * 删除检验单生成任务明细
     *
     * @param id 编号
     */
    void deleteInspectionSheetGenerateTaskDetail(String id);

    /**
     * 获得检验单生成任务明细
     *
     * @param id 编号
     * @return 检验单生成任务明细
     */
    InspectionSheetGenerateTaskDetailDO getInspectionSheetGenerateTaskDetail(String id);

    /**
     * 获得检验单生成任务明细分页
     *
     * @param pageReqVO 分页查询
     * @return 检验单生成任务明细分页
     */
    PageResult<InspectionSheetGenerateTaskDetailDO> getInspectionSheetGenerateTaskDetailPage(InspectionSheetGenerateTaskDetailPageReqVO pageReqVO);

    /**
     * 任务id 获取任务详细
     * @param id
     * @return
     */
    List<InspectionSheetGenerateTaskDetailDO> getInspectionSheetGenerateTaskDetailListByTaskId(String id);
}
