package com.miyu.module.qms.service.samplingruleconfig;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.samplingruleconfig.vo.*;
import com.miyu.module.qms.dal.dataobject.samplingruleconfig.SamplingRuleConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 抽样规则（检验抽样方案） Service 接口
 *
 * @author 芋道源码
 */
public interface SamplingRuleConfigService {

    /**
     * 创建抽样规则（检验抽样方案）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createSamplingRuleConfig(@Valid SamplingRuleConfigSaveBatchReqVO createReqVO);

    /**
     * 更新抽样规则（检验抽样方案）
     *
     * @param updateReqVO 更新信息
     */
    void updateSamplingRuleConfig(@Valid SamplingRuleConfigSaveReqVO updateReqVO);

    /**
     * 删除抽样规则（检验抽样方案）
     *
     * @param id 编号
     */
    void deleteSamplingRuleConfig(String id);

    /**
     * 获得抽样规则（检验抽样方案）
     *
     * @param id 编号
     * @return 抽样规则（检验抽样方案）
     */
    SamplingRuleConfigDO getSamplingRuleConfig(String id);

    /**
     * 获得抽样规则（检验抽样方案）分页
     *
     * @param pageReqVO 分页查询
     * @return 抽样规则（检验抽样方案）分页
     */
    PageResult<SamplingRuleConfigDO> getSamplingRuleConfigPage(SamplingRuleConfigPageReqVO pageReqVO);

    /***
     * 查询集合
     * @param code  样本码
     * @param standardId  标准ID
     * @param samplingRuleType 检查类型
     * @return
     */
    List<SamplingRuleConfigDO> getSamplingRuleConfigList(String code,String standardId,Integer samplingRuleType);


    Map<String,Object> getSamplingRuleConfigInfo(String standardId);



    String createSamplingRuleConfigs(List<SamplingRuleConfigDO> dos);

}