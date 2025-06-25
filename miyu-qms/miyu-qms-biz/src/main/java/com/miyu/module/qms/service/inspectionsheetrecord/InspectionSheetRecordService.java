package com.miyu.module.qms.service.inspectionsheetrecord;

import java.util.*;
import javax.validation.*;

import com.miyu.module.qms.controller.admin.analysis.vo.AnalysisReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetUpdateAuditReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetUpdateReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetUpdateTerminalReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetrecord.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetrecord.InspectionSheetRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 检验记录 Service 接口
 *
 * @author Zhangyunfei
 */
public interface InspectionSheetRecordService {

    /**
     * 创建检验记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionSheetRecord(@Valid InspectionSheetRecordSaveReqVO createReqVO);

    /**
     * 更新检验记录
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionSheetRecord(@Valid InspectionSheetRecordSaveReqVO updateReqVO);

    /**
     * 删除检验记录
     *
     * @param id 编号
     */
    void deleteInspectionSheetRecord(String id);

    /**
     * 获得检验记录
     *
     * @param id 编号
     * @return 检验记录
     */
    InspectionSheetRecordDO getInspectionSheetRecord(String id);

    /**
     * 获得检验记录分页
     *
     * @param pageReqVO 分页查询
     * @return 检验记录分页
     */
    PageResult<InspectionSheetRecordDO> getInspectionSheetRecordPage(InspectionSheetRecordPageReqVO pageReqVO);

    /**
     * 检验单产品id获取检验记录集合
     * @param materialId
     * @return
     */
    List<InspectionSheetRecordDO> getInspectionSheetRecordListByMaterialId(String materialId);

    /**
     * 产品检验
     * @param updateReqVO
     * @return
     */
    void updateInspectionRecord(InspectionSheetUpdateReqVO updateReqVO);


    /***
     * 获取检测项
     * @param reqVO
     * @return
     */
    List<InspectionSheetRecordDO> getInspectionSheetRecords(AnalysisReqVO reqVO);

    /**
     * 产品检验并提交审核
     * @param bean
     */
    void updateInspectionRecordAndAudit(InspectionSheetUpdateAuditReqVO bean);

    /**
     * 生产操作终端获取任务检测项集合
     * @param reqVO
     * @return
     */
    Map<String, Object> getInspectionSheetRecordList4Terminal(InspectionSheetRecordReqVO reqVO);

    /**
     * 生产操作终端产品检验
     * @param updateReqVO
     */
    void updateInspectionRecordTerminal(InspectionSheetUpdateTerminalReqVO updateReqVO);

    /**
     * 工序检验开始
     * @param bean
     */
    void updateMcsRecordBegin(InspectionSheetRecordReqVO bean);
}
