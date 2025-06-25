package com.miyu.module.qms.service.inspectionsheetsamplingrule;

import javax.validation.*;
import com.miyu.module.qms.controller.admin.inspectionsheetsamplingrule.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetsamplingrule.InspectionSheetSamplingRuleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 检验单抽样规则（检验抽样方案）关系 Service 接口
 *
 * @author Zhangyunfei
 */
public interface InspectionSheetSamplingRuleService {

    /**
     * 创建检验单抽样规则（检验抽样方案）关系
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionSheetSamplingRule(@Valid InspectionSheetSamplingRuleSaveReqVO createReqVO);

    /**
     * 更新检验单抽样规则（检验抽样方案）关系
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionSheetSamplingRule(@Valid InspectionSheetSamplingRuleSaveReqVO updateReqVO);

    /**
     * 删除检验单抽样规则（检验抽样方案）关系
     *
     * @param id 编号
     */
    void deleteInspectionSheetSamplingRule(String id);

    /**
     * 获得检验单抽样规则（检验抽样方案）关系
     *
     * @param id 编号
     * @return 检验单抽样规则（检验抽样方案）关系
     */
    InspectionSheetSamplingRuleDO getInspectionSheetSamplingRule(String id);

    /**
     * 获得检验单抽样规则（检验抽样方案）关系分页
     *
     * @param pageReqVO 分页查询
     * @return 检验单抽样规则（检验抽样方案）关系分页
     */
    PageResult<InspectionSheetSamplingRuleDO> getInspectionSheetSamplingRulePage(InspectionSheetSamplingRulePageReqVO pageReqVO);

}