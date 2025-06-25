package com.miyu.module.qms.service.samplingrule;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.samplingrule.vo.*;
import com.miyu.module.qms.dal.dataobject.samplingrule.SamplingRuleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 抽样规则 Service 接口
 *
 * @author 芋道源码
 */
public interface SamplingRuleService {

    /**
     * 创建抽样规则
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createSamplingRule(@Valid SamplingRuleSaveReqVO createReqVO);

    /**
     * 更新抽样规则
     *
     * @param updateReqVO 更新信息
     */
    void updateSamplingRule(@Valid SamplingRuleSaveReqVO updateReqVO);

    /**
     * 删除抽样规则
     *
     * @param id 编号
     */
    void deleteSamplingRule(String id);

    /**
     * 获得抽样规则
     *
     * @param id 编号
     * @return 抽样规则
     */
    SamplingRuleDO getSamplingRule(String id);

    /**
     * 获得抽样规则分页
     *
     * @param pageReqVO 分页查询
     * @return 抽样规则分页
     */
    PageResult<SamplingRuleDO> getSamplingRulePage(SamplingRulePageReqVO pageReqVO);

    List<SamplingRuleDO> getSamplingRuleByStandardId(String standardId);


    /***
     * 获取抽检数字码表
     * @param standardId
     * @return
     */
    List<SamplingRuleInfoRespVO> getSamplingRuleInfo(String standardId);
}