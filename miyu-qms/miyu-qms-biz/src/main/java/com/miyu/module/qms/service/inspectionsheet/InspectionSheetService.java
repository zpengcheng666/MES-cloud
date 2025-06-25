package com.miyu.module.qms.service.inspectionsheet;

import javax.validation.*;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.*;
import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.InspectionSheetSchemePageReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo.InspectionMaterialOutBoundReqVO;
import com.miyu.module.qms.dal.dataobject.inspectionsheet.InspectionSheetDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import java.util.Collection;
import java.util.List;

/**
 * 检验单 Service 接口
 *
 * @author Zhangyunfei
 */
public interface InspectionSheetService {

    /**
     * 创建检验单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionSheet(@Valid InspectionSheetSaveReqVO createReqVO);

    /**
     * 更新检验单
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionSheet(@Valid InspectionSheetSaveReqVO updateReqVO);

    /**
     * 删除检验单
     *
     * @param id 编号
     */
    void deleteInspectionSheet(String id);

    /**
     * 获得检验单
     *
     * @param id 编号
     * @return 检验单
     */
    InspectionSheetDO getInspectionSheet(String id);

    /**
     * 获得检验单分页
     *
     * @param pageReqVO 分页查询
     * @return 检验单分页
     */
    PageResult<InspectionSheetDO> getInspectionSheetPage(InspectionSheetPageReqVO pageReqVO);


    /**
     * 专检领取检验任务获得检验单分页
     * @param pageReqVO
     * @return
     */
    PageResult<InspectionSheetSchemeDO> getInspectionSheetTaskPage(InspectionSheetTaskPageReqVO pageReqVO);

    // ==================== 子表（检验单方案任务计划） ====================

    /**
     * 获得检验单方案任务计划分页
     *
     * @param pageReqVO 分页查询
     * @return 检验单方案任务计划分页
     */
    PageResult<InspectionSheetSchemeDO> getInspectionSheetSchemePage(InspectionSheetSchemePageReqVO pageReqVO);

    /**
     * 创建检验单方案任务计划
     *
     * @param inspectionSheetScheme 创建信息
     * @return 编号
     */
    String createInspectionSheetScheme(@Valid InspectionSheetSchemeDO inspectionSheetScheme);

    /**
     * 更新检验单方案任务计划
     *
     * @param inspectionSheetScheme 更新信息
     */
    void updateInspectionSheetScheme(@Valid InspectionSheetSchemeDO inspectionSheetScheme);

    /**
     * 删除检验单方案任务计划
     *
     * @param id 编号
     */
    void deleteInspectionSheetScheme(String id);

	/**
	 * 获得检验单方案任务计划
	 *
	 * @param id 编号
     * @return 检验单方案任务计划
	 */
    InspectionSheetSchemeDO getInspectionSheetScheme(String id);


    /**
     * 获取检验单信息
     * @param reqVO
     * @return
     */
    InspectionSheetDO getInspectionSheetInfo(InspectionSheetReqVO reqVO);




    /**
     * 检测任务id获取检验单信息
     * @param schemeId
     * @return
     */
    InspectionSheetSchemeDO getInspectionSheetInfoBySchemeId(String schemeId);

    /**
     * 物料条码和批次号获取检验单信息
     * @param reqVO
     * @return
     */
    List<InspectionSheetSchemeMaterialDO> getInspectionSheetInfoMaterial(InspectionSheetMaterialReqVO reqVO);

    /**
     * 创建检验单(自检)
     * @param createReqVO
     * @return
     */
    String createInspectionSheetSelfInspection(InspectionSheetSelfCheckSaveReqVO createReqVO);

    /**
     * 单号查询检验单集合
     * @param numbers
     * @return
     */
    List<InspectionSheetDO> getInspectionSheetListByRecordNumber(Collection<String> numbers);

    /**
     * 单号查询检验单产品集合 (批量)
     * @param numbers
     * @return
     */
    List<InspectionSheetSchemeMaterialDO> getInspectionSheetMaterialListByRecordNumberBatch(Collection<String> numbers);

    /**
     * 检验单主键获取检验任务集合
     * @param sheetId
     * @return
     */
    List<InspectionSheetSchemeDO> getInspectionSheetSchemeListBySheetId(String sheetId);

    /**
     * 生产单号查询检验单产品集合
     * @param number
     * @return
     */
    List<InspectionSheetSchemeMaterialDO> getInspectionSheetMaterialListByRecordNumber(String number);


    /**
     * 检测任务id获取检验单
     * @param schemeId
     * @return
     */
    InspectionSheetDO getInspectionSheetBySchemeId(String schemeId);

    /**
     * 检验叫料，通知wms出库
     * @param req
     */
    Boolean outBoundInspection(InspectionMaterialOutBoundReqVO req);


    /**
     * 创建检验单(检验任务)
     * @param createReqVO
     * @return
     */
    String createInspectionSheetTask(InspectionSheetSaveReqVO createReqVO);

    /**
     * 获取待领取检验任务列表
     * @return
     */
    PageResult<InspectionSheetSchemeDO> getInspectionClaimTaskPage(InspectionSheetTaskPageReqVO pageReqVO);

    /**
     * 获取待检验的任务列表
     * @return
     */
    PageResult<InspectionSheetSchemeDO> getInspectionTaskPage(InspectionSheetTaskPageReqVO pageReqVO);

    /**
     * 获取不合格品的任务
     * @return
     */
    PageResult<InspectionSheetSchemeDO> getUnqualifiedTaskPage(InspectionSheetTaskPageReqVO pageReqVO);

    /**
     * 更新不合格品审批状态
     * @param businessKey
     * @param status
     */
    void updateUnqualifiedAuditStatus(String businessKey, Integer status);
}
