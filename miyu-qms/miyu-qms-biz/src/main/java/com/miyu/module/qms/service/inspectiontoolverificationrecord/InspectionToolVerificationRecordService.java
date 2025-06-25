package com.miyu.module.qms.service.inspectiontoolverificationrecord;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.inspectiontoolverificationrecord.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectiontoolverificationrecord.InspectionToolVerificationRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 检验工具校准记录 Service 接口
 *
 * @author Zhangyunfei
 */
public interface InspectionToolVerificationRecordService {

    /**
     * 创建检验工具校准记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionToolVerificationRecord(@Valid InspectionToolVerificationRecordSaveReqVO createReqVO);

    /**
     * 更新检验工具校准记录
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionToolVerificationRecord(@Valid InspectionToolVerificationRecordSaveReqVO updateReqVO);

    /**
     * 删除检验工具校准记录
     *
     * @param id 编号
     */
    void deleteInspectionToolVerificationRecord(String id);

    /**
     * 获得检验工具校准记录
     *
     * @param id 编号
     * @return 检验工具校准记录
     */
    InspectionToolVerificationRecordDO getInspectionToolVerificationRecord(String id);

    /**
     * 获得检验工具校准记录分页
     *
     * @param pageReqVO 分页查询
     * @return 检验工具校准记录分页
     */
    PageResult<InspectionToolVerificationRecordDO> getInspectionToolVerificationRecordPage(InspectionToolVerificationRecordPageReqVO pageReqVO);

    /**
     * 首页获取待送检集合
     * @return
     */
    PageResult<InspectionToolVerificationRecordDO> getToolVerificationTaskPage(InspectionToolVerificationRecordPageReqVO pageReqVO);
}
